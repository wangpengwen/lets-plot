package jetbrains.datalore.base.json

import kotlinx.serialization.internal.StringSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.map

actual object JsonSupport {
    actual fun parseJson(json: String): MutableMap<String, Any> {
        val jsonObj = JSON.parse<dynamic>(json)
        val map = objectToMutableMap(jsonObj)
        return map
    }

    actual fun toJson(o: Any): String {
        println(o.toString())
        if (o is Map<*, *>) {
            println("GGGGGG")
            println(o["mapping"].toString())
            println("MAP")
            val d: dynamic = mutateMapToObject(o)
            return JSON.stringify(d)
        } else {
            throw IllegalArgumentException()
        }
    }

    private fun mutateMapToObject(o: Map<*, *>): dynamic {
        val d: dynamic = object{}

        for (pair in o) {
            val key = pair.key.toString()
            var value: dynamic = pair.value

            if (value is Map<*, *>) {
                value = mutateMapToObject(value)
            }

            d[key] = value
        }

        return d
    }

    private fun objectToMutableMap(o: dynamic): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        val keys = js("Object").keys(o).unsafeCast<Array<String>>()
        for (key in keys) {
            var value = o[key]
            if (value is Array<*>) {
                console.log(value)
                val list = mutableListOf<Any>()
                for (el in value) {
                    list.add(el!!)
                }
                value = list
            }
            map[key] = value
        }
        return map
    }
}
