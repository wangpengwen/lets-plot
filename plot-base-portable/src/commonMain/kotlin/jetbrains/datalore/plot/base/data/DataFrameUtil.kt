/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.datalore.plot.base.data

import jetbrains.datalore.base.function.Predicate
import jetbrains.datalore.base.gcommon.base.Preconditions.checkArgument
import jetbrains.datalore.base.gcommon.collect.Ordering
import jetbrains.datalore.plot.base.Aes
import jetbrains.datalore.plot.base.DataFrame
import jetbrains.datalore.plot.base.Scale
import jetbrains.datalore.plot.base.scale.ScaleUtil
import jetbrains.datalore.plot.base.stat.Stats
import kotlin.jvm.JvmOverloads

object DataFrameUtil {
    fun transformVarFor(aes: Aes<*>): DataFrame.Variable {
        return TransformVar.forAes(aes)
    }

    fun applyTransform(data: DataFrame, `var`: DataFrame.Variable, aes: Aes<*>, scale: Scale<*>): DataFrame {
        val transformVar = transformVarFor(aes)
        return applyTransform(data, `var`, transformVar, scale)
    }

    private fun applyTransform(data: DataFrame, variable: DataFrame.Variable, transformVar: DataFrame.Variable, scale: Scale<*>): DataFrame {
        val transformSource = getTransformSource(data, variable, scale)
        val transformResult = ScaleUtil.transform(transformSource, scale)
        return data.builder()
                .putNumeric(transformVar, transformResult)
                .build()
    }

    private fun getTransformSource(data: DataFrame, variable: DataFrame.Variable, scale: Scale<*>): List<*> {
        if (!scale.hasDomainLimits()) {
            return data[variable]
        }

        if (scale.isContinuousDomain) {
            val limits = scale.domainLimits
            return filterTransformSource(data.getNumeric(variable)) { input: Double? ->
                // keep null(s)
                input == null || limits.contains(input)   // faster then 'scale.isInDomainLimits(Object v)'
            }
        }

        // discrete domain
        return filterTransformSource(data[variable]) { input: Any? ->
            // keep null(s)
            input == null || scale.isInDomainLimits(input)
        }
    }

    private fun <T> filterTransformSource(rawData: List<T>, retain: Predicate<T>): List<T?> {
        val result = ArrayList<T?>(rawData.size)
        for (v in rawData) {
            if (retain(v)) {
                result.add(v)
            } else {
                // drop this value
                result.add(null)
            }
        }
        return result
    }

    fun hasVariable(data: DataFrame, varName: String): Boolean {
        for (`var` in data.variables()) {
            if (varName == `var`.name) {
                return true
            }
        }
        return false
    }

    fun findVariableOrFail(data: DataFrame, varName: String): DataFrame.Variable {
        for (`var` in data.variables()) {
            if (varName == `var`.name) {
                return `var`
            }
        }
        throw IllegalArgumentException("Variable not found: '$varName'")
    }

    fun isNumeric(data: DataFrame, varName: String): Boolean {
        return data.isNumeric(findVariableOrFail(data, varName))
    }

    /**
     * ToDo: Cache in DataFrame (similar to 'factor')
     */
    fun distinctValues(data: DataFrame, variable: DataFrame.Variable): Collection<Any?> {
        return LinkedHashSet(data[variable])
    }

    fun hasValues(data: DataFrame, `var`: DataFrame.Variable): Boolean {
        return data.has(`var`) && !data[`var`].isEmpty()
    }

    fun valuesOrNull(data: DataFrame, `var`: DataFrame.Variable): List<*>? {
        return if (data.has(`var`)) {
            data[`var`]
        } else null
    }

    fun sortedCopy(variables: Iterable<DataFrame.Variable>): List<DataFrame.Variable> {
        val ordering = Ordering.from(Comparator<DataFrame.Variable> { o1, o2 -> o1.name.compareTo(o2.name) })
        return ordering.sortedCopy(variables)
    }

    fun variables(df: DataFrame): Map<String, DataFrame.Variable> {
        val vars = HashMap<String, DataFrame.Variable>()
        for (`var` in df.variables()) {
            vars[`var`.name] = `var`
        }
        return vars
    }

    fun appendReplace(df0: DataFrame, df1: DataFrame): DataFrame {
        val df0Vars = variables(df0)

        val builder = df0.builder()
        for (df1Var in df1.variables()) {
            var resultVar = df1Var
            if (df0Vars.containsKey(df1Var.name)) {
                val df0Var = df0Vars[df1Var.name]!!
                builder.remove(df0Var)
                resultVar = df0Var
            }
            builder.put(resultVar, df1[df1Var])
        }
        return builder.build()
    }

    fun toMap(df: DataFrame): Map<String, List<*>> {
        val result = HashMap<String, List<*>>()
        val variables = df.variables()
        for (`var` in variables) {
            result[`var`.name] = df[`var`]
        }
        return result
    }

    fun fromMap(map: Map<*, *>): DataFrame {
        val frameBuilder = DataFrame.Builder()
        for ((key, value) in map) {
            checkArgument(key is String, "Map to data-frame: key expected a String but was " + key!!::class.simpleName + " : " + key)
            checkArgument(key is String, "Map to data-frame: value expected a List but was " + value!!::class.simpleName + " : " + value)
            frameBuilder.put(createVariable(key as String), value as List<*>)
        }
        return frameBuilder.build()
    }

    @JvmOverloads
    fun createVariable(name: String, label: String = name): DataFrame.Variable {
        val variable: DataFrame.Variable
        when {
            TransformVar.isTransformVar(name) -> variable = TransformVar[name]
            Stats.isStatVar(name) -> variable = Stats.statVar(name)
            Dummies.isDummyVar(name) -> return Dummies.newDummy(
                name
            )
            else -> variable = DataFrame.Variable(name, DataFrame.Variable.Source.ORIGIN, label)
        }
        return variable
    }

    fun getSummaryText(df: DataFrame): String {
        val sb = StringBuilder()
        for (variable in df.variables()) {
            sb.append(variable.toSummaryString())
                    .append(" numeric: " + df.isNumeric(variable))
                    .append(" size: " + df[variable].size)
                    .append('\n')
        }
        return sb.toString()
    }

    fun removeAllExcept(df: DataFrame, keepNames: Set<String>): DataFrame {
        val b = df.builder()
        for (variable in df.variables()) {
            if (!keepNames.contains(variable.name)) {
                b.remove(variable)
            }
        }
        return b.build()
    }
}
