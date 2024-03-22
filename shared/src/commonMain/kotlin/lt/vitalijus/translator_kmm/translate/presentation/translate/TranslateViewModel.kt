package lt.vitalijus.translator_kmm.translate.presentation.translate

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import lt.vitalijus.translator_kmm.core.domain.util.Result
import lt.vitalijus.translator_kmm.translate.domain.history.GetUsecase
import lt.vitalijus.translator_kmm.translate.domain.history.InsertUsecase
import lt.vitalijus.translator_kmm.translate.domain.translate.TranslateUsecase
import lt.vitalijus.translator_kmm.translate.presentation.history.toUiHistoryItem

class TranslateViewModel(
    private val translateUsecase: TranslateUsecase,
    private val insertUsecase: InsertUsecase,
    private val getUsecase: GetUsecase,
    coroutineScope: CoroutineScope?
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private var translateJob: Job? = null

    private val _state = MutableStateFlow(TranslateState())
    val state = combine(
        _state,
        getHistoryFlow()
    ) { state, history ->
        if (state.history != history && history.isNotEmpty()) {
            state.copy(
                history = history.mapNotNull { item ->
                    item.toUiHistoryItem()
                }
            )
        } else state
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            TranslateState()
        )

    private fun getHistoryFlow() = when (val result = getUsecase()) {
        is Result.Error -> emptyFlow()

        is Result.Success -> result.data
    }

    fun onEvent(event: TranslateEvent) {
        when (event) {
            is TranslateEvent.ChangeTranslationText -> {
                _state.update {
                    it.copy(
                        fromText = event.text
                    )
                }
            }

            is TranslateEvent.ChooseFromLanguage -> {
                _state.update {
                    it.copy(
                        isChoosingFromLanguage = false,
                        fromLanguage = event.language
                    )
                }
            }

            is TranslateEvent.ChooseToLanguage -> {
                val newState = _state.updateAndGet {
                    it.copy(
                        isChoosingToLanguage = false,
                        toLanguage = event.language
                    )
                }
                translate(state = newState)
            }

            is TranslateEvent.CloseTranslation -> {
                _state.update {
                    it.copy(
                        isTranslating = false,
                        fromText = "",
                        toText = null
                    )
                }
            }

            is TranslateEvent.EditTranslation -> {
                if (state.value.toText != null) {
                    _state.update {
                        it.copy(
                            isTranslating = false,
                            toText = null
                        )
                    }
                }
            }

            is TranslateEvent.OnErrorSeen -> {
                _state.update {
                    it.copy(
                        error = null
                    )
                }
            }

            is TranslateEvent.OpenFromLanguageDropDown -> {
                _state.update {
                    it.copy(
                        isChoosingFromLanguage = true
                    )
                }
            }

            is TranslateEvent.OpenToLanguageDropDown -> {
                _state.update {
                    it.copy(
                        isChoosingToLanguage = true
                    )
                }
            }

            is TranslateEvent.SelectHistoryItem -> {
                translateJob?.cancel()
                _state.update {
                    it.copy(
                        fromText = event.item.fromText,
                        toText = event.item.toText,
                        isTranslating = false,
                        fromLanguage = event.item.fromLanguage,
                        toLanguage = event.item.toLanguage
                    )
                }
            }

            is TranslateEvent.StopChoosingLanguage -> {
                _state.update {
                    it.copy(
                        isChoosingFromLanguage = false,
                        isChoosingToLanguage = false
                    )
                }
            }

            is TranslateEvent.SubmitVoiceResult -> {
                _state.update {
                    it.copy(
                        fromText = event.result ?: it.fromText,
                        isTranslating = if (event.result != null) false else it.isTranslating,
                        toText = if (event.result != null) null else it.toText
                    )
                }
            }

            is TranslateEvent.SwapLanguages -> {
                _state.update {
                    it.copy(
                        fromLanguage = it.toLanguage,
                        toLanguage = it.fromLanguage,
                        fromText = it.toText ?: "",
                        toText = if (it.toText != null) it.fromText else null
                    )
                }
            }

            is TranslateEvent.Translate -> {
                translate(state = state.value)
            }

            is TranslateEvent.Speak -> {
                _state.update {
                    it.copy(
                        isSpeak = !it.isSpeak
                    )
                }
            }

            else -> Unit
        }
    }

    private fun translate(state: TranslateState) {
        if (state.isTranslating || state.fromText.isBlank()) {
            return
        }

        translateJob = viewModelScope.launch {
            // Using _state.value = state.value.copy() may cause race condition.
            _state.update {
                it.copy(isTranslating = true)
            }
            val result = translateUsecase(
                fromLanguage = state.fromLanguage.language,
                fromText = state.fromText,
                toLanguage = state.toLanguage.language
            )
            when (result) {
                is Result.Success -> {
                    insertUsecase(
                        fromLanguage = state.fromLanguage.language,
                        fromText = state.fromText,
                        toLanguage = state.toLanguage.language,
                        toText = result.data
                    )

                    _state.update {
                        it.copy(
                            isTranslating = false,
                            toText = result.data
                        )
                    }
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isTranslating = false,
                            error = result.error
                        )
                    }
                }
            }
        }
    }
}
