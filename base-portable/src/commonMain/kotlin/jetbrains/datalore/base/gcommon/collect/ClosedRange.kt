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

import jetbrains.datalore.base.gcommon.collect.Comparables.gt
import jetbrains.datalore.base.gcommon.collect.Comparables.gte
import jetbrains.datalore.base.gcommon.collect.Comparables.ls
import jetbrains.datalore.base.gcommon.collect.Comparables.lse
import jetbrains.datalore.base.gcommon.collect.Comparables.max
import jetbrains.datalore.base.gcommon.collect.Comparables.min

class ClosedRange<T : Comparable<T>>(private val myLower: T, private val myUpper: T) {

    val isEmpty: Boolean
        get() = myLower.compareTo(myUpper) == 0

    init {
        if (myLower > myUpper) {
            throw IllegalArgumentException("`lower` must be less or equal to `upper` (lower=$myLower upper=$myUpper")
        }
    }

    fun lowerEndpoint(): T {
        return myLower
    }

    fun upperEndpoint(): T {
        return myUpper
    }

    operator fun contains(v: T): Boolean {
        return lse(myLower, v) && lse(v, myUpper)
    }

    fun span(other: ClosedRange<T>): ClosedRange<T> {
        if (encloses(other)) return this
        return if (other.encloses(this)) other else ClosedRange(min(myLower, other.myLower), max(myUpper, other.myUpper))
    }

    fun encloses(other: ClosedRange<T>): Boolean {
        return lse(myLower, other.myLower) && gte(myUpper, other.myUpper)
    }

    fun isConnected(other: ClosedRange<T>): Boolean {
        return !(gt(myLower, other.myUpper) || ls(myUpper, other.myLower))
    }

    fun intersection(other: ClosedRange<T>): ClosedRange<T> {
        if (!isConnected(other)) throw IllegalArgumentException("Ranges are not connected: this=$this other=$other")
        if (encloses(other)) return other
        return if (other.encloses(this)) this else ClosedRange(max(myLower, other.myLower), min(myUpper, other.myUpper))
    }

    override fun toString(): String {
        return "ClosedRange[$myLower, $myUpper]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ClosedRange<*>

        if (myLower != other.myLower) return false
        if (myUpper != other.myUpper) return false
        return true
    }

    override fun hashCode(): Int {
        return myLower.hashCode() + 31 * myUpper.hashCode()
    }

    companion object {
        fun <T : Comparable<T>> closed(lower: T, upper: T): ClosedRange<T> {
            return ClosedRange(lower, upper)
        }

        fun <T : Comparable<T>> singleton(v: T): ClosedRange<T> {
            return ClosedRange(v, v)
        }

        fun <T : Comparable<T>> encloseAll(values: Iterable<T?>): ClosedRange<T> {
            var iterated = false
            var min: T? = null
            var max: T? = null
            for (v: T? in values) {
                if (!iterated) {
                    iterated = true
                    min = v
                    max = v
                } else {
                    min = min(min!!, v!!)
                    max = max(max!!, v)
                }
            }
            if (!iterated) {
                throw NoSuchElementException()
            }
            return ClosedRange<T>(min!!, max!!)
        }
    }
}
