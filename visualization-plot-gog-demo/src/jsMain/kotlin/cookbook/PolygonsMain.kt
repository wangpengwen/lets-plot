package jetbrains.datalore.visualization.gogDemo.cookbook

import jetbrains.datalore.visualization.gogDemo.DomDemoUtil
import jetbrains.datalore.visualization.gogDemo.model.cookbook.Polygons

fun main(args: Array<String>) {
    PolygonsMain().show()
}

class PolygonsMain : Polygons() {

    fun show() {
        val plotSpecList = listOf(
                basic()
        )

        DomDemoUtil.show(viewSize, plotSpecList as List<MutableMap<String, Any>>)
    }
}