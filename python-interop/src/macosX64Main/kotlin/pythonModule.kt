import Python.*
import kotlinx.cinterop.*

val scope = MemScope()

fun createMethods(): CArrayPointer<PyMethodDef> {
    val methodDefs = nativeHeap.allocArray<PyMethodDef>(2)

    cValue<PyMethodDef> {
        ml_name = "get_sample_plot_html".cstr.getPointer(scope)
        ml_flags = METH_VARARGS
        ml_meth = staticCFunction { self, args -> SampleBarPlot.getHTML(self, args)}
        ml_doc = "Show sample plot".cstr.getPointer(scope)
    }.place(methodDefs[0].ptr)
    cValue<PyMethodDef> {
        ml_name = null
        ml_flags = 0
        ml_meth = null
        ml_doc = null
    }.place(methodDefs[1].ptr)


    return methodDefs
}

val module = cValue<PyModuleDef> {
    m_name = "datalore_plot_kotlin_interop".cstr.getPointer(scope)
    m_doc = "Experiments with Python to kotlin".cstr.getPointer(scope)
    m_size = -1
    m_methods = createMethods()
}

@CName("PyInit_libdatalore_plot_python_interop")
fun PyInit_libdatalore_plot_python_interop() = PyModule_Create2(module, PYTHON_API_VERSION)