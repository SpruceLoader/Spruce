package xyz.unifycraft.uniloader.loader

class ArgumentMap {
    companion object {
        @JvmStatic
        fun parse(args: Array<String>): ArgumentMap {
            val returnValue = ArgumentMap()
            for (item in args) {
                val index = args.indexOf(item)
                if (args.size - 1 < index) continue

                if (!item.startsWith("--")) continue // This is a value, not an arg name.

                var value = args[index + 1]
                if (value.startsWith("--")) value = "" // This is an empty item.

                val name = item.substring(2) // Remove the "--" from the item.
                returnValue.put(name, value)
            }
            return returnValue
        }

        @JvmStatic
        fun parse(args: List<String>) =
            parse(args.toTypedArray())
    }

    private val internalMap = mutableMapOf<String, String>()

    fun hasName(name: String) =
        internalMap.containsKey(name)
    fun hasKey(key: String) =
        internalMap.containsKey(if (key.startsWith("--")) key else "--$key")

    fun get(name: String) =
        internalMap[name]
    fun getOrDefault(name: String, default: String) =
        internalMap.getOrDefault(name, default)

    fun put(name: String, value: String) =
        internalMap.put(name, value)
    fun remove(name: String) =
        internalMap.remove(name)

    fun toArray(): Array<String> {
        val returnValue = mutableListOf<String>()
        internalMap.forEach { (name, value) ->
            returnValue.add("--$name")
            returnValue.add(value)
        }
        return returnValue.toTypedArray()
    }
}
