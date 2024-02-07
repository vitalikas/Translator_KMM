package lt.vitalijus.translator_kmm.core.domain.util

import kotlinx.coroutines.DisposableHandle

fun interface DisposableHandle : DisposableHandle

// equivalent without functional interface
fun disposableHandle(block: () -> Unit): DisposableHandle {
    return object : DisposableHandle {
        override fun dispose() {
            block()
        }
    }
}