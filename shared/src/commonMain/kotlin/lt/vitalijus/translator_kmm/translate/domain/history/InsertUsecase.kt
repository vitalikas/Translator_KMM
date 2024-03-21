package lt.vitalijus.translator_kmm.translate.domain.history

import lt.vitalijus.translator_kmm.core.domain.language.Language
import lt.vitalijus.translator_kmm.core.domain.util.HistoryError
import lt.vitalijus.translator_kmm.core.domain.util.Result

class InsertUsecase(
    private val historyDataSource: HistoryDataSource
) {

    suspend operator fun invoke(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language,
        toText: String
    ): Result<Unit, HistoryError.InsertError> =
        historyDataSource.insertHistoryItem(
            HistoryItem(
                id = null,
                fromLanguageCode = fromLanguage.langCode,
                fromText = fromText,
                toLanguageCode = toLanguage.langCode,
                toText = toText
            )
        )
}
