package lt.vitalijus.translator_kmm.translate.presentation.translate

import lt.vitalijus.translator_kmm.core.presentation.UiLanguage
import lt.vitalijus.translator_kmm.translate.presentation.history.UiHistoryItem

sealed class TranslateEvent {

    data class ChooseFromLanguage(val language: UiLanguage): TranslateEvent()
    data class ChooseToLanguage(val language: UiLanguage): TranslateEvent()
    object StopChoosingLanguage: TranslateEvent()
    object SwapLanguages: TranslateEvent()
    data class ChangeTranslationText(val text: String): TranslateEvent()
    object Translate: TranslateEvent()
    object OpenFromLanguageDropDown: TranslateEvent()
    object OpenToLanguageDropDown: TranslateEvent()
    object CloseTranslation: TranslateEvent()
    data class SelectHistoryItem(val item: UiHistoryItem): TranslateEvent()
    object EditTranslation: TranslateEvent()
    data class Speak(val isSpeak: Boolean): TranslateEvent()
    object RecordAudio: TranslateEvent()
    data class SubmitVoiceResult(val result: String?): TranslateEvent()
    object OnErrorSeen: TranslateEvent()
}
