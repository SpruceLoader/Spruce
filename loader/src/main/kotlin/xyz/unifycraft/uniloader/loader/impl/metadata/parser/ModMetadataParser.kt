package xyz.unifycraft.uniloader.loader.impl.metadata.parser

import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import xyz.unifycraft.uniloader.loader.api.Environment
import xyz.unifycraft.uniloader.loader.exceptions.InvalidMetadataException
import xyz.unifycraft.uniloader.loader.impl.metadata.*
import java.io.StringReader

object ModMetadataParser {

    @JvmStatic
    fun read(jsonInput: String): ModMetadata {
        var schemaVersion = ModMetadata.CURRENT_SCHEMA_VERSION

        var name: String? = null
        var version: String? = null
        var id: String? = null
        var type = ModType.MOD

        val contributors = mutableListOf<Contributor>()
        var links = ModLinks.empty()

        var loader = LoaderData.empty()

        var additional = JsonObject()

        JsonReader(StringReader(jsonInput)).use { reader ->
            val token = reader.peek()
            if (token != JsonToken.BEGIN_OBJECT)
                throw InvalidMetadataException("Metadata file must be a JSON object!")

            reader.beginObject()

            var currentName = ""
            while (reader.hasNext()) {
                val token = reader.peek()
                if (token == JsonToken.NAME) {
                    currentName = reader.nextName()
                    continue
                }

                when (currentName) {
                    "schema_version" -> {
                        if (token != JsonToken.NUMBER)
                            throw InvalidMetadataException("schema_version should be a number!")
                        schemaVersion = reader.nextInt()
                    }
                    "name" -> {
                        if (token != JsonToken.STRING)
                            throw InvalidMetadataException("name should be a string!")
                        name = reader.nextString()
                    }
                    "version" -> {
                        if (token != JsonToken.STRING)
                            throw InvalidMetadataException("version should be a string!")
                        version = reader.nextString()
                    }
                    "id" -> {
                        if (token != JsonToken.STRING)
                            throw InvalidMetadataException("id should be a string!")
                        id = reader.nextString()
                    }
                    "type" -> {
                        if (token != JsonToken.STRING)
                            throw InvalidMetadataException("type should be a string!")
                        val value = reader.nextString()
                        type = ModType.values().first { it.name.equals(value, true) }
                    }
                    // "license"
                    "contributors" -> readContributors(contributors, reader, token)
                    "links" -> links = readLinks(reader, token)
                    "loader" -> loader = readLoader(reader, token)
                    "additional" -> readAdditional(additional, reader, token)
                    else -> reader.skipValue()
                }
            }

            reader.endObject()
        }

        println("Schema version: $schemaVersion")
        println("Name: $name")
        println("Version: $version")
        println("ID: $id")
        println("Type: $type")
        println("Contributors: $contributors")
        println("Links: $links")
        println("Loader: $loader")

        return ModMetadata(
            schemaVersion,

            name,
            version ?: throw InvalidMetadataException("A version is required!"),
            id ?: throw InvalidMetadataException("A mod ID is required!"),
            type,

            contributors,
            links,

            loader,
            additional
        )
    }

    private fun readContributors(value: MutableList<Contributor>, reader: JsonReader, token: JsonToken) {
        if (token != JsonToken.BEGIN_ARRAY)
            throw InvalidMetadataException("contributors should be an array!")

        reader.beginArray()
        while (reader.hasNext()) {
            val token = reader.peek()
            if (token != JsonToken.BEGIN_OBJECT)
                throw InvalidMetadataException("contributor should be an object!")

            reader.beginObject()

            var contributorName = ""
            var contributorRole = ""

            var currentName = ""
            while (reader.hasNext()) {
                val token = reader.peek()
                if (token == JsonToken.NAME) {
                    currentName = reader.nextName()
                    continue
                }

                when (currentName) {
                    "name" -> {
                        if (token != JsonToken.STRING)
                            throw InvalidMetadataException("contributor name should be a string!")
                        contributorName = reader.nextString()
                    }
                    "role" -> {
                        if (token != JsonToken.STRING)
                            throw InvalidMetadataException("contributor role should be a string!")
                        contributorRole = reader.nextString()
                    }
                }
            }

            reader.endObject()

            value.add(Contributor(contributorName, contributorRole))
        }

        reader.endArray()
    }

