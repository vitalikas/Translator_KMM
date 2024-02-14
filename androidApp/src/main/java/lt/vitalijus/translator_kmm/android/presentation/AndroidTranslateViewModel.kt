package lt.vitalijus.translator_kmm.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import lt.vitalijus.translator_kmm.translate.domain.history.HistoryDataSource
import lt.vitalijus.translator_kmm.translate.domain.history.Insert
import lt.vitalijus.translator_kmm.translate.domain.translate.Translate
import lt.vitalijus.translator_kmm.translate.presentation.TranslateEvent
import lt.vitalijus.translator_kmm.translate.presentation.TranslateViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidTranslateViewModel @Inject constructor(
    private val translate: Translate,
    private val insert: Insert,
    historyDataSource: HistoryDataSource
) : ViewModel() {

    private val viewModel by lazy {
        TranslateViewModel(
            translate = translate,
            insert = insert,
            historyDataSource = historyDataSource,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: TranslateEvent) {
        viewModel.onEvent(event = event)
    }
}
