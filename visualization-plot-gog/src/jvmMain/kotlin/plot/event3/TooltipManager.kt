package jetbrains.datalore.visualization.plot.gog.plot.event3

import jetbrains.datalore.base.geometry.DoubleVector
import jetbrains.datalore.base.values.Color
import jetbrains.datalore.visualization.plot.gog.plot.event3.tooltip.TooltipOrientation

interface TooltipManager {

    fun measure(text: List<String>, fontSize: Double): DoubleVector

    fun add(tooltipEntry: TooltipEntry)

    fun beginUpdate()

    fun endUpdate()

    class TooltipContent(val text: List<String>, val fill: Color, val fontSize: Double) {

        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false
            val that = o as TooltipContent?
            return java.lang.Double.compare(that!!.fontSize, fontSize) == 0 &&
                    fill == that.fill &&
                    text == that.text
        }

        override fun hashCode(): Int {
            return listOf(fill, text, fontSize).hashCode()
        }
    }

    class TooltipEntry(val tooltipContent: TooltipContent, val tooltipCoord: DoubleVector, val stemCoord: DoubleVector, val orientation: TooltipOrientation) {

        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false
            val that = o as TooltipEntry?
            return tooltipContent == that!!.tooltipContent &&
                    tooltipCoord == that.tooltipCoord &&
                    stemCoord == that.stemCoord &&
                    orientation === that.orientation
        }

        override fun hashCode(): Int {
            return listOf(tooltipContent, tooltipCoord, stemCoord, orientation).hashCode()
        }
    }
}