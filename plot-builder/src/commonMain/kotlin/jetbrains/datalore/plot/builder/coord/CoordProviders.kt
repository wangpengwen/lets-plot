/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.plot.builder.coord

object CoordProviders {
    fun cartesian(): jetbrains.datalore.plot.builder.coord.CoordProvider {
        return jetbrains.datalore.plot.builder.coord.CartesianCoordProvider()
    }

    fun fixed(ratio: Double): jetbrains.datalore.plot.builder.coord.CoordProvider {
        return jetbrains.datalore.plot.builder.coord.FixedRatioCoordProvider(ratio)
    }

    fun map(): jetbrains.datalore.plot.builder.coord.CoordProvider {
        // Mercator projection is cylindrical thus we don't really need 'projection X'
        return jetbrains.datalore.plot.builder.coord.ProjectionCoordProvider.Companion.withProjectionY(jetbrains.datalore.plot.builder.coord.map.MercatorProjectionY())
    }
}
