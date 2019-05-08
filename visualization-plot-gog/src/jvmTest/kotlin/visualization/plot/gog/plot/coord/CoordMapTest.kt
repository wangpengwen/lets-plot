package jetbrains.datalore.visualization.plot.gog.plot.coord

import jetbrains.datalore.base.geometry.DoubleRectangle
import jetbrains.datalore.base.geometry.DoubleVector
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class CoordMapTest : CoordTestBase() {

    @BeforeTest
    fun setUp() {
        dataBounds = DoubleRectangle(DoubleVector.ZERO, DATA_SPAN)
    }

    @Test
    fun adjustDomains() {
        // Coord Map keeps fixed ratio == 1 (equal X and Y)
        val dataBounds = dataBounds
        tryAdjustDomains(2.0, PROVIDER, dataBounds!!.xRange(), expand(dataBounds.yRange(), 2.0))
        tryAdjustDomains(0.5, PROVIDER, expand(dataBounds.xRange(), 2.0), dataBounds.yRange())
    }

    @Test
    fun applyScales() {
        // Map coord tries to keep grid square regardless of the display form factor
        run {
            val ratio = 2.0
            val shortSide = shortSideOfDisplay(ratio)
            tryApplyScales(ratio, PROVIDER,
                    DoubleVector(0.0, 0.0), DoubleVector(shortSide, shortSide), DoubleVector(0.0, 1.0E-2))
        }
        run {
            val ratio = 0.5
            val shortSide = shortSideOfDisplay(ratio)
            tryApplyScales(ratio, PROVIDER,
                    DoubleVector(0.0, 0.0), DoubleVector(shortSide, shortSide), DoubleVector(0.0, 1.0E-5))
        }
    }

    private fun shortSideOfDisplay(ratio: Double): Double {
        val displaySize = unitDisplaySize(ratio)
        return Math.min(displaySize.x, displaySize.y)
    }

    companion object {
        private val PROVIDER = CoordProviders.map()

        private val DATA_SPAN = DoubleVector(10.0, 10.0)
    }
}