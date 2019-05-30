package jetbrains.datalore.visualization.gogDemo.cookbook

import jetbrains.datalore.visualization.gogDemo.DomDemoUtil
import jetbrains.datalore.visualization.gogDemo.model.cookbook.ABLine

fun main(args: Array<String>) {
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


        println("FFFFFFFFFFFFFFf")
        console.log(plotSpecList)
        println("FFFFFFFFFFFFFFf")

        DomDemoUtil.show(viewSize, plotSpecList as List<MutableMap<String, Any>>)
    }
}