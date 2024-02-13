package lt.vitalijus.translator_kmm.translate.domain.history

import lt.vitalijus.translator_kmm.core.domain.language.Language
import lt.vitalijus.translator_kmm.core.domain.util.Resource
import lt.vitalijus.translator_kmm.translate.domain.translate.TranslateException

class Insert(
    private val historyDataSource: HistoryDataSource
) {

    suspend fun execute(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language,
        toText: String
    ): Resource<Unit> {
        return try {
            historyDataSource.insertHistoryItem(
                HistoryItem(
                    id = null,
                    fromLanguageCode = fromLanguage.langCode,
                    fromText = fromText,
                    toLanguageCode = toLanguage.langCode,
                    toText = toText
                )
            )

            Resource.Success(data = Unit)
        } catch (e: TranslateException) {
            e.printStackTrace()
            Resource.Error(throwable = e)
        }
    }
}
