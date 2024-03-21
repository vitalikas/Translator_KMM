package lt.vitalijus.translator_kmm.core.domain.util

sealed interface HistoryError : Error {

    enum class InsertError : HistoryError {

        DATABASE_ERROR
    }

    enum class GetError : HistoryError {

        DATABASE_ERROR
    }
}
