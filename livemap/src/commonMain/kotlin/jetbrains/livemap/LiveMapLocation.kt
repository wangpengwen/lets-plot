package jetbrains.livemap

import jetbrains.datalore.base.geometry.DoubleRectangle
import jetbrains.datalore.base.projectionGeometry.*
import jetbrains.datalore.base.projectionGeometry.GeoUtils.FULL_LONGITUDE
import jetbrains.livemap.projections.*
import kotlin.math.pow
import kotlin.math.round

class LiveMapLocation(private val myViewProjection: ViewProjection, private val myMapProjection: MapProjection) {

    val viewLonLatRect: DoubleRectangle
        get() {
            val viewRect = myViewProjection.viewRect

            val nw = worldToLonLat(viewRect.origin)
            val se = worldToLonLat(viewRect.origin + viewRect.dimension)

            return DoubleRectangle(nw.x, se.y, se.x - nw.x, nw.y - se.y)
        }

    private fun worldToLonLat(worldCoord: WorldPoint): LonLatPoint {
        val coord: Vec<World>
        val shift: LonLatPoint
        val worldSize = myMapProjection.mapRect.dimension

        if (worldCoord.x > worldSize.x) {
            shift = explicitVec<LonLat>(FULL_LONGITUDE, 0.0)
            coord = worldCoord.transform(fx = { it - worldSize.scalarX })
        } else if (worldCoord.x < 0) {
            shift = explicitVec<LonLat>(-FULL_LONGITUDE, 0.0)
            coord = worldSize.transform(fx = { it + worldSize.scalarX })
        } else {
            shift = explicitVec<LonLat>(0.0, 0.0)
            coord = worldCoord
        }

        return shift + myMapProjection.invert(coord)
    }

    companion object {

        fun getLocationString(viewRect: DoubleRectangle): String {
            val delta = viewRect.dimension.mul(0.05)

            return ("location = ["
                    + (viewRect.left + delta.x).round(6) + ", "
                    + (viewRect.top + delta.y).round(6) + ", "
                    + (viewRect.right - delta.x).round(6) + ", "
                    + (viewRect.bottom - delta.y).round(6) + "]")
        }

        private fun Double.round(digits: Int): Double {
            val a = 10.0.pow(digits)

            return round(this * a) / a
        }
    }
}