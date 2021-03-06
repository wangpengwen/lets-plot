/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.plot.builder.coord

import jetbrains.datalore.base.gcommon.collect.ClosedRange
import jetbrains.datalore.base.geometry.DoubleVector
import jetbrains.datalore.base.values.Pair

internal class CartesianCoordProvider : jetbrains.datalore.plot.builder.coord.CoordProviderBase() {

    override fun adjustDomains(xDomain: ClosedRange<Double>, yDomain: ClosedRange<Double>, displaySize: DoubleVector): Pair<ClosedRange<Double>, ClosedRange<Double>> {
        // unchanged
        return Pair(xDomain, yDomain)
    }
}
