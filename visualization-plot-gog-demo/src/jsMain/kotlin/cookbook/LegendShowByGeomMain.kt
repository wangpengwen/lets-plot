package jetbrains.datalore.visualization.gogDemo.cookbook

import jetbrains.datalore.visualization.gogDemo.DomDemoUtil
import jetbrains.datalore.visualization.gogDemo.model.cookbook.LegendShowByGeom

//fun main(args: Array<String>) {
//    LegendShowByGeomMain().show()
//}

class LegendShowByGeomMain : LegendShowByGeom() {

    fun show() {
        val plotSpecList = listOf(
                defaultLegend(),
                noLinesLegend(),
                noBothLegends()
        )

        DomDemoUtil.show(viewSize, plotSpecList as List<MutableMap<String, Any>>)
    }
}