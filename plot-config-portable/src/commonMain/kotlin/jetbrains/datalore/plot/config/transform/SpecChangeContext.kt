package jetbrains.datalore.plot.config.transform

interface SpecChangeContext {
    fun getSpecsAbsolute(vararg keys: String): List<Map<String, Any>>
}