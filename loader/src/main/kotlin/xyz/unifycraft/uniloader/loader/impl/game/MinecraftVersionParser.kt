package xyz.unifycraft.uniloader.loader.impl.game

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import org.apache.logging.log4j.LogManager
import xyz.unifycraft.uniloader.loader.exceptions.ParsingException
import xyz.unifycraft.uniloader.loader.impl.config.LoaderConfigParser
import java.io.StringReader
import java.time.Instant
import java.util.*

object MinecraftVersionParser {
    private val logger = LogManager.getLogger("Loader Config Parser")

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
                throw ParsingException("Minecraft version does not appear to be a JSON object!")

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
                            throw ParsingException("Minecraft version ID isn't a string?")
                        id = reader.nextString()
                    }
                    "name" -> {
                        if (token != JsonToken.STRING)
                            throw ParsingException("Minecraft version name isn't a string?")
                        name = reader.nextString()
                    }
                    "release_target" -> {
                        if (token != JsonToken.STRING)
                            throw ParsingException("Minecraft version release target isn't a string?")
                        releaseTarget = reader.nextString()
                    }
                    "world_version" -> {
                        if (token != JsonToken.NUMBER)
                            throw ParsingException("Minecraft version's world version isn't an integer?")
                        worldVersion = reader.nextInt()
                    }
                    "series_id" -> {
                        if (token != JsonToken.STRING)
                            throw ParsingException("Minecraft version series ID isn't a string?")
                        seriesId = reader.nextString()
                    }
                    "protocol_version" -> {
                        if (token != JsonToken.NUMBER)
                            throw ParsingException("Minecraft version's protocol version isn't an integer?")
                        protocolVersion = reader.nextInt()
                    }
                    "pack_version" -> packVersion = readPackVersion(reader, token)
                    "build_time" -> {
                        if (token != JsonToken.STRING)
                            throw ParsingException("Minecraft version's build time isn't a string?")
                        buildTime = Date.from(Instant.parse(reader.nextString()))
                    }
                    "java_component" -> {
                        if (token != JsonToken.STRING)
                            throw ParsingException("Minecraft version's Java component isn't a string?")
                        javaComponent = reader.nextString()
                    }
                    "java_version" -> {
                        if (token != JsonToken.NUMBER)
                            throw ParsingException("Minecraft version's Java version isn't an integer?")
                        javaVersion = reader.nextInt()
                    }
                    "stable" -> {
                        if (token != JsonToken.BOOLEAN)
                            throw ParsingException("Minecraft version's stable state isn't a boolean?")
                        stable = reader.nextBoolean()
                    }
                    else -> {
                        logger.warn("Unknown value found - skipping. ($currentName)")
                        reader.skipValue()
                    }
                }
            }

            reader.endObject()
        }

        return MinecraftVersion(
            id ?: throw ParsingException("ID couldn't be found."),
            name ?: throw ParsingException("Name couldn't be found."),
            releaseTarget ?: throw ParsingException("Release target couldn't be found."),
            worldVersion ?: throw ParsingException("World version couldn't be found."),
            seriesId ?: throw ParsingException("Series ID couldn't be found."),
            protocolVersion ?: throw ParsingException("Protocol version couldn't be found."),
            packVersion ?: throw ParsingException("Pack version couldn't be found."),
            buildTime ?: throw ParsingException("Build time couldn't be found."),
            javaComponent ?: throw ParsingException("Java component couldn't be found."),
            javaVersion ?: throw ParsingException("Java version couldn't be found."),
            stable ?: throw ParsingException("Stable state couldn't be found.")
        )
    }

    private fun readPackVersion(reader: JsonReader, token: JsonToken): PackVersion {
        if (token != JsonToken.BEGIN_OBJECT)
            throw ParsingException("Pack version should be an object?")

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
                        throw ParsingException("Pack version's resource isn't an integer?")
                    resource = reader.nextInt()
                }
                "data" -> {
                    if (token != JsonToken.NUMBER)
                        throw ParsingException("Pack version's data isn't an integer?")
                    data = reader.nextInt()
                }
                else -> reader.skipValue()
            }
        }

        reader.endObject()

        return PackVersion(
            resource ?: throw ParsingException("Resource couldn't be found."),
            data ?: throw ParsingException("Data couldn't be found.")
        )
    }
}
