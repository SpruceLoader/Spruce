package xyz.unifycraft.uniloader.loader.impl.game

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import xyz.unifycraft.uniloader.loader.exceptions.GeneralParsingException
import java.io.StringReader
import java.time.Instant
import java.util.*

object MinecraftVersionParser {
    @JvmStatic
    fun parse(input: String): MinecraftVersion {
        var id: String? = null
        var name: String? = null
        var releaseTarget: String? = null
        var worldVersion: Int? = null
        var seriesId: String? = null
        var protocolVersion: Int? = null
        var packVersion: PackVersion? = null
        var buildTime: Date? = null
        var javaComponent: String? = null
        var javaVersion: Int? = null
        var stable: Boolean? = null

        JsonReader(StringReader(input)).use { reader ->
            val token = reader.peek()
            if (token != JsonToken.BEGIN_OBJECT)
                throw GeneralParsingException("Minecraft version does not appear to be a JSON object!")

            reader.beginObject()


            var currentName = ""
            while (reader.hasNext()) {
                val token = reader.peek()
                if (token == JsonToken.NAME) {
                    currentName = reader.nextName()
                    continue
                }

                when (currentName) {
                    "id" -> {
                        if (token != JsonToken.STRING)
                            throw GeneralParsingException("Minecraft version ID isn't a string?")
                        id = reader.nextString()
                    }
                    "name" -> {
                        if (token != JsonToken.STRING)
                            throw GeneralParsingException("Minecraft version name isn't a string?")
                        name = reader.nextString()
                    }
                    "release_target" -> {
                        if (token != JsonToken.STRING)
                            throw GeneralParsingException("Minecraft version release target isn't a string?")
                        releaseTarget = reader.nextString()
                    }
                    "world_version" -> {
                        if (token != JsonToken.NUMBER)
                            throw GeneralParsingException("Minecraft version's world version isn't an integer?")
                        worldVersion = reader.nextInt()
                    }
                    "series_id" -> {
                        if (token != JsonToken.STRING)
                            throw GeneralParsingException("Minecraft version series ID isn't a string?")
                        seriesId = reader.nextString()
                    }
                    "protocol_version" -> {
                        if (token != JsonToken.NUMBER)
                            throw GeneralParsingException("Minecraft version's protocol version isn't an integer?")
                        protocolVersion = reader.nextInt()
                    }
                    "pack_version" -> packVersion = readPackVersion(reader, token)
                    "build_time" -> {
                        if (token != JsonToken.STRING)
                            throw GeneralParsingException("Minecraft version's build time isn't a string?")
                        buildTime = Date.from(Instant.parse(reader.nextString()))
                    }
                    "java_component" -> {
                        if (token != JsonToken.STRING)
                            throw GeneralParsingException("Minecraft version's Java component isn't a string?")
                        javaComponent = reader.nextString()
                    }
                    "java_version" -> {
                        if (token != JsonToken.NUMBER)
                            throw GeneralParsingException("Minecraft version's Java version isn't an integer?")
                        javaVersion = reader.nextInt()
                    }
                    "stable" -> {
                        if (token != JsonToken.BOOLEAN)
                            throw GeneralParsingException("Minecraft version's stable state isn't a boolean?")
                        stable = reader.nextBoolean()
                    }
                    else -> reader.skipValue()
                }
            }

            reader.endObject()
        }

        return MinecraftVersion(
            id ?: throw GeneralParsingException("ID couldn't be found."),
            name ?: throw GeneralParsingException("Name couldn't be found."),
            releaseTarget ?: throw GeneralParsingException("Release target couldn't be found."),
            worldVersion ?: throw GeneralParsingException("World version couldn't be found."),
            seriesId ?: throw GeneralParsingException("Series ID couldn't be found."),
            protocolVersion ?: throw GeneralParsingException("Protocol version couldn't be found."),
            packVersion ?: throw GeneralParsingException("Pack version couldn't be found."),
            buildTime ?: throw GeneralParsingException("Build time couldn't be found."),
            javaComponent ?: throw GeneralParsingException("Java component couldn't be found."),
            javaVersion ?: throw GeneralParsingException("Java version couldn't be found."),
            stable ?: throw GeneralParsingException("Stable state couldn't be found.")
        )
    }

    private fun readPackVersion(reader: JsonReader, token: JsonToken): PackVersion {
        if (token != JsonToken.BEGIN_OBJECT)
            throw GeneralParsingException("Pack version should be an object?")

        var resource: Int? = null
        var data: Int? = null

        reader.beginObject()

        var currentName = ""
        while (reader.hasNext()) {
            val token = reader.peek()
            if (token == JsonToken.NAME) {
                currentName = reader.nextName()
                continue
            }

            when (currentName) {
                "resource" -> {
                    if (token != JsonToken.NUMBER)
                        throw GeneralParsingException("Pack version's resource isn't an integer?")
                    resource = reader.nextInt()
                }
                "data" -> {
                    if (token != JsonToken.NUMBER)
                        throw GeneralParsingException("Pack version's data isn't an integer?")
                    data = reader.nextInt()
                }
                else -> reader.skipValue()
            }
        }

        reader.endObject()

        return PackVersion(
            resource ?: throw GeneralParsingException("Resource couldn't be found."),
            data ?: throw GeneralParsingException("Data couldn't be found.")
        )
    }
}
