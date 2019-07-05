package jetbrains.datalore.visualization.gogDemo.cookbook

import jetbrains.datalore.visualization.gogDemo.DomDemoUtil
import jetbrains.datalore.visualization.gogDemo.model.cookbook.BoxPlot

//fun main(args: Array<String>) {
//    BoxPlotMain().show()
//}

class BoxPlotMain : BoxPlot() {

    fun show() {
        val plotSpecList = listOf(
                basic(),
                withVarWidth(),
                withCondColored(),
                withOutlierOverride(),
                withGrouping(),
                withGroupingAndVarWidth()
        )

        DomDemoUtil.show(viewSize, plotSpecList as List<MutableMap<String, Any>>)
    }
}