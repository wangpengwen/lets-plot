/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.plot

import jetbrains.datalore.base.json.JsonSupport

fun parsePlotSpec(spec: String) = JsonSupport.parseJson(spec) as MutableMap<String, Any>
