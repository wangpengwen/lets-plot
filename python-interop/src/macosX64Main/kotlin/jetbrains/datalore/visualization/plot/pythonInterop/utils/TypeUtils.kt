package utils

import Python.*
import kotlinx.cinterop.*

object TypeUtils {
    fun getPyObjectType(obj: CPointer<PyObject>?) = PyObject_Type(obj)!!.reinterpret<PyTypeObject>().pointed.tp_name?.toKString()

    fun pyObjectToKotlin(obj: CPointer<PyObject>?): Any? {
        if (obj == null) return null;

        val objType = getPyObjectType(obj)

        return when(objType) {
            PythonTypes.STR -> pyStrToString(obj)
            PythonTypes.INT -> pyIntToLong(obj)
            PythonTypes.FLOAT -> pyFloatToDouble(obj)
            PythonTypes.BOOL -> pyBoolToBoolean(obj)
            PythonTypes.NONE -> null
            PythonTypes.LIST -> pyListToList(obj)
            PythonTypes.DICT -> pyDictToMap(obj)
            else -> throw IllegalArgumentException("Wrong python type: $objType")
        }
    }

    fun pyStrToString(str: CPointer<PyObject>) = PyBytes_AsString(PyUnicode_AsUTF8String(str))?.toKString()

    fun pyIntToLong(int: CPointer<PyObject>): Long = PyLong_AsLong(int)

    fun pyFloatToDouble(float: CPointer<PyObject>): Double = PyFloat_AsDouble(float)

    fun pyBoolToBoolean(bool: CPointer<PyObject>): Boolean = PyObject_IsTrue(bool) == 1

    fun pyListToList(list: CPointer<PyObject>): List<Any?> {
        val result = mutableListOf<Any?>()

        val listLen = PyList_Size(list)

        for (i in 0 until listLen) {
            val value = PyList_GetItem(list, i)
            result.add(pyObjectToKotlin(value))
        }

        return result
    }

    fun pyDictToMap(dict: CPointer<PyObject>?): Map<String, Any?> {
        val result = mutableMapOf<String, Any?>()
        val keys = PyDict_Keys(dict)
        val keysLen = PyList_Size(keys)
        for (i in 0 until keysLen) {
            val key = PyList_GetItem(keys, i)
            val keyType = getPyObjectType(key)
            assert(keyType != null && keyType == PythonTypes.STR)
            val keyStr = pyStrToString(key!!)!!

            val value = PyDict_GetItem(dict, key)
            result[keyStr] = pyObjectToKotlin(value)
        }

        return result
    }
}