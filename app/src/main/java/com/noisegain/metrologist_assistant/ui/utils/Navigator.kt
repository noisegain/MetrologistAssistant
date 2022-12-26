package com.noisegain.metrologist_assistant.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.noisegain.metrologist_assistant.core.log
import com.noisegain.metrologist_assistant.domain.Passport
import com.noisegain.metrologist_assistant.ui.MainViewModel
import com.noisegain.metrologist_assistant.ui.screens.MainScreen
import com.noisegain.metrologist_assistant.ui.screens.PassportsScreen
import com.noisegain.metrologist_assistant.ui.screens.ShowPassportScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class Navigator(
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
}

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
                navigator::onFilterClick
            )
        }
        composable(Screen.ShowPassport.route) {
            ShowPassportScreen(navigator.viewModel.curPassport.collectAsState())
        }
        composable(Screen.Main.route) {
            MainScreen(navigator::onFilterFromMainClick)
        }
    }
}
