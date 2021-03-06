/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

/* root package */

import jetbrains.datalore.base.event.MouseEventSpec
import jetbrains.datalore.base.event.dom.DomEventUtil
import jetbrains.datalore.base.geometry.DoubleRectangle
import jetbrains.datalore.base.geometry.DoubleVector
import jetbrains.datalore.base.geometry.Vector
import jetbrains.datalore.base.js.dom.DomEventType
import jetbrains.datalore.base.jsObject.dynamicObjectToMap
import jetbrains.datalore.base.observable.property.ValueProperty
import jetbrains.datalore.plot.builder.Plot
import jetbrains.datalore.plot.builder.PlotContainer
import jetbrains.datalore.plot.builder.assemble.PlotAssembler
import jetbrains.datalore.plot.config.*
import jetbrains.datalore.plot.livemap.LiveMapUtil
import jetbrains.datalore.plot.server.config.PlotConfigServerSide
import jetbrains.datalore.vis.canvas.dom.DomCanvasControl
import jetbrains.datalore.vis.svg.SvgNodeContainer
import jetbrains.datalore.vis.svgMapper.dom.SvgRootDocumentMapper
import mu.KotlinLogging
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLParagraphElement
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.svg.SVGSVGElement
import kotlin.dom.createElement


private val LOG = KotlinLogging.logger {}

private val DEF_PLOT_SIZE = DoubleVector(500.0, 400.0)
private val DEF_LIVE_MAP_SIZE = DoubleVector(800.0, 600.0)

private const val FIT_IN_WIDTH = false
private const val ASPECT_RATIO = 3.0 / 2.0   // TODO: theme

/**
 * The entry point to call in JS
 * `raw specs` are plot specs not processed by datalore plot backend
 */
@Suppress("unused")
@JsName("buildPlotFromRawSpecs")
fun buildPlotFromRawSpecs(plotSpecJs: dynamic, width: Double, height: Double, parentElement: HTMLElement) {
    try {
        val plotSpec = dynamicObjectToMap(plotSpecJs)
        PlotConfig.assertPlotSpecOrErrorMessage(plotSpec)
        val processedSpec = if (PlotConfig.isFailure(plotSpec)) {
            plotSpec
        } else {
            PlotConfigServerSide.processTransform(plotSpec)
        }
        buildPlotFromProcessedSpecsIntern(processedSpec, width, height, parentElement)
    } catch (e: RuntimeException) {
        handleException(e, parentElement)
    }
}

/**
 * The entry point to call in JS
 * `processed specs` are plot specs processed by datalore plot backend
 */
@Suppress("unused")
@JsName("buildPlotFromProcessedSpecs")
fun buildPlotFromProcessedSpecs(plotSpecJs: dynamic, width: Double, height: Double, parentElement: HTMLElement) {
    try {
        val plotSpec = dynamicObjectToMap(plotSpecJs)
        buildPlotFromProcessedSpecsIntern(plotSpec, width, height, parentElement)
    } catch (e: RuntimeException) {
        handleException(e, parentElement)
    }
}

private fun buildPlotFromProcessedSpecsIntern(
    plotSpec: MutableMap<String, Any>,
    width: Double,
    height: Double,
    parentElement: HTMLElement
) {
    throwTestingErrors()  // noop

    PlotConfig.assertPlotSpecOrErrorMessage(plotSpec)
    if (PlotConfig.isFailure(plotSpec)) {
        val errorMessage = PlotConfig.getErrorMessage(plotSpec)
        showError(errorMessage, parentElement)
        return
    }

    when {
        PlotConfig.isPlotSpec(plotSpec) -> buildSinglePlotFromProcessedSpecs(plotSpec, width, height, parentElement)
        PlotConfig.isGGBunchSpec(plotSpec) -> buildGGBunchFromProcessedSpecs(plotSpec, parentElement)
        else -> throw RuntimeException("Unexpected plot spec kind: " + PlotConfig.specKind(plotSpec))
    }
}

private fun buildGGBunchFromProcessedSpecs(bunchSpec: MutableMap<String, Any>, parentElement: HTMLElement) {
    val bunchConfig = BunchConfig(bunchSpec)
    if (bunchConfig.bunchItems.isEmpty()) return

    var bunchBounds = DoubleRectangle(DoubleVector.ZERO, DoubleVector.ZERO)
    for (bunchItem in bunchConfig.bunchItems) {
        val plotSpec = bunchItem.featureSpec as MutableMap<String, Any>
        val assembler = createPlotAssembler(plotSpec) { messages ->
            messages.forEach {
                showInfo(it, parentElement)
            }
        }

        val plotSize = if (bunchItem.hasSize()) {
            bunchItem.size
        } else {
            PlotConfigClientSideUtil.getPlotSizeOrNull(plotSpec) ?: DEF_PLOT_SIZE
        }

        val itemElement = parentElement.ownerDocument!!.createElement("div") {
            setAttribute(
                "style",
                "position: absolute; left: ${bunchItem.x}px; top: ${bunchItem.y}px;"
            )
        } as HTMLElement

        parentElement.appendChild(itemElement)
        buildPlotComponent(plotSpec, assembler, plotSize, itemElement)

        val itemBounds = DoubleRectangle(bunchItem.x, bunchItem.y, plotSize.x, plotSize.y)
        bunchBounds = bunchBounds.union(itemBounds)
    }

    parentElement.setAttribute(
        "style",
        "position: relative; width: ${bunchBounds.width}px; height: ${bunchBounds.height}px;"
    )
}

