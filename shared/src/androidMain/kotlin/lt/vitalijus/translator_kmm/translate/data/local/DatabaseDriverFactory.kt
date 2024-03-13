package lt.vitalijus.translator_kmm.translate.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import lt.vitalijus.translator_kmm.TranslateDatabase

actual class DatabaseDriverFactory(
    private val context: Context
) {

    actual fun create(): SqlDriver =
        AndroidSqliteDriver(TranslateDatabase.Schema, context, DatabaseContract.DB_NAME)
}
