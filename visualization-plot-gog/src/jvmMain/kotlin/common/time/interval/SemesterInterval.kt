package jetbrains.datalore.visualization.plot.gog.common.time.interval

import jetbrains.datalore.visualization.plot.gog.common.text.DateTimeFormatUtil
import java.util.function.Function

internal class SemesterInterval(count: Int) : TimeInterval(count) {

    override val tickFormatPattern: String
        get() = throw UnsupportedOperationException()

    override val tickFormatter: Function<Any, String>
        get() = TIME_FORMAT_FUNCTION

    override fun range(start: Double, end: Double): List<Double> {
        throw UnsupportedOperationException()
    }

    companion object {

        private val TIME_FORMAT_FUNCTION = Function<Any, String> { input ->
            var result = DateTimeFormatUtil.formatDateUTC(input as Number, "Q")
            if (result.length == 2 && result.startsWith("Q")) {
                try {
                    val quarterNum = Integer.parseInt(result.substring(1))
                    val semesterNumber = (quarterNum + 1) / 2
                    if (semesterNumber == 1 || semesterNumber == 2) {
                        result = "Semester $semesterNumber"
                    }
                } catch (ignored: NumberFormatException) {
                    // ignore
                }

            }
            result
        }
    }
}
