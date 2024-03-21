package lt.vitalijus.translator_kmm.android.translate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import lt.vitalijus.translator_kmm.translate.domain.history.GetUsecase
import lt.vitalijus.translator_kmm.translate.domain.history.InsertUsecase
import lt.vitalijus.translator_kmm.translate.domain.translate.TranslateUsecase
import lt.vitalijus.translator_kmm.translate.presentation.translate.TranslateEvent
import lt.vitalijus.translator_kmm.translate.presentation.translate.TranslateViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidTranslateViewModel @Inject constructor(
    private val translateUsecase: TranslateUsecase,
    private val insertUsecase: InsertUsecase,
    private val getUsecase: GetUsecase
) : ViewModel() {

    private val viewModel by lazy {
        TranslateViewModel(
            translateUsecase = translateUsecase,
            insertUsecase = insertUsecase,
            getUsecase = getUsecase,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: TranslateEvent) = viewModel.onEvent(event = event)
}
