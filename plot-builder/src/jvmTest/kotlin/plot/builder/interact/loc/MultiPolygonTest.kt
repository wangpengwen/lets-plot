package jetbrains.datalore.plot.builder.interact.loc

import jetbrains.datalore.base.geometry.DoubleRectangle
import jetbrains.datalore.base.geometry.DoubleVector
import jetbrains.datalore.plot.builder.interact.TestUtil.assertEmpty
import jetbrains.datalore.plot.builder.interact.TestUtil.assertObjects
import jetbrains.datalore.plot.builder.interact.TestUtil.between
import jetbrains.datalore.plot.builder.interact.TestUtil.createLocator
import jetbrains.datalore.plot.builder.interact.TestUtil.inside
import jetbrains.datalore.plot.builder.interact.TestUtil.outsideX
import jetbrains.datalore.plot.builder.interact.TestUtil.point
import jetbrains.datalore.plot.builder.interact.TestUtil.polygon
import jetbrains.datalore.plot.builder.interact.TestUtil.polygonTarget
import jetbrains.datalore.visualization.plot.base.interact.GeomTargetLocator
import kotlin.test.Test

class MultiPolygonTest {

    private val polygonLocator: GeomTargetLocator
        get() = createLocator(GeomTargetLocator.LookupStrategy.HOVER, GeomTargetLocator.LookupSpace.XY,
            jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.FIRST_TARGET
        )

    @Test
    fun pointInsideFirst_NotInHole_ShouldFindPolygon() {
        val locator = polygonLocator

        assertObjects(locator, between(
            jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.FIRST_POLYGON_RECT,
            jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.HOLE_RECT
        ), jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.FIRST_POLYGON_KEY
        )
    }

    @Test
    fun pointInsideFirst_InsideHole_ShouldFindNothing() {
        val locator = polygonLocator

        assertEmpty(locator, inside(jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.HOLE_RECT))
    }

    @Test
    fun pointInsideSecond_ShouldFindPolygon() {
        val locator = polygonLocator

        assertObjects(locator, inside(jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.SECOND_POLYGON_RECT),
            jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.FIRST_POLYGON_KEY
        )
    }

    @Test
    fun pointRightFromSecond_ShouldFindNothing() {
        val locator = polygonLocator

        assertEmpty(locator, outsideX(jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.SECOND_POLYGON_RECT))
    }

    companion object {

        /*
    200 *-------*
    150 |  *-*  |       *----*  150
        |  | |  |       |    |
     50 |  *-*  |       *----*  50
      0 *-------*
        0       200     400  500
  */

        private val HOLE_RECT = DoubleRectangle(50.0, 50.0, 100.0, 100.0)
        private val SECOND_POLYGON_RECT = DoubleRectangle(400.0, 150.0, 100.0, 100.0)
        private val FIRST_POLYGON_RECT = DoubleRectangle(0.0, 0.0, 200.0, 200.0)

        private val FIRST_POLYGON = jetbrains.datalore.plot.builder.interact.TestUtil.multipolygon(
            jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.polygonFromRect(jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.HOLE_RECT),
            jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.polygonFromRect(jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.SECOND_POLYGON_RECT),
            jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.polygonFromRect(jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.FIRST_POLYGON_RECT)
        )

        private const val FIRST_POLYGON_KEY = 1
        private val FIRST_TARGET = polygonTarget(
            jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.FIRST_POLYGON_KEY,
            jetbrains.datalore.plot.builder.interact.loc.MultiPolygonTest.Companion.FIRST_POLYGON
        )

        private fun polygonFromRect(rect: DoubleRectangle): MutableList<DoubleVector> {

            return polygon(
                    point(rect.left, rect.top),
                    point(rect.left, rect.bottom),
                    point(rect.right, rect.bottom),
                    point(rect.right, rect.top))
        }
    }

}
