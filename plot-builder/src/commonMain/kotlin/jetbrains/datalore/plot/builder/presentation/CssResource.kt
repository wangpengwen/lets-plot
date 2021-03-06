/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.plot.builder.presentation

class CssResource(private val selectorMap: Map<Selector, Map<StyleType, Any>>) {
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
}

class Selector(private val selectors: List<Any>, private val innerSelector: Selector? = null) {
    override fun toString(): String {
        val sb = StringBuilder()

        for (selector in selectors) {
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
}