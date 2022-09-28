package xyz.unifycraft.uniloader.loader.impl.metadata.parser

import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import xyz.unifycraft.uniloader.loader.api.Environment
import xyz.unifycraft.uniloader.loader.api.metadata.Version
import xyz.unifycraft.uniloader.loader.exceptions.InvalidMetadataException
import xyz.unifycraft.uniloader.loader.impl.metadata.*
import xyz.unifycraft.uniloader.loader.impl.utils.*

object ModMetadataParser : Parser<ModMetadata> {
    override fun parse(input: String): ModMetadata {
        var schemaVersion = ModMetadata.CURRENT_SCHEMA_VERSION

        var name: String? = null
        var version: Version? = null
        var id: String? = null
        var type = mutableListOf(ModType.MOD)

        var license: License? = null

        val contributors = mutableListOf<Contributor>()
        var links: ModLinks? = null

        var loader: LoaderData? = null

        var additional = JsonObject()

        input.readJson {
            requireObject("Mod metadata")

            forEachItemObject { token, currentName ->
                when (currentName) {
                    "schema_version" -> schemaVersion = readInt("schema_version")
                    "name" -> name = readString("name")
                    "version" -> version = VersionParser.parse(readString("version"))
                    "id" -> id = readString("id")
                    "type" -> type.addAll(readTypes(token))
                    "license" -> license = readLicense()
                    "contributors" -> contributors.addAll(readContributors())
                    "links" -> links = readLinks()
                    "loader" -> loader = readLoader()
                    "additional" -> readAdditional(additional)
                    else -> return@forEachItemObject true
                }

                false
            }

            endObject()
        }

        return ModMetadata(
            schemaVersion,

            name,
            version ?: throw InvalidMetadataException("A version is required!"),
            id ?: throw InvalidMetadataException("A mod ID is required!"),
            type,

            license,

            contributors,
            links,

            loader,
            additional
        )
    }

    private fun JsonReader.readTypes(token: JsonToken): List<ModType> {
        fun determineTypeFrom(input: String) = ModType.values().firstOrNull {
            it.name.equals(input, true)
        } ?: throw InvalidMetadataException("Invalid mod type entered!")

        when (token) {
            JsonToken.STRING -> return listOf(determineTypeFrom(nextString()))
            JsonToken.BEGIN_ARRAY -> {
                beginArray()

                val value = mutableListOf<ModType>()
                forEachItemArray { token ->
                    value.add(determineTypeFrom(readString("Mod type")))
                    false
                }

                endArray()
                return value
            }
            else -> throw InvalidMetadataException("type should be a string or an array!")
        }
    }

    private fun JsonReader.readLicense(): License {
        requireObject("Mod license")

        var licenseName = ""
        var licenseUrl: String? = null

        forEachItemObject { token, name ->
            when (name) {
                "name", "display_name" -> licenseName = readString("name/display_name")
                "url" -> licenseUrl = readString("url")
            }

            false
        }

        endObject()

        if (licenseName.isBlank())
            throw InvalidMetadataException("license name/display_name must be present!")

        return License(licenseName, licenseUrl)
    }

    private fun JsonReader.readContributors(): List<Contributor> {
        requireArray("Mod contributors")
        val value = mutableListOf<Contributor>()

        forEachItemArray {
            requireObject("Mod contributor")

            var contributorName = ""
            var contributorRole = ""

            forEachItemObject { token, name ->
                when (name) {
                    "name" -> contributorName = readString("name")
                    "role" -> contributorRole = readString("role")
                    else -> return@forEachItemObject true
                }

                false
            }

            endObject()

            if (contributorName.isBlank())
                throw InvalidMetadataException("contributor name must be present!")
            if (contributorRole.isBlank())
                throw InvalidMetadataException("contributor role must be present!")
            value.add(Contributor(contributorName, contributorRole))

            false
        }

        endArray()
        return value
    }

    private fun JsonReader.readLinks(): ModLinks {
        requireObject("Mod links")

        var linksHome: String? = null
        var linksSource: String? = null
        var linksIssues: String? = null
        var linksDiscord: String? = null

        forEachItemObject { token, name ->
            when (name) {
                "home" -> linksHome = readString("home")
                "source" -> linksSource = readString("source")
                "issues" -> linksIssues = readString("issues")
                "discord" -> linksDiscord = readString("discord")
                else -> return@forEachItemObject true
            }

            false
        }

        endObject()
        return ModLinks(linksHome, linksSource, linksIssues, linksDiscord)
    }

