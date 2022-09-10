package xyz.unifycraft.uniloader.loader.impl.metadata

import com.google.gson.JsonObject

data class ModMetadata(
    val schemaVersion: Int,

    val name: String?,
    val version: String,
    val id: String,
    val type: List<ModType>,

    val license: License,

    val contributors: List<Contributor>,
    val links: ModLinks,

    val loader: LoaderData,

    val additional: JsonObject
) {
    companion object {
        const val CURRENT_SCHEMA_VERSION = 1
        const val FILE_NAME = "mod.metadata.json"
    }
}