private fun buildSinglePlotFromProcessedSpecs(
    plotSpec: MutableMap<String, Any>,
    width: Double,
    height: Double,
    parentElement: HTMLElement
) {

    val assembler = createPlotAssembler(plotSpec) { messages ->
        messages.forEach {
            showInfo(it, parentElement)
        }
    }

    // Figure out plot size
    val plotSize = if (width > 0 && height > 0) {
        DoubleVector(width, height)
    } else {
        var plotSizeSpec = PlotConfigClientSideUtil.getPlotSizeOrNull(plotSpec)
        if (plotSizeSpec != null) {
            plotSizeSpec
        } else {
            val maxWidth = if (width > 0) width else parentElement.offsetWidth.toDouble()
            if (FIT_IN_WIDTH) {
                DoubleVector(maxWidth, maxWidth / ASPECT_RATIO)
            } else {
                val defaultSize = defaultPlotSize(assembler)
                if (defaultSize.x > maxWidth) {
                    val scaler = maxWidth / defaultSize.x
                    DoubleVector(maxWidth, defaultSize.y * scaler)
                } else {
                    defaultSize
                }
            }
        }
    }

    buildPlotComponent(plotSpec, assembler, plotSize, parentElement)
}

private fun buildPlotComponent(
    plotSpec: MutableMap<String, Any>,
    assembler: PlotAssembler,
    plotSize: DoubleVector,
    parentElement: HTMLElement
) {
    LiveMapOptionsParser.parseFromPlotOptions(OptionsAccessor(plotSpec))
        ?.let {
            LiveMapUtil.injectLiveMapProvider(
                assembler.layersByTile,
                it
            )
        }

    val plot = assembler.createPlot()
    val svg = buildPlotSvg(plot, plotSize, parentElement)
    parentElement.appendChild(svg)
}

private fun createPlotAssembler(
    plotSpec: MutableMap<String, Any>,
    computationMessagesHandler: ((List<String>) -> Unit)?
): PlotAssembler {
    @Suppress("NAME_SHADOWING")
    var plotSpec = plotSpec
    plotSpec = PlotConfigClientSide.processTransform(plotSpec)
    if (computationMessagesHandler != null) {
        val computationMessages = PlotConfigUtil.findComputationMessages(plotSpec)
        if (computationMessages.isNotEmpty()) {
            computationMessagesHandler(computationMessages)
        }
    }

    return PlotConfigClientSideUtil.createPlotAssembler(plotSpec)
}

private fun defaultPlotSize(assembler: PlotAssembler): DoubleVector {
    var plotSize = DEF_PLOT_SIZE
    val facets = assembler.facets
    if (facets.isDefined) {
        val xLevels = facets.xLevels!!
        val yLevels = facets.yLevels!!
        val columns = if (xLevels.isEmpty()) 1 else xLevels.size
        val rows = if (yLevels.isEmpty()) 1 else yLevels.size
        val panelWidth = DEF_PLOT_SIZE.x * (0.5 + 0.5 / columns)
        val panelHeight = DEF_PLOT_SIZE.y * (0.5 + 0.5 / rows)
        plotSize = DoubleVector(panelWidth * columns, panelHeight * rows)
    } else if (assembler.containsLiveMap) {
        plotSize = DEF_LIVE_MAP_SIZE
    }
    return plotSize
}

private fun buildPlotSvg(
    plot: Plot,
    plotSize: DoubleVector,
    eventTarget: Node
): SVGSVGElement {

    val plotContainer = PlotContainer(plot, ValueProperty(plotSize))

    eventTarget.addEventListener(DomEventType.MOUSE_MOVE.name, { e: Event ->
        plotContainer.mouseEventPeer.dispatch(
            MouseEventSpec.MOUSE_MOVED,
            DomEventUtil.translateInTargetCoord(e as MouseEvent)
        )
    })

    eventTarget.addEventListener(DomEventType.MOUSE_LEAVE.name, { e: Event ->
        plotContainer.mouseEventPeer.dispatch(
            MouseEventSpec.MOUSE_LEFT,
            DomEventUtil.translateInTargetCoord(e as MouseEvent)
        )
    })

    plotContainer.ensureContentBuilt()

    plotContainer.liveMapFigures.forEach { liveMapFigure ->
        val canvasControl =
            DomCanvasControl(liveMapFigure.dimension().get().toVector())
        liveMapFigure.mapToCanvas(canvasControl)
        eventTarget.appendChild(canvasControl.rootElement)
    }

    val svgRoot = plotContainer.svg
    val mapper = SvgRootDocumentMapper(svgRoot)
    SvgNodeContainer(svgRoot)
    mapper.attachRoot()
    return mapper.target
}

private fun DoubleVector.toVector(): Vector {
    return Vector(x.toInt(), y.toInt())
}

private fun handleException(e: RuntimeException, parentElement: HTMLElement) {
    val failureInfo = FailureHandler.failureInfo(e)
    showError(failureInfo.message, parentElement)
    if (failureInfo.isInternalError) {
        LOG.error(e) {}
    }
}

private fun showError(message: String, parentElement: HTMLElement) {
    showText(message, "color:darkred;", parentElement)
}

private fun showInfo(message: String, parentElement: HTMLElement) {
    showText(message, "color:darkblue;", parentElement)
}

private fun showText(message: String, style: String, parentElement: HTMLElement) {
    val paragraphElement = parentElement.ownerDocument!!.createElement("p") as HTMLParagraphElement
    if (style.isNotBlank()) {
        paragraphElement.setAttribute("style", style)
    }
    paragraphElement.textContent = message
    parentElement.appendChild(paragraphElement)
}

private fun throwTestingErrors() {
    // testing errors
//        throw RuntimeException()
//        throw RuntimeException("My sudden crush")
//        throw IllegalArgumentException("User configuration error")
//        throw IllegalStateException("User configuration error")
//        throw IllegalStateException()   // Huh?
}
