package jetbrains.datalore.visualization.gogDemo

import jetbrains.datalore.base.domCore.dom.DomHTMLElement
import jetbrains.datalore.base.geometry.DoubleVector
import jetbrains.datalore.base.geometry.Vector
import jetbrains.datalore.visualization.base.canvasDom.DomCanvasControl
import jetbrains.datalore.visualization.plot.gog.DemoAndTest
import kotlin.browser.document

object DomDemoUtil {
    fun show(viewSize: DoubleVector, plotSpecList: List<MutableMap<String, Any>>) {
        val container = document.getElementById("root")
        for (plotSpec in plotSpecList) {
            val component = createComponent(viewSize, plotSpec)

            container!!.appendChild(component)
        }
    }

    private fun createComponent(viewSize: DoubleVector, plotSpec: MutableMap<String, Any>): DomHTMLElement {
        val plot = DemoAndTest.createPlot(plotSpec, false)
        val canvasControl = DomCanvasControl(Vector(viewSize.x.toInt(), viewSize.y.toInt()))

        PlotCanvasMapper(plot, canvasControl)

        return canvasControl.rootElement
    }
}