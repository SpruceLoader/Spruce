package xyz.unifycraft.uniloader.loader.impl.utils

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import xyz.unifycraft.uniloader.loader.exceptions.ParsingException
import java.io.StringReader

/*
 * Extensions for helping with creating JSON parsers.
 */

fun String.readJson(block: JsonReader.(JsonToken) -> Unit) {
    JsonReader(StringReader(this)).use { reader ->
        reader.block(reader.peek())
    }
}

fun JsonReader.requireObject(key: String = "") {
    val token = peek()
    if (token == JsonToken.BEGIN_OBJECT) {
        beginObject()
        return
    }

    throw ParsingException(
        if (key.isBlank())
            "JSON element must be an object!" else
            "$key must be a JSON object!"
    )
}

fun JsonReader.requireArray(key: String = "") {
    val token = peek()
    if (token == JsonToken.BEGIN_ARRAY) {
        beginArray()
        return
    }

    throw ParsingException(
        if (key.isBlank())
            "JSON element must be an array!" else
            "$key must be a JSON array!"
    )
}

class JsonItemIterator(
    private val reader: JsonReader
) {
    internal lateinit var token: JsonToken

    fun readBool(key: String = ""): Boolean {
        if (token != JsonToken.BOOLEAN)
            throw ParsingException(key.constructMessage(" boolean"))
        return reader.nextBoolean()
    }

    fun readInt(key: String = ""): Int {
        if (token != JsonToken.NUMBER)
            throw ParsingException(key.constructMessage("n integer"))
        return reader.nextInt()
    }

    fun readDouble(key: String = ""): Double {
        if (token != JsonToken.NUMBER)
            throw ParsingException(key.constructMessage(" double"))
        return reader.nextDouble()
    }

    fun readLong(key: String = ""): Long {
        if (token != JsonToken.NUMBER)
            throw ParsingException(key.constructMessage(" long"))
        return reader.nextLong()
    }

    fun readString(key: String = ""): String {
        if (token != JsonToken.STRING)
            throw ParsingException(key.constructMessage(" string"))
        return reader.nextString()
    }

    private fun String.constructMessage(type: String): String {
        return if (type.isBlank())
            "JSON element must be a$type!" else
            "'$this' should be a$type!"
    }
}

fun JsonReader.forEachItemObject(block: JsonItemIterator.(token: JsonToken, name: String) -> Unit) {
    val iterator = JsonItemIterator(this)
    var currentName = ""
    while (hasNext()) {
        val token = peek()
        if (token == JsonToken.NAME) {
            currentName = nextName()
            continue
        }

        iterator.token = token
        iterator.block(token, currentName)
    }

    iterator.token = JsonToken.END_DOCUMENT
}

fun JsonReader.forEachItemArray(block: JsonItemIterator.(token: JsonToken) -> Unit) {
    val iterator = JsonItemIterator(this)
    while (hasNext()) {
        val token = peek()
        iterator.token = token
        iterator.block(token)
    }

    iterator.token = JsonToken.END_DOCUMENT
}

/**
 * Interface used to define a JSON parser for something,
 * mostly only used internally.
 */
interface Parser<T> {
    fun parse(input: String): T
}
