package com.noisegain.metrologist_assistant.di

import android.content.Context
import androidx.room.Room
import com.noisegain.metrologist_assistant.data.AppDatabase
import com.noisegain.metrologist_assistant.data.PassportsRepositoryImpl
import com.noisegain.metrologist_assistant.data.PassportsDAO
import com.noisegain.metrologist_assistant.domain.PassportsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/*val appModule = module {
    log("HELLO")
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "passports").build().also {
            log("DB Created")
        }
    }
    single {
        get<AppDatabase>().userDao()
    }
    single<PassportsRepository> {
        MainRepositoryImpl(get())
    }
    single<PassportsRepository>(named("log")) {
        MainRepositoryLoggerImpl(get())
    }
    viewModel {
        MainViewModel(get(named("log")))
    }
}*/
@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Binds
    fun bindMainRepository(mainRepositoryImpl: PassportsRepositoryImpl): PassportsRepository

    companion object {
        @Provides
        fun provideAppDatabase(
            @ApplicationContext context: Context,
        ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "passports").build()

        @Provides
        fun providePassportsDAO(database: AppDatabase): PassportsDAO = database.passportsDAO()
    }
}