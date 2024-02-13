package lt.vitalijus.translator_kmm.translate.presentation

import lt.vitalijus.translator_kmm.core.presentation.UiLanguage
import lt.vitalijus.translator_kmm.translate.domain.translate.TranslateError

data class TranslateState(
    val fromText: String = "",
    val toText: String? = null,
    val isTranslating: Boolean = false,
    val fromLanguage: UiLanguage = UiLanguage.byCode(langCode = "en"),
    val toLanguage: UiLanguage = UiLanguage.byCode(langCode = "de"),
    val isChoosingFromLanguage: Boolean = false,
    val isChoosingToLanguage: Boolean = false,
    val error: TranslateError? = null,
    val history: List<UiHistoryItem> = emptyList()
)
