package jetbrains.datalore.base.json

actual object JsonSupport {
    actual fun parseJson(json: String): MutableMap<String, Any> {
        val jsonObj = JSON.parse<dynamic>(json)
        return objectToMutableMap(jsonObj)
    }

    actual fun toJson(o: Any): String {
        if (o is Map<*, *>) {
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
                val list = mutableListOf<Any>()
                for (el in value) {
                    if (el is Number || el is String) {
                        list.add(el)
                    } else {
                        list.add(objectToMutableMap(el))
                    }
                }
                value = list
            } else if (!(value is Number) && !(value is String)) {
                value = objectToMutableMap(value)
            }
            map[key] = value
        }
        return map
    }
}
