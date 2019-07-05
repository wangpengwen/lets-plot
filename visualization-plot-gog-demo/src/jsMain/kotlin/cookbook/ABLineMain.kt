package jetbrains.datalore.visualization.gogDemo.cookbook

import jetbrains.datalore.visualization.gogDemo.DomDemoUtil
import jetbrains.datalore.visualization.gogDemo.model.cookbook.ABLine

fun mainn(args: Array<String>) {
    ABLineMain().show()
}

class ABLineMain : ABLine() {

    fun show() {
        val plotSpecList = listOf(
                lineDefaultAlone(),
                lineDefault(),
                negativeSlope(),
                zeroSlope(),
                variableInterceptAndSlope()
        )

        DomDemoUtil.show(viewSize, plotSpecList as List<MutableMap<String, Any>>)
    }
}