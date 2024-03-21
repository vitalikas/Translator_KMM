package lt.vitalijus.translator_kmm.translate.data.translate

import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import lt.vitalijus.translator_kmm.NetworkConstants.BASE_URL
import lt.vitalijus.translator_kmm.core.domain.language.Language
import lt.vitalijus.translator_kmm.core.domain.util.Result
import lt.vitalijus.translator_kmm.core.domain.util.TranslateError
import lt.vitalijus.translator_kmm.translate.domain.translate.TranslateClient

class KtorTranslateClient(
    private val httpClient: HttpClient
) : TranslateClient {

    override suspend fun translate(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): Result<String, TranslateError> {
        return try {
            val response = httpClient.post {
                url("$BASE_URL/translate")
                contentType(ContentType.Application.Json)
                setBody(
                    TranslateDto(
                        textToTranslate = fromText,
                        sourceLanguageCode = fromLanguage.langCode,
                        targetLanguageCode = toLanguage.langCode
                    )
                )
            }
            when (response.status.value) {
                in 200..299 -> try {
                    val translatedText = response.body<TranslatedDto>().translatedText
                    Result.Success(data = translatedText)
                } catch (e: NoTransformationFoundException) {
                    Result.Error(error = TranslateError.SERVER_ERROR)
                }

                500 -> Result.Error(error = TranslateError.SERVER_ERROR)
                in 400..499 -> Result.Error(error = TranslateError.CLIENT_ERROR)
                else -> Result.Error(error = TranslateError.UNKNOWN_ERROR)
            }
        } catch (e: IOException) {
            Result.Error(error = TranslateError.SERVICE_UNAVAILABLE)
        }
    }
}
