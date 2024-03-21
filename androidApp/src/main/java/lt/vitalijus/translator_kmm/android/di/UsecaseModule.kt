package lt.vitalijus.translator_kmm.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lt.vitalijus.translator_kmm.translate.domain.history.GetUsecase
import lt.vitalijus.translator_kmm.translate.domain.history.HistoryDataSource
import lt.vitalijus.translator_kmm.translate.domain.history.InsertUsecase
import lt.vitalijus.translator_kmm.translate.domain.translate.TranslateClient
import lt.vitalijus.translator_kmm.translate.domain.translate.TranslateUsecase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsecaseModule {

    @Provides
    @Singleton
    fun providesTranslateUsecase(
        client: TranslateClient
    ): TranslateUsecase = TranslateUsecase(client = client)

    @Provides
    @Singleton
    fun providesInsertUsecase(
        historyDataSource: HistoryDataSource
    ): InsertUsecase = InsertUsecase(historyDataSource = historyDataSource)

    @Provides
    @Singleton
    fun providesGetUsecase(
        historyDataSource: HistoryDataSource
    ): GetUsecase = GetUsecase(historyDataSource = historyDataSource)
}
