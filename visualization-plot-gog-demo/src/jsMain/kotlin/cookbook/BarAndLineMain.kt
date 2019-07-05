package jetbrains.datalore.visualization.gogDemo.cookbook

import jetbrains.datalore.visualization.gogDemo.DomDemoUtil
import jetbrains.datalore.visualization.gogDemo.model.cookbook.BarAndLine

//fun main(args: Array<String>) {
//    BarAndLineMain().show()
//}

class BarAndLineMain : BarAndLine() {

    fun show() {
        val plotSpecList = listOf(
                defaultBarDiscreteX(),
                barDiscreteXFill(),
                barDiscreteXFillMappedInGeom(),
                barDiscreteXFillAndBlackOutline(),
                barDiscreteXTitleAxisLabelsNarrowWidth()
        )

        DomDemoUtil.show(viewSize, plotSpecList as List<MutableMap<String, Any>>)
    }
}