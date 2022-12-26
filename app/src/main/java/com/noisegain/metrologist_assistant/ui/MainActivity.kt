package com.noisegain.metrologist_assistant.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material.icons.rounded.UploadFile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noisegain.metrologist_assistant.core.log
import com.noisegain.metrologist_assistant.ui.screens.MainScreen
import com.noisegain.metrologist_assistant.ui.screens.PassportsScreen
import com.noisegain.metrologist_assistant.ui.screens.ShowPassportScreen
import com.noisegain.metrologist_assistant.ui.theme.CircleButton
import com.noisegain.metrologist_assistant.ui.theme.MetrologistAssistantTheme
import com.noisegain.metrologist_assistant.ui.utils.NavigationComponent
import com.noisegain.metrologist_assistant.ui.utils.Navigator
import com.noisegain.metrologist_assistant.ui.utils.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    private val getFile = registerForActivityResult(ActivityResultContracts.GetContent()) {
        log(it)
        if (it != null) viewModel.loadContent(contentResolver, it)
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
                            onClick = { getFile.launch("text/*") },
                            icon = Icons.Rounded.UploadFile
                        )
                        NavigationComponent(navigator = navigator)
                    }
                }
            }
        }
    }
}
