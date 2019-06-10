package jetbrains.datalore.visualization.base.svg.css

class CssResource(map: Map<Selector, Map<StyleType, Any>>) {

    private val selectorMap : Map<Selector, Map<StyleType, Any>>

    init {
        this.selectorMap = map.toList().sortedBy { (key, value) -> key.selectorValues.count() }.toMap()
    }

    override fun toString(): String {
        val sb = StringBuilder()

        for (selector in selectorMap.keys) {
            sb.append(selector.toString()).append(" {\n")
            sb.append(styleToString(selectorMap.getValue(selector)))
            sb.append("}\n")
        }
        return sb.toString()
    }

    private fun styleToString(styleMap: Map<StyleType, Any>): String {
        val sb = StringBuilder()

        for (styleType in styleMap.keys) {
            sb.append("\t")
            sb.append(styleType.str).append(": ").append(styleMap[styleType].toString()).append(";\n")
        }

        return sb.toString()
    }

    fun getStylesForSelector(selector: Selector?) : Map<StyleType, Any> {
        val selectorMapForCurrentSelector = mutableMapOf<StyleType, Any>()

        if (selector == null)
            return selectorMapForCurrentSelector

        for (selectorAndStyle in selectorMap) {
            if (selector.selectorEquals(selectorAndStyle.key)) {
                for (style in selectorAndStyle.value) {
                    selectorMapForCurrentSelector[style.key] = style.value
                }
            }
        }

        return selectorMapForCurrentSelector
    }
}

class Selector(val selectorValues: List<Any>, val innerSelector: Selector? = null) {
    override fun toString(): String {
        val sb = StringBuilder()

        for (selector in selectorValues) {
            if (selector is String) {
                sb.append(".$selector")
            }
            else if (selector is SelectorType) {
                sb.append(selector.str)
            }
        }

        if (innerSelector != null) {
            sb.append(" ").append(innerSelector.toString())
        }

        return sb.toString()
    }

    fun selectorEquals(selector: Selector): Boolean {
        var isEqualWithCurrent = false
        if (selectorValues.containsAll(selector.selectorValues)) {
            isEqualWithCurrent = true
        }

        if (innerSelector == null && selector.innerSelector == null) return isEqualWithCurrent

        if (innerSelector != null) {
            if (isEqualWithCurrent && selector.innerSelector != null) {
                return innerSelector.selectorEquals(selector.innerSelector)
            }
            return innerSelector.selectorEquals(selector)
        }

        return isEqualWithCurrent
    }
}