package com.noisegain.metrologist_assistant.ui

import android.content.Intent
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.UploadFile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noisegain.metrologist_assistant.core.log
import com.noisegain.metrologist_assistant.core.uuid
import com.noisegain.metrologist_assistant.domain.Passport
import com.noisegain.metrologist_assistant.ui.screens.EditPassportScreen
import com.noisegain.metrologist_assistant.ui.screens.MainScreen
import com.noisegain.metrologist_assistant.ui.screens.PassportsScreen
import com.noisegain.metrologist_assistant.ui.screens.ShowPassportScreen
import com.noisegain.metrologist_assistant.ui.theme.CircleButton
import com.noisegain.metrologist_assistant.ui.theme.MetrologistAssistantTheme
import com.noisegain.metrologist_assistant.ui.utils.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    private val importPassports = registerForActivityResult(ActivityResultContracts.GetContent()) {
        log(it)
        if (it != null) viewModel.importPassports(contentResolver, it)
    }

    private val loadContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        log(it)
        it ?: return@registerForActivityResult
        val passport = viewModel.curPassport.value ?: return@registerForActivityResult
        val filename = passport.uuid + "." + MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(contentResolver.getType(it))
        val uri = FileProvider.getUriForFile(
            this,
            "com.noisegain.metrologist_assistant.fileprovider",
            File(filesDir, filename)
        )
        viewModel.loadContent(
            contentResolver,
            it,
            contentResolver.openOutputStream(uri)!!,
            filename
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetrologistAssistantTheme {
                //getFile.launch("text/*")
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val navigator = remember { Navigator(navController, viewModel) }
                    Box(Modifier.fillMaxSize()) {
                        CircleButton(
                            modifier = Modifier
                                .align(alignment = Alignment.TopEnd)
                                .padding(16.dp)
                                .zIndex(1f),
                            onClick = { importPassports.launch("text/*") },
                            icon = Icons.Rounded.UploadFile
                        )
                        NavigationComponent(navigator = navigator)
                    }
                }
            }
        }
    }

    inner class Navigator(
        val navController: NavHostController,
        val viewModel: MainViewModel
    ) {

        private val _sharedFlow = MutableSharedFlow<Screen>(extraBufferCapacity = 1)
        val sharedFlow = _sharedFlow.asSharedFlow()

        private fun navigateTo(navTarget: Screen) {
            _sharedFlow.tryEmit(navTarget)
        }

        fun navigateBack() {
            navController.popBackStack()
        }

        fun onPassportClick(passport: Passport) {
            log("Selected: $passport")
            viewModel.selectPassport(passport)
            navigateTo(Screen.ShowPassport)
        }

        fun onFilterClick(filter: (Passport) -> Boolean) {
            viewModel.filterPassports(filter)
        }

        fun onFilterFromMainClick(filter: (Passport) -> Boolean) {
            onFilterClick(filter)
            navigateTo(Screen.Passports)
        }

        fun onEditPassportClick(passport: Passport) {
            viewModel.selectPassport(passport)
            navigateTo(Screen.EditPassport)
        }

        fun onLoadPhotoClick(passport: Passport) {
            viewModel.setLoadTypePhoto()
            if (passport.photoUri == null) {
                loadContent.launch("*/*")
            } else {
                viewFileIntent(passport.photoUri)
            }
        }

        fun onLoadCertificateClick(passport: Passport) {
            viewModel.setLoadTypeCertificate()
            if (passport.certificateUri == null) {
                loadContent.launch("*/*")
            } else {
                viewFileIntent(passport.certificateUri)
            }
        }

        fun onRemovePhotoClick(passport: Passport) {
            viewModel.setLoadTypePhoto()
            val filename = passport.photoUri ?: return
            File(filesDir, filename).delete()
            viewModel.removeContent(passport)
        }

        fun onRemoveCertificateClick(passport: Passport) {
            viewModel.setLoadTypeCertificate()
            val filename = passport.certificateUri ?: return
            File(filesDir, filename).delete()
            viewModel.removeContent(passport)
        }

        fun onSavePassportClick(passport: Passport) {
            viewModel.selectPassport(passport)
            viewModel.savePassport(passport)
            navigateTo(Screen.ShowPassport)
        }

        fun onExportClick(filename: String) {
            val uri = FileProvider.getUriForFile(
                this@MainActivity,
                "com.noisegain.metrologist_assistant.fileprovider",
                File(filesDir, "$filename.xlsx")
            )
            viewModel.exportPassports(contentResolver.openOutputStream(uri)!!)
        }

        fun onShowExportedClick() {
            viewFileIntent(filename = "test.xlsx")
        }

        private fun viewFileIntent(filename: String) {
            Intent(Intent.ACTION_VIEW).apply {
                val file = File(filesDir, filename)
                val uri = FileProvider.getUriForFile(
                    this@MainActivity,
                    "com.noisegain.metrologist_assistant.fileprovider",
                    file
                )
                setDataAndType(
                    uri,
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
                )
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                startActivity(this)
            }
        }
    }

    companion object {
        @Composable
        fun NavigationComponent(
            navigator: Navigator
        ) {
            LaunchedEffect("navigation") {
                navigator.sharedFlow.onEach {
                    navigator.navController.navigate(it.route)
                }.launchIn(this)
            }

            NavHost(
                navController = navigator.navController,
                startDestination = Screen.Main.route
            ) {
                composable(Screen.Passports.route) {
                    PassportsScreen(
                        navigator.viewModel.filteredPassports.collectAsState(),
                        navigator::onPassportClick,
                        navigator::onFilterClick,
                        navigator::onExportClick,
                        navigator::onShowExportedClick,
                    )
                }
                composable(Screen.ShowPassport.route) {
                    ShowPassportScreen(
                        navigator.viewModel.curPassport.collectAsState(),
                        navigator::onLoadPhotoClick,
                        navigator::onLoadCertificateClick,
                        navigator::onEditPassportClick,
                    )
                }
                composable(Screen.Main.route) {
                    MainScreen(navigator::onFilterFromMainClick)
                }
                composable(Screen.EditPassport.route) {
                    EditPassportScreen(
                        navigator.viewModel.curPassport.collectAsState(),
                        navigator::onLoadPhotoClick,
                        navigator::onLoadCertificateClick,
                        navigator::onRemovePhotoClick,
                        navigator::onRemoveCertificateClick,
                        navigator::onSavePassportClick,
                    )
                }
            }
        }
    }

}
