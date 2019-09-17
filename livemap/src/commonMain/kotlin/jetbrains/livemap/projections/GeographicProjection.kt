package jetbrains.livemap.projections

import jetbrains.datalore.base.projectionGeometry.GeoUtils.limitLat
import jetbrains.datalore.base.projectionGeometry.GeoUtils.limitLon
import jetbrains.datalore.base.projectionGeometry.LonLat
import jetbrains.datalore.base.projectionGeometry.Rect
import jetbrains.datalore.base.projectionGeometry.explicitVec
import jetbrains.datalore.base.projectionGeometry.newSpanRectangle

internal class GeographicProjection : GeoProjection {

    override fun project(v: LonLatPoint): GeographicPoint = explicitVec(limitLon(v.x), limitLat(v.y))

    override fun invert(v: GeographicPoint): LonLatPoint = explicitVec(limitLon(v.x), limitLat(v.y))
    
    override fun validRect(): Rect<LonLat> = VALID_RECTANGLE

    companion object {
        private val VALID_RECTANGLE = newSpanRectangle(
            explicitVec<LonLat>(-180.0, -90.0),
            explicitVec<LonLat>(+180.0, +90.0)
        )
    }
}