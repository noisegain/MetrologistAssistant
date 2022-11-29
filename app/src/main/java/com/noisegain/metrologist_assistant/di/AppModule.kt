package com.noisegain.metrologist_assistant.di

import com.noisegain.metrologist_assistant.data.MainRepositoryImpl
import com.noisegain.metrologist_assistant.data.MainRepositoryLoggerImpl
import com.noisegain.metrologist_assistant.domain.MainRepository
import com.noisegain.metrologist_assistant.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<MainRepository> {
        MainRepositoryImpl()
    }
    single<MainRepository>(named("log")) {
        MainRepositoryLoggerImpl(get())
    }
    viewModel {
        MainViewModel(get(named("log")))
    }
}