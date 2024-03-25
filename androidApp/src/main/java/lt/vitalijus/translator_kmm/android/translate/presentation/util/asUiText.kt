package lt.vitalijus.translator_kmm.android.translate.presentation.util

import lt.vitalijus.translator_kmm.android.R
import lt.vitalijus.translator_kmm.android.core.presentation.UiText
import lt.vitalijus.translator_kmm.core.domain.util.Result
import lt.vitalijus.translator_kmm.core.domain.util.TranslateError

fun TranslateError.asUiText() : UiText = when (this) {
    TranslateError.SERVICE_UNAVAILABLE -> UiText.StringResource(R.string.error_service_unavailable)
    TranslateError.CLIENT_ERROR -> UiText.StringResource(R.string.error_client)
    TranslateError.SERVER_ERROR -> UiText.StringResource(R.string.error_server)
    TranslateError.UNKNOWN_ERROR -> UiText.StringResource(R.string.error_unknown)
}

fun Result.Error<*, TranslateError>.asTranslateErrorUiText(): UiText = this.error.asUiText()
