package lt.vitalijus.translator_kmm.translate.domain.translate

import lt.vitalijus.translator_kmm.core.domain.language.Language
import lt.vitalijus.translator_kmm.core.domain.util.Result
import lt.vitalijus.translator_kmm.core.domain.util.TranslateError

class TranslateUsecase(
    private val client: TranslateClient
) {

    suspend operator fun invoke(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): Result<String, TranslateError> =
        client.translate(
            fromLanguage = fromLanguage,
            fromText = fromText,
            toLanguage = toLanguage
        )
}
