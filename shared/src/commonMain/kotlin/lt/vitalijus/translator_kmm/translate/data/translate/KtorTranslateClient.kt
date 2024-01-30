package lt.vitalijus.translator_kmm.translate.data.translate

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import lt.vitalijus.translator_kmm.NetworkConstants
import lt.vitalijus.translator_kmm.core.domain.language.Language
import lt.vitalijus.translator_kmm.translate.domain.translate.TranslateClient
import lt.vitalijus.translator_kmm.translate.domain.translate.TranslateError
import lt.vitalijus.translator_kmm.translate.domain.translate.TranslateException

class KtorTranslateClient(
    private val httpClient: HttpClient
) : TranslateClient {

    override suspend fun translate(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): String {
        val result = try {
            httpClient.post {
                url(NetworkConstants.BASE_URL + "/translate")
                contentType(ContentType.Application.Json)
                setBody(
                    TranslateDto(
                        textToTranslate = fromText,
                        sourceLanguageCode = fromLanguage.langCode,
                        targetLanguageCode = toLanguage.langCode
                    )
                )
            }
        } catch (e: IOException) {
            throw TranslateException(TranslateError.SERVICE_UNAVAILABLE)
        }

        return when (result.status.value) {
            in 200..299 -> try {
                result.body<TranslatedDto>().translatedText
            } catch (e: Exception) {
                throw TranslateException(TranslateError.SERVER_ERROR)
            }
            500 -> throw TranslateException(TranslateError.SERVER_ERROR)
            in 400..499 -> throw TranslateException(TranslateError.CLIENT_ERROR)
            else -> throw TranslateException(TranslateError.UNKNOWN_ERROR)
        }
    }
}