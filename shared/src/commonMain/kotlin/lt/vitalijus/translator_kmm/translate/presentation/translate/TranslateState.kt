package lt.vitalijus.translator_kmm.translate.presentation.translate

import lt.vitalijus.translator_kmm.core.presentation.UiLanguage
import lt.vitalijus.translator_kmm.core.domain.util.TranslateError
import lt.vitalijus.translator_kmm.translate.presentation.history.UiHistoryItem

data class TranslateState(
    val fromText: String = "",
    val toText: String? = null,
    val isTranslating: Boolean = false,
    val fromLanguage: UiLanguage = UiLanguage.byCode(langCode = "en"),
    val toLanguage: UiLanguage = UiLanguage.byCode(langCode = "de"),
    val isChoosingFromLanguage: Boolean = false,
    val isChoosingToLanguage: Boolean = false,
    val error: TranslateError? = null,
    val history: List<UiHistoryItem> = emptyList(),
    val isSpeak: Boolean = false
)
