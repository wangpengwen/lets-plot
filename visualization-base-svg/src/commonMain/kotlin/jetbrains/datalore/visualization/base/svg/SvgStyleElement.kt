package jetbrains.datalore.visualization.base.svg

import jetbrains.datalore.visualization.base.svg.css.CssResource

class SvgStyleElement(resource: SvgCssResource) : SvgElement() {

    override val elementName = "style"
    val cssResource: CssResource = resource.css()

    init {
        setAttribute("type", "text/css")
    }
}