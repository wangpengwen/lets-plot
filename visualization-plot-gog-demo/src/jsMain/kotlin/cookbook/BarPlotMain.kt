package jetbrains.datalore.visualization.gogDemo.cookbook

import jetbrains.datalore.visualization.gogDemo.DomDemoUtil
import jetbrains.datalore.visualization.gogDemo.model.cookbook.BarPlot
import jetbrains.datalore.visualization.gogDemo.model.cookbook.BoxPlot

//fun main(args: Array<String>) {
//    BarPlotMain().show()
//}

class BarPlotMain : BarPlot() {

    fun show() {
        val plotSpecList = listOf(
                basic(),
                fancy()
        )

        DomDemoUtil.show(viewSize, plotSpecList as List<MutableMap<String, Any>>)
    }
}