    private fun JsonReader.readLoader(): LoaderData {
        requireObject("Mod loader data")

        var environment = Environment.BOTH
        val entrypoints = mutableMapOf<String, EntrypointMetadata>()
        val dependencies = mutableListOf<Dependency>()

        forEachItemObject { token, name ->
            when (name) {
                "env", "environment" -> {
                    var value = readString("environment")
                    if (value == "*")
                        value = "both"
                    environment = Environment.values().first { it.name.equals(value, true) }
                }
                "entrypoints" -> {
                    requireArray("Mod entrypoints")

                    forEachItemArray {
                        beginObject()

                        var entrypointType = ""
                        var entrypointValue = ""
                        var entrypointAdapter: String? = null

                        forEachItemObject { token, name ->
                            when (name) {
                                "type" -> entrypointType = readString("type")
                                "value" -> entrypointValue = readString("value")
                                "adapter" -> entrypointAdapter = readString("adapter")
                                else -> return@forEachItemObject true
                            }

                            false
                        }

                        entrypoints[entrypointType] = EntrypointMetadata(entrypointAdapter, entrypointValue)
                        endObject()
                        false
                    }

                    endArray()
                }
                "dependencies" -> {
                    requireArray("Mod dependencies")

                    forEachItemArray {
                        requireObject("Mod dependency")

                        var dependencyId = ""
                        var dependencyVersion: Version? = null
                        val dependencyUnless = mutableListOf<String>()
                        val dependencyOnly = mutableListOf<String>()

                        forEachItemObject { token, name ->
                            when (name) {
                                "id" -> dependencyId = readString("id")
                                "version" -> dependencyVersion = VersionParser.parse(readString("version"))
                                "unless" -> {
                                    when (token) {
                                        JsonToken.STRING -> dependencyUnless.add(readString())
                                        JsonToken.BEGIN_ARRAY -> {
                                            beginArray()

                                            forEachItemArray {
                                                dependencyUnless.add(readString())
                                                false
                                            }

                                            endArray()
                                        }
                                        else -> throw InvalidMetadataException("loader dependency (ID: $dependencyId) unless should either be a string or an array!")
                                    }
                                }
                                "only" -> {
                                    when (token) {
                                        JsonToken.STRING -> dependencyOnly.add(readString())
                                        JsonToken.BEGIN_ARRAY -> {
                                            beginArray()

                                            forEachItemArray {
                                                dependencyOnly.add(readString())
                                                false
                                            }

                                            endArray()
                                        }
                                        else -> throw InvalidMetadataException("loader dependency (ID: $dependencyId) only should either be a string or an array!")
                                    }
                                }
                            }

                            false
                        }

                        endObject()

                        if (dependencyVersion == null)
                            throw InvalidMetadataException("Dependency version appears to be invalid! ($dependencyId)")
                        dependencies.add(Dependency(dependencyId, dependencyVersion!!, dependencyUnless, dependencyOnly))

                        false
                    }

                    endArray()
                }
                else -> return@forEachItemObject true
            }

            false
        }

        endObject()

        return LoaderData(environment, entrypoints, dependencies)
    }

    private fun JsonReader.readAdditional(value: JsonObject) {
        requireObject("Mod additional values")

        forEachItemObject { token, name ->
            when (token) {
                JsonToken.STRING -> value.addProperty(name, readString())
                JsonToken.BOOLEAN -> value.addProperty(name, readString())
                JsonToken.NULL -> value.add(name, JsonNull.INSTANCE)
                JsonToken.NUMBER -> {
                    val str = readString()
                    if (str.contains(".")) {
                        val double = str.toDoubleOrNull()
                        if (double == null) {
                            value.addProperty(name, str.toLong())
                        } else value.addProperty(name, double)
                    } else value.addProperty(name, str.toInt())
                }

                else -> return@forEachItemObject true
            }

            false
        }

        endObject()
    }

}
