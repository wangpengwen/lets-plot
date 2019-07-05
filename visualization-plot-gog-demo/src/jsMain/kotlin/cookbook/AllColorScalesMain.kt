package jetbrains.datalore.visualization.gogDemo.cookbook

import jetbrains.datalore.visualization.gogDemo.DomDemoUtil
import jetbrains.datalore.visualization.gogDemo.model.cookbook.AllColorScales

//fun main(args: Array<String>) {
//    AllColorScalesMain().show()
//}

class AllColorScalesMain : AllColorScales() {

    fun show() {
        DomDemoUtil.show(viewSize, bundle() as List<MutableMap<String, Any>>)
    }
}