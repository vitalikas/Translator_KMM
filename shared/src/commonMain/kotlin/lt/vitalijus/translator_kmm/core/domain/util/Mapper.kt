package lt.vitalijus.translator_kmm.core.domain.util

interface Mapper<in I, out O> {
    fun map(from: I): O
}
