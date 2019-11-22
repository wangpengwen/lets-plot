/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.plotDemo.model.plotConfig

import jetbrains.datalore.plot.parsePlotSpec
import jetbrains.datalore.plotDemo.model.PlotConfigDemoBase

@Suppress("DuplicatedCode")
class ScaleSize : PlotConfigDemoBase() {
    fun plotSpecList(): List<Map<String, Any>> {
        return listOf(
            defaultRange(),
            customRange(),
            areaScale(),
            areaScaleWithMaxSize(),
            areaScaleWithMaxSizeAndNoZeroVal()
        )
    }

    private fun defaultRange(): Map<String, Any> {
        val spec = """
            |{
            |   'kind': 'plot',
            |   'data': {'x': [1, 2, 3],
            |          'y': [1, 2, 3],
            |          's': [0, 1, 2]},
            |   'scales': [
            |       {
            |           'aesthetic': 'size'
            |       }
            |   ],
            |   'layers': [
            |       {
            |           'geom': 'point',
            |           'mapping': {'x': 'x', 'y': 'y', 'size': 's'}
            |       }
            |   ]
            |}
            """.trimMargin()
        return parsePlotSpec(spec)
    }

    private fun customRange(): Map<String, Any> {
        val spec = """
            |{
            |   'kind': 'plot',
            |   'data': {'x': [1, 2, 3],
            |          'y': [1, 2, 3],
            |          's': [0, 1, 2]},
            |   'scales': [
            |       {
            |           'aesthetic': 'size',
            |           'range': [10, 30]
            |       }
            |   ],
            |   'layers': [
            |       {
            |           'geom': 'point',
            |           'mapping': {'x': 'x', 'y': 'y', 'size': 's'}
            |       }
            |   ]
            |}
            """.trimMargin()
        return parsePlotSpec(spec)
    }

    private fun areaScale(): Map<String, Any> {
        val spec = """
            |{
            |   'kind': 'plot',
            |   'data': {'x': [1, 2, 3],
            |          'y': [1, 2, 3],
            |          's': [0, 1, 2]},
            |   'scales': [
            |       {
            |           'aesthetic': 'size',
            |           'scale_mapper_kind': 'size_area'
            |       }
            |   ],
            |   'layers': [
            |       {
            |           'geom': 'point',
            |           'mapping': {'x': 'x', 'y': 'y', 'size': 's'}
            |       }
            |   ]
            |}
            """.trimMargin()
        return parsePlotSpec(spec)
    }

    private fun areaScaleWithMaxSize(): Map<String, Any> {
        val spec = """
            |{
            |   'kind': 'plot',
            |   'data': {'x': [1, 2, 3],
            |          'y': [1, 2, 3],
            |          's': [0, 1, 2]},
            |   'scales': [
            |       {
            |           'aesthetic': 'size',
            |           'scale_mapper_kind': 'size_area',
            |           'max_size': 30
            |       }
            |   ],
            |   'layers': [
            |       {
            |           'geom': 'point',
            |           'mapping': {'x': 'x', 'y': 'y', 'size': 's'}
            |       }
            |   ]
            |}
            """.trimMargin()
        return parsePlotSpec(spec)
    }


    private fun areaScaleWithMaxSizeAndNoZeroVal(): Map<String, Any> {
        val spec = """
            |{
            |   'kind': 'plot',
            |   'data': {'x': [1, 2, 3],
            |          'y': [1, 2, 3],
            |          's': [1, 2, 3]},
            |   'scales': [
            |       {
            |           'aesthetic': 'size',
            |           'scale_mapper_kind': 'size_area',
            |           'max_size': 30
            |       }
            |   ],
            |   'layers': [
            |       {
            |           'geom': 'point',
            |           'mapping': {'x': 'x', 'y': 'y', 'size': 's'}
            |       }
            |   ]
            |}
            """.trimMargin()
        return parsePlotSpec(spec)
    }
}