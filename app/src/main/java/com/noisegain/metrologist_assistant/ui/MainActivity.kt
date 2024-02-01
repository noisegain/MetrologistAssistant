package com.noisegain.metrologist_assistant.ui

import android.content.Intent
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noisegain.metrologist_assistant.core.log
import com.noisegain.metrologist_assistant.core.uuid
import com.noisegain.metrologist_assistant.domain.entity.Exported
import com.noisegain.metrologist_assistant.domain.entity.Passport
import com.noisegain.metrologist_assistant.domain.entity.Report
import com.noisegain.metrologist_assistant.ui.screens.*
import com.noisegain.metrologist_assistant.ui.theme.CircleButton
import com.noisegain.metrologist_assistant.ui.theme.MetrologistAssistantTheme
import com.noisegain.metrologist_assistant.ui.utils.Filter
import com.noisegain.metrologist_assistant.ui.utils.Filters
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

    val importPassports = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) viewModel.importPassports(contentResolver, it)
    }

    private val loadContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it ?: return@registerForActivityResult
        val passport = viewModel.curPassport.value ?: return@registerForActivityResult
        val filename = passport.uuid + "." + MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(it))
        val uri = FileProvider.getUriForFile(
                this,
                Tags.PROVIDER,
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
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val navigator = remember { Navigator(navController, viewModel) }
                    Box(Modifier.fillMaxSize()) {
//                        CircleButton(
//                                onClick = { navigator.navigateTo(Screen.ExportScreen) },
//                                icon = Icons.Rounded.Explore
//                        )
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

        private var curFilter: Filter = Filters.All

        fun navigateTo(navTarget: Screen) {
            _sharedFlow.tryEmit(navTarget)
        }

        private fun acceptMainFilter(filter: Filter) {
            curFilter = filter
        }

        private fun navigateBack() {
            navController.popBackStack()
        }

        fun onPassportClick(passport: Passport) {
            log("Selected: $passport")
            viewModel.selectPassport(passport)
            navigateTo(Screen.ShowPassport)
        }

        fun onFilterButtonClick() {
            navigateTo(Screen.FilterScreen)
        }

        private fun onFilterClick(filter: Filter) {
            viewModel.filterPassports(Filter.combine(curFilter, filter).filter)
        }

        fun onFilterFromMainClick(filter: Filter) {
            acceptMainFilter(filter)
            onFilterClick(filter)
            navigateTo(Screen.Passports)
        }

        fun onFilterFromPassportsClick(filter: Filter) {
            onFilterClick(filter)
            navigateBack()
        }

        fun onFilterFromSelectClick(filter: Filter) {
            onFilterClick(filter)
            navigateTo(Screen.SelectPassports)
        }

        fun onSummaryExportClick() {
            viewModel.selectedAction()
            navigateTo(Screen.ExportScreen)
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


        private fun onRemoveClick(passport: Passport, field: String?) {
            val filename = field ?: return
            File(filesDir, filename).delete()
            viewModel.removeContent(passport)
        }

        fun onRemovePhotoClick(passport: Passport) {
            viewModel.setLoadTypePhoto()
            onRemoveClick(passport, passport.photoUri)
        }

        fun onRemoveCertificateClick(passport: Passport) {
            viewModel.setLoadTypeCertificate()
            onRemoveClick(passport, passport.certificateUri)
        }

        fun onSavePassportClick(passport: Passport) {
            viewModel.selectPassport(passport)
            viewModel.savePassport(passport)
            navigateTo(Screen.ShowPassport)
        }

        fun onSelectReportTypeClick(type: Report.Type) {
            viewModel.setReportType(type)
        }

        fun onExportActExportClick(filename: String) {
            viewModel.selectExportAct()
            onExportClick(filename)
        }

        fun onExportClick(filename: String) {
            Toast.makeText(this@MainActivity, "Exporting...", Toast.LENGTH_SHORT).show()
            viewModel.onExportClick(filename)
            Toast.makeText(this@MainActivity, "Exported successfully", Toast.LENGTH_SHORT).show()
        }

        fun findReports() = viewModel.exported

        fun onClearPassportsClick() {
            viewModel.clearPassports()
        }

        fun onUploadPassportsClick() {
            importPassports.launch("text/*")
        }

        fun viewExport(export: Exported) {
            viewFileIntent("$REPORTS_DIR/${export.uri}")
        }

        fun deleteReport(export: Exported) {
            viewModel.deleteExport(export)
        }

        fun onSettingsClick() {
            navigateTo(Screen.Settings)
        }

        fun onShowExportedClick() {
            navigateTo(Screen.Reports)
        }

        private fun viewFileIntent(filename: String) {
            Intent(Intent.ACTION_VIEW).apply {
                val file = File(filesDir, filename)
                val uri = FileProvider.getUriForFile(
                        this@MainActivity,
                        Tags.PROVIDER,
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
        const val REPORTS_DIR = "reports"

        @Composable
        fun NavigationComponent(
                navigator: Navigator
        ) {
            LaunchedEffect("navigation") {
                navigator.sharedFlow.onEach {
                    navigator.navController.navigate(it.route) {
                        it.popUpTo?.let(::popUpTo)
                    }
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
                            navigator::onFilterButtonClick,
                            navigator::onExportClick,
                            navigator::onShowExportedClick,
                            navigator::onSelectReportTypeClick
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
                    MainScreen(
                            navigator::onFilterFromMainClick,
                            { navigator.navigateTo(Screen.ExportScreen) },
                            navigator::onSettingsClick
                    )
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
                composable(Screen.Reports.route) {
                    ExportsScreen(
                            navigator.findReports().collectAsState(),
                            navigator::viewExport,
                            navigator::deleteReport
                    )
                }
                composable(Screen.FilterScreen.route) {
                    FilterPassportsScreen(
                            navigator::onFilterFromPassportsClick
                    )
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(
                            navigator::onClearPassportsClick,
                            navigator::onUploadPassportsClick,
                            navigator::onShowExportedClick
                    )
                }
                composable(Screen.ExportScreen.route) {
                    SelectedPassportsOnExport(
                            navigator.viewModel,
                            navigator::onExportActExportClick,
                    ) { navigator.navController.navigate(Screen.SelectPassports.route) }
                }
                composable(Screen.SelectPassports.route) {
                    SelectPassportsScreen(
                            navigator.viewModel.filteredPassportsCheckBox.collectAsState(),
                            navigator::onSummaryExportClick
                    ) {
                        navigator.navigateTo(Screen.FilterForSelect)
                    }
                }
                composable(Screen.FilterForSelect.route) {
                    FilterPassportsScreen(onFilterPressed = navigator::onFilterFromSelectClick)
                }
            }
        }

        object Tags {
            const val PROVIDER = "com.noisegain.metrologist_assistant.fileprovider"
        }
    }
}
