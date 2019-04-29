package jetbrains.datalore.base.gcommon.collect

import jetbrains.datalore.base.function.Predicate

actual object Iterables {
    private fun checkNonNegative(position: Int) {
        if (position < 0) {
            throw IndexOutOfBoundsException(position.toString())
        }
    }

    actual fun <T> toList(iterable: Iterable<T>): List<T> {
        return iterable.toList()
    }

    actual fun isEmpty(iterable: Iterable<*>): Boolean {
        return (iterable as? Collection<*>)?.isEmpty() ?: !iterable.iterator().hasNext()
    }

    actual fun <T> filter(unfiltered: Iterable<T>, retainIfTrue: Predicate<in T>): Iterable<T> {
        return unfiltered.filter(retainIfTrue)
    }

    actual fun <T> all(iterable: Iterable<T>, predicate: Predicate<in T>): Boolean {
        return iterable.all(predicate)
    }

    actual fun <T> concat(a: Iterable<T>, b: Iterable<T>): Iterable<T> {
        return a.union(b)
    }

    actual operator fun <T> get(iterable: Iterable<T>, position: Int): T {
        checkNonNegative(position)
        if (iterable is List<*>) {
            return (iterable as List<T>)[position]
        }

        val it = iterable.iterator()
        for (i in 0..position) {
            if (i == position) {
                return it.next()
            }
            it.next()
        }
        throw IndexOutOfBoundsException(position.toString())
    }

    actual operator fun <T> get(iterable: Iterable<T>, position: Int, defaultValue: T): T {
        checkNonNegative(position)
        if (iterable is List<*>) {
            val list = iterable as List<T>
            return if (position < list.size) list[position] else defaultValue
        }
        val it = iterable.iterator()
        var i = 0
        while (i <= position && it.hasNext()) {
            if (i == position) {
                return it.next()
            }
            it.next()
            i++
        }
        return defaultValue
    }

    actual fun <T> find(iterable: Iterable<T>, predicate: Predicate<in T>, defaultValue: T): T {
        return iterable.find(predicate)?:defaultValue
    }

    actual fun <T> getLast(iterable: Iterable<T>): T {
        return iterable.last()
    }

    internal actual fun toArray(iterable: Iterable<*>): Array<*> {
        val collection: Collection<*>
        if (iterable is Collection<*>) {
            collection = iterable
        } else {
            collection = iterable.toList()
        }
        return collection.toTypedArray()
    }

}