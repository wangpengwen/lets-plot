package jetbrains.datalore.visualization.plot.gog.common.time.interval

internal class QuarterInterval(count: Int) : TimeInterval(count) {

    override val tickFormatPattern: String
        get() = "Q"

    override fun range(start: Double, end: Double): List<Double> {
        throw UnsupportedOperationException()
    }

}
