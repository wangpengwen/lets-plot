/*
 * Copyright (c) 2019 JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 *
 * This file has been modified by JetBrains : Java code has been converted to Kotlin code.
 *
 * THE FOLLOWING IS THE COPYRIGHT OF THE ORIGINAL DOCUMENT:
 *
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package jetbrains.datalore.base.gcommon.collect

import jetbrains.datalore.base.function.Predicate

object Iterables {
    private fun checkNonNegative(position: Int) {
        if (position < 0) {
            throw IndexOutOfBoundsException(position.toString())
        }
    }

    fun <T> toList(iterable: Iterable<T>): List<T> {
        return iterable.toList()
    }

    fun size(iterable: Iterable<*>): Int {
        return iterable.count()
    }

    fun isEmpty(iterable: Iterable<*>): Boolean {
        return (iterable as? Collection<*>)?.isEmpty() ?: !iterable.iterator().hasNext()
    }

    fun <T> filter(unfiltered: Iterable<T>, retainIfTrue: Predicate<in T>): Iterable<T> {
        return unfiltered.filter(retainIfTrue)
    }

    fun <T> all(iterable: Iterable<T>, predicate: Predicate<in T>): Boolean {
        return iterable.all(predicate)
    }

    fun <T> concat(a: Iterable<T>, b: Iterable<T>): Iterable<T> {
        return a + b
    }

    operator fun <T> get(iterable: Iterable<T>, position: Int): T {
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

    operator fun <T> get(iterable: Iterable<T>, position: Int, defaultValue: T): T {
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

    fun <T> find(iterable: Iterable<T>, predicate: Predicate<in T>, defaultValue: T): T {
        return iterable.find(predicate) ?: defaultValue
    }

    fun <T> getLast(iterable: Iterable<T>): T {
        return iterable.last()
    }

    internal fun toArray(iterable: Iterable<*>): Array<*> {
        val collection: Collection<*>
        if (iterable is Collection<*>) {
            collection = iterable
        } else {
            collection = iterable.toList()
        }
        return collection.toTypedArray()
    }
}