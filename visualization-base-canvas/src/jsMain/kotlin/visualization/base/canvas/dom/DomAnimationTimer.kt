package jetbrains.datalore.visualization.base.canvasDom

import jetbrains.datalore.base.domCore.dom.DomElement
import jetbrains.datalore.base.domCore.dom.DomWindow
import jetbrains.datalore.visualization.base.canvas.CanvasControl.AnimationTimer
import kotlin.browser.window

internal abstract class DomAnimationTimer(private val myElement: DomElement) : AnimationTimer {
    private var myHandle: Int? = null
    private var myIsStarted: Boolean = false

    init {
        myIsStarted = false
    }

    internal abstract fun handle(millisTime: Long)

    override fun start() {
        if (myIsStarted) {
            return
        }

        myIsStarted = true
        requestNextFrame()
    }

    override fun stop() {
        if (!myIsStarted) {
            return
        }

        myIsStarted = false
        window.cancelAnimationFrame(myHandle!!)
    }

    fun execute(millisTime: Double) {
        if (!myIsStarted) {
            return
        }

        handle(millisTime.toLong())

        requestNextFrame()
    }

    private fun requestNextFrame() {
        myHandle = window.requestAnimationFrame { this.execute(it) }
    }
}
