package lt.vitalijus.translator_kmm.translate.domain.translate

import lt.vitalijus.translator_kmm.core.domain.language.Language
import lt.vitalijus.translator_kmm.core.domain.util.Resource

class Translate(
    private val client: TranslateClient
) {

    suspend fun execute(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): Resource<String> =
        try {
            val translatedText = client.translate(
                fromLanguage = fromLanguage,
                fromText = fromText,
                toLanguage = toLanguage
            )

            Resource.Success(data = translatedText)
        } catch (e: TranslateException) {
            e.printStackTrace()
            Resource.Error(throwable = e)
        }
}