    private fun readLinks(reader: JsonReader, token: JsonToken): ModLinks {
        if (token != JsonToken.BEGIN_OBJECT)
            throw InvalidMetadataException("links should be an object!")

        var linksHome: String? = null
        var linksSource: String? = null
        var linksIssues: String? = null
        var linksDiscord: String? = null

        reader.beginObject()

        var currentName = ""
        while (reader.hasNext()) {
            val token = reader.peek()
            if (token == JsonToken.NAME) {
                currentName = reader.nextName()
                continue
            }

            when (currentName) {
                "home" -> {
                    if (token != JsonToken.STRING)
                        throw InvalidMetadataException("links home should be a string!")
                    linksHome = reader.nextString()
                }
                "source" -> {
                    if (token != JsonToken.STRING)
                        throw InvalidMetadataException("links source should be a string!")
                    linksSource = reader.nextString()
                }
                "issues" -> {
                    if (token != JsonToken.STRING)
                        throw InvalidMetadataException("links issues should be a string!")
                    linksIssues = reader.nextString()
                }
                "discord" -> {
                    if (token != JsonToken.STRING)
                        throw InvalidMetadataException("links discord should be a string!")
                    linksDiscord = reader.nextString()
                }
            }
        }

        reader.endObject()
        return ModLinks(linksHome, linksSource, linksIssues, linksDiscord)
    }

    private fun readLoader(reader: JsonReader, token: JsonToken): LoaderData {
        if (token != JsonToken.BEGIN_OBJECT)
            throw InvalidMetadataException("loader should be an object!")

        var environment = Environment.BOTH
        val accessWideners = mutableListOf<String>()
        val mixins = mutableListOf<String>()
        // val entrypoints =
        // val dependencies =

        reader.beginObject()

        var currentName = ""
        while (reader.hasNext()) {
            val token = reader.peek()
            if (token == JsonToken.NAME) {
                currentName = reader.nextName()
                continue
            }

            when (currentName) {
                "env", "environment" -> {
                    if (token != JsonToken.STRING)
                        throw InvalidMetadataException("loader env/environment should be a string!")
                    var value = reader.nextString()
                    if (value == "*")
                        value = "both"
                    environment = Environment.values().first { it.name.equals(value, true) }
                }
                "access_wideners" -> {
                    when (token) {
                        JsonToken.STRING -> {
                            accessWideners.add(reader.nextString())
                        }
                        JsonToken.BEGIN_ARRAY -> {
                            reader.beginArray()

                            while (reader.hasNext()) {
                                val token = reader.peek()
                                if (token != JsonToken.STRING)
                                    throw InvalidMetadataException("loader access wideners only accepts strings!")
                                accessWideners.add(reader.nextString())
                            }

                            reader.endArray()
                        }
                        else -> throw InvalidMetadataException("loader access wideners should either be a string or an array!")
                    }
                }
                "mixins" -> {
                    when (token) {
                        JsonToken.STRING -> {
                            mixins.add(reader.nextString())
                        }
                        JsonToken.BEGIN_ARRAY -> {
                            reader.beginArray()

                            while (reader.hasNext()) {
                                val token = reader.peek()
                                if (token != JsonToken.STRING)
                                    throw InvalidMetadataException("loader mixins only accepts strings!")
                                mixins.add(reader.nextString())
                            }

                            reader.endArray()
                        }
                        else -> throw InvalidMetadataException("loader mixins should either be a string or an array!")
                    }
                }
            }
        }

        return LoaderData(environment, accessWideners, mixins, mapOf())
    }

    fun readAdditional(value: JsonObject, reader: JsonReader, token: JsonToken) {

    }

}
