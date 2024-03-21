package lt.vitalijus.translator_kmm.translate.data.history

import app.cash.sqldelight.coroutines.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import lt.vitalijus.translator_kmm.TranslateDatabase
import lt.vitalijus.translator_kmm.core.domain.util.CommonFlow
import lt.vitalijus.translator_kmm.core.domain.util.HistoryError
import lt.vitalijus.translator_kmm.core.domain.util.Result
import lt.vitalijus.translator_kmm.core.domain.util.toCommonFlow
import lt.vitalijus.translator_kmm.translate.domain.history.HistoryDataSource
import lt.vitalijus.translator_kmm.translate.domain.history.HistoryItem

class SqlDelightHistoryDataSource(
    db: TranslateDatabase
) : HistoryDataSource {

    private val queries = db.translateQueries

    override fun getHistory(): Result<CommonFlow<List<HistoryItem>>, HistoryError.GetError>  = try {
        val flow = queries
            .getHistory()
            .asFlow()
            .map { query ->
                query.executeAsList()
            }
            .map { history ->
                history.map { historyEntity ->
                    historyEntity.toHistoryItem()
                }
            }
            .toCommonFlow()
        Result.Success(data = flow)
    } catch (e: Exception) {
        Result.Error(error = HistoryError.GetError.DATABASE_ERROR)
    }


    override suspend fun insertHistoryItem(item: HistoryItem): Result<Unit, HistoryError.InsertError> = try {
        queries.insertHistoryEntity(
            id = item.id,
            fromLanguageCode = item.fromLanguageCode,
            fromText = item.fromText,
            toLanguageCode = item.toLanguageCode,
            toText = item.toText,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
        Result.Success(data = Unit)
    } catch (e: Exception) {
        Result.Error(error = HistoryError.InsertError.DATABASE_ERROR)
    }
}
