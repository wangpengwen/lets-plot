package jetbrains.datalore.visualization.plot.gog.common.base64

import org.khronos.webgl.*
import kotlin.browser.window

actual object BinaryUtil {
    actual fun encodeList(l: List<Double?>): String {
        val notNullList = l.map { it ?: Double.NaN }
        val arr = Float64Array(notNullList.toTypedArray())
        val bytesView = createBufferByteView(arr.buffer)
        val binString = toBinString(bytesView)

        return b64encode(binString)
    }

    actual fun decodeList(s: String): List<Double> {
        val binStr = b64decode(s)
        val length = binStr.length

        val doubles = ArrayList<Double>(length / 16)

        val buffer = ArrayBuffer(8)
        val bytesView = createBufferByteView(buffer)
        val doublesView = createBufferDoubleView(buffer)

        for (i in 0 until length / 16) {
            val pos = i * 16
            val bytes = Array<Byte>(8) {
                val bytePos = pos + it * 2
                binStr.slice(bytePos .. bytePos + 1).toInt(16).toByte()
            }
            decodeDouble(
                    bytes,
                    bytesView
            )
            doubles.add(doublesView[0])
        }
        return doubles
    }

    private fun toBinString(arr: Uint8Array): String {
        var str = ""
        for (i in 0 until arr.length) {
            str += arr[i].toString(16).padStart(2, '0')
        }
        return str
    }

    private fun b64decode(a: String): String {
        return window.atob(a)
    }

    private fun b64encode(a: String): String {
        return window.btoa(a)
    }

    private fun createBufferByteView(buf: ArrayBuffer): Uint8Array {
        return Uint8Array(buf)
    }

    private fun createBufferDoubleView(buf: ArrayBuffer): Float64Array {
        return Float64Array(buf)
    }

    private fun decodeDouble(
            arr: Array<Byte>,
            byteView: Uint8Array
    ) {
        byteView[0] = arr[0]
        byteView[1] = arr[1]
        byteView[2] = arr[2]
        byteView[3] = arr[3]
        byteView[4] = arr[4]
        byteView[5] = arr[5]
        byteView[6] = arr[6]
        byteView[7] = arr[7]
    }
}
