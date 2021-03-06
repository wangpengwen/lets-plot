/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.plot.base.livemap

import jetbrains.datalore.plot.base.livemap.LivemapConstants.*

class LiveMapOptions(
    val zoom: Int?,
    val location: Any?,
    val stroke: Double?,
    val interactive: Boolean,
    val magnifier: Boolean,
    val displayMode: DisplayMode,
    val featureLevel: String?,
    val parent: Any?,
    val scaled: Boolean,
    val clustering: Boolean,
    val labels: Boolean,
    val theme: Theme,
    val projection: Projection,
    val geodesic: Boolean,
    val devParams: Map<*, *>
)