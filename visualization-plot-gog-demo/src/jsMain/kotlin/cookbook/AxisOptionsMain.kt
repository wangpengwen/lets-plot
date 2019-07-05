package jetbrains.datalore.visualization.gogDemo.cookbook

import jetbrains.datalore.visualization.gogDemo.DomDemoUtil
import jetbrains.datalore.visualization.gogDemo.model.cookbook.AxisOptions

//fun main(args: Array<String>) {
//    AxisOptionsMain().show()
//}

class AxisOptionsMain : AxisOptions() {

    fun show() {
        val plotSpecList = listOf(
                defaultAxis(),
                noXTitle(),
                noYTitle(),
                noXTickLabels(),
                noYTickLabels(),
                noTickMarks(),
                noTickMarksOrLabels(),
                noTitlesOrLabels(),
                onlyLines(),
                noLinesOrTitles()
        )

        DomDemoUtil.show(viewSize, plotSpecList as List<MutableMap<String, Any>>)
    }
}