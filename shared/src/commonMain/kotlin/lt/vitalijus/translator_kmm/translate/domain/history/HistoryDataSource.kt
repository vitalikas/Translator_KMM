package lt.vitalijus.translator_kmm.translate.domain.history

import lt.vitalijus.translator_kmm.core.domain.util.CommonFlow

interface HistoryDataSource {

    fun getHistory(): CommonFlow<List<HistoryItem>>
    suspend fun insertHistoryItem(item: HistoryItem)
}
