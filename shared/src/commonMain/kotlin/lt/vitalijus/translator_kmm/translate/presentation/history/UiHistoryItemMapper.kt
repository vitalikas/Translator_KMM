package lt.vitalijus.translator_kmm.translate.presentation.history

import lt.vitalijus.translator_kmm.core.presentation.UiLanguage
import lt.vitalijus.translator_kmm.translate.domain.history.HistoryItem

fun HistoryItem.toUiHistoryItem(): UiHistoryItem? = id?.let {
    UiHistoryItem(
        id = it,
        fromText = fromText,
        toText = toText,
        fromLanguage = UiLanguage.byCode(langCode = fromLanguageCode),
        toLanguage = UiLanguage.byCode(langCode = toLanguageCode)
    )
}
