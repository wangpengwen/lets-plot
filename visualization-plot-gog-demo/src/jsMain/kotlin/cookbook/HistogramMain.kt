package jetbrains.datalore.visualization.gogDemo.cookbook

import jetbrains.datalore.visualization.gogDemo.DomDemoUtil
import jetbrains.datalore.visualization.gogDemo.model.cookbook.Histogram

//fun main(args: Array<String>) {
//    HistogramMain().show()
//}

class HistogramMain : Histogram() {

    fun show() {
        val plotSpecList = listOf(
                basic(),
                withWeights(),
                withConstantWeight()
        )

        DomDemoUtil.show(viewSize, plotSpecList as List<MutableMap<String, Any>>)
    }
}