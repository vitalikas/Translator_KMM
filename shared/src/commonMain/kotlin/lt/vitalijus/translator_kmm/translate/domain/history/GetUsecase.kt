package lt.vitalijus.translator_kmm.translate.domain.history

import lt.vitalijus.translator_kmm.core.domain.util.CommonFlow
import lt.vitalijus.translator_kmm.core.domain.util.HistoryError
import lt.vitalijus.translator_kmm.core.domain.util.Result

class GetUsecase(
    private val historyDataSource: HistoryDataSource
) {

    operator fun invoke(): Result<CommonFlow<List<HistoryItem>>, HistoryError.GetError> =
        historyDataSource.getHistory()
}
