package jetbrains.datalore.base.numberFormat

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EdgeCases {

    @Test
    fun exponentCloseToMaxDecimals_AutoDecimals() {
        assertEquals("2e-17", NumberFormat("e").apply(2.0E-17)) // 17 decimals
        assertEquals("2e-18", NumberFormat("e").apply(2.0E-18)) // 18 decimals
        assertEquals("2e-19", NumberFormat("e").apply(2.0E-19)) // 19 decimals

        assertEquals("2e-17", NumberFormat("g").apply(2.0E-17))
        assertEquals("2e-18", NumberFormat("g").apply(2.0E-18))
        assertEquals("2e-19", NumberFormat("g").apply(2.0E-19))

        assertEquals("0.012345678901234568", NumberFormat("g").apply(0.012_345_678_901_234_567_89))
    }

    @Test
    fun exponentCloseToMaxDecimals_FixedDecimals() {
        assertEquals("2.000000000000000000e-17", NumberFormat(".18e").apply(2.0E-17)) // 17 decimals
        assertEquals("2.000000000000000000e-18", NumberFormat(".18e").apply(2.0E-18)) // 18 decimals
        assertEquals("2.000000000000000000e-19", NumberFormat(".18e").apply(2.0E-19)) // 19 decimals

        assertEquals("2e-17", NumberFormat(".18g").apply(2.0E-17))
        assertEquals("2e-18", NumberFormat(".18g").apply(2.0E-18))
        assertEquals("2e-19", NumberFormat(".18g").apply(2.0E-19))

        assertEquals("0.0123456789012345684", NumberFormat(".18g").apply(0.012_345_678_901_234_567_89))
    }

    @Test
    fun maxValues() {
        assertEquals("", NumberFormat("g").apply(Long.MAX_VALUE))
        assertEquals("", NumberFormat("e").apply(Long.MAX_VALUE))
        assertEquals("", NumberFormat("f").apply(Long.MAX_VALUE))

        assertEquals("", NumberFormat("g").apply(Double.MAX_VALUE))
        assertEquals("", NumberFormat("e").apply(Double.MAX_VALUE))
        assertEquals("", NumberFormat("f").apply(Double.MAX_VALUE))
    }

    @Test
    fun minValues() {
        assertEquals("", NumberFormat("g").apply(Long.MIN_VALUE))
        assertEquals("", NumberFormat("e").apply(Long.MIN_VALUE))
        assertEquals("", NumberFormat("f").apply(Long.MIN_VALUE))

        assertEquals("", NumberFormat("g").apply(Double.MIN_VALUE))
        assertEquals("", NumberFormat("e").apply(Double.MIN_VALUE))
        assertEquals("", NumberFormat("f").apply(Double.MIN_VALUE))
    }

    @Test
    fun throwsExceptions() {
        val assertThrows = {format: NumberFormat, number: Number ->
            var wasThrown: Boolean = false
            try {
                format.apply(number)
            } catch (e: Throwable) {
                wasThrown = true
            } finally {
                assertTrue(wasThrown)
            }
        }

        listOf("g", "e", "f").forEach {
            with(NumberFormat(it)) {
                assertThrows(this, Double.NaN)
                assertThrows(this, Double.NEGATIVE_INFINITY)
                assertThrows(this, Double.POSITIVE_INFINITY)
            }
        }
    }

    @Test
    fun negativeZero() {
        assertEquals("-0", NumberFormat("g").apply(-0.0))
        assertEquals("-0.0000", NumberFormat("f").apply(-0.0))
        assertEquals("-0.0000e+0", NumberFormat("e").apply(-0.0))
    }

    @Test
    fun zero() {
        assertEquals("0", NumberFormat(".18g").apply(0.0))
        assertEquals("0", NumberFormat("g").apply(0.0))
    }
}
