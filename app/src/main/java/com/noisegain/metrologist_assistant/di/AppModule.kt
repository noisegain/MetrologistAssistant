package com.noisegain.metrologist_assistant.di

import android.content.Context
import androidx.room.Room
import com.noisegain.metrologist_assistant.data.*
import com.noisegain.metrologist_assistant.domain.ExportedRepository
import com.noisegain.metrologist_assistant.domain.PassportsParser
import com.noisegain.metrologist_assistant.domain.PassportsRepository
import com.noisegain.metrologist_assistant.domain.writer.ExcelWriter
import com.noisegain.metrologist_assistant.domain.writer.ExportActWriter
import com.noisegain.metrologist_assistant.domain.writer.ReportWriter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.*

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

    @Binds
    fun bindExportedRepository(exportedRepositoryImpl: ExportedRepositoryImpl): ExportedRepository

    @Binds
    fun bindPassportsParser(passportsParserImpl: PassportsParserImpl): PassportsParser

    companion object {
        @Provides
        fun provideAppDatabase(
            @ApplicationContext context: Context,
        ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "passports").build()

        @Provides
        fun providePassportsDAO(database: AppDatabase): PassportsDAO = database.passportsDAO()

        @Provides
        fun provideExportedDAO(database: AppDatabase): ExportedDAO = database.exportedDAO()

        @Provides
        fun bindReportWriter(): ReportWriter = ReportWriterImpl(EnumMap(ExcelWriter.Styles::class.java))

        @Provides
        fun bindExportActWriter(): ExportActWriter = ExportActWriterImpl(EnumMap(ExcelWriter.Styles::class.java))
    }
}