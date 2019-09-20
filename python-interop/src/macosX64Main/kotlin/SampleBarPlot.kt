import Python.*
import kotlinx.cinterop.*
import utils.TypeUtils.pyDictToMap

object SampleBarPlot {
    fun getHTML(self: CPointer<PyObject>?, args: CPointer<PyObject>?): CPointer<PyObject>? {
        val inputDict: CPointerVar<PyObject> = nativeHeap.allocPointerTo<PyObject>()

        PyArg_ParseTuple(args, "O", inputDict.ptr)

        val plotSpecMap = pyDictToMap(inputDict.value)

        val html = PlotHtmlGen.getHtml(plotSpecMap)
        println(plotSpecMap)

        val result = Py_BuildValue("s", html);

        return result
    }
}