package lt.vitalijus.translator_kmm.android.di

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import lt.vitalijus.translator_kmm.TranslateDatabase
import lt.vitalijus.translator_kmm.translate.data.history.SqlDelightHistoryDataSource
import lt.vitalijus.translator_kmm.translate.data.local.DatabaseDriverFactory
import lt.vitalijus.translator_kmm.translate.data.remote.HttpClientFactory
import lt.vitalijus.translator_kmm.translate.data.translate.KtorTranslateClient
import lt.vitalijus.translator_kmm.translate.domain.history.HistoryDataSource
import lt.vitalijus.translator_kmm.translate.domain.translate.TranslateClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesHttpClient(): HttpClient = HttpClientFactory().create()

    @Provides
    @Singleton
    fun providesTranslateClient(
        httpClient: HttpClient
    ): TranslateClient = KtorTranslateClient(httpClient = httpClient)

    @Provides
    @Singleton
    fun providesDatabaseDriver(
        app: Application
    ): SqlDriver = DatabaseDriverFactory(context = app).create()

    @Provides
    @Singleton
    fun providesHistoryDataSource(
        driver: SqlDriver
    ): HistoryDataSource =
        SqlDelightHistoryDataSource(db = TranslateDatabase.invoke(driver = driver))
}
