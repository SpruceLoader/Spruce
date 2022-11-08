package xyz.spruceloader.loader.impl.game

import com.google.gson.stream.JsonReader
import xyz.spruceloader.loader.exceptions.ParsingException
import xyz.spruceloader.loader.impl.utils.*
import java.time.Instant
import java.util.*

object MinecraftVersionParser : Parser<MinecraftVersion> {
    override fun parse(input: String): MinecraftVersion {
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

        input.readJson { token ->
            requireObject("Minecraft version")

            forEachItemObject { token, currentName ->
                when (currentName) {
                    "id" -> id = readString("id")
                    "name" -> name = readString("name")
                    "release_target" -> releaseTarget = readString("release_target")
                    "world_version" -> worldVersion = readInt("world_version")
                    "series_id" -> seriesId = readString("series_id")
                    "protocol_version" -> protocolVersion = readInt("protocol_version")
                    "pack_version" -> packVersion = readPackVersion()
                    "build_time" -> buildTime = Date.from(Instant.parse(readString("build_time")))
                    "java_component" -> javaComponent = readString("java_component")
                    "java_version" -> javaVersion = readInt("java_version")
                    "stable" -> stable = readBool("stable")
                    else -> return@forEachItemObject true
                }

                false
            }

            endObject()
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

    private fun JsonReader.readPackVersion(): PackVersion {
        requireObject("Pack version")

        var resource: Int? = null
        var data: Int? = null

        forEachItemObject { _, name ->
            when (name) {
                "resource" -> resource = readInt("resource")
                "data" -> data = readInt("data")
                else -> return@forEachItemObject true
            }

            false
        }

        endObject()

        return PackVersion(
            resource ?: throw ParsingException("Resource couldn't be found."),
            data ?: throw ParsingException("Data couldn't be found.")
        )
    }
}
