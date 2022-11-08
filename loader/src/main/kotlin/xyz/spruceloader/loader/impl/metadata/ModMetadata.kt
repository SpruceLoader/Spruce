package xyz.spruceloader.loader.impl.metadata

import com.google.gson.JsonObject
import xyz.spruceloader.loader.api.metadata.Version

data class ModMetadata(
    val schemaVersion: Int,

    val name: String?,
    val version: Version,
    val id: String,
    val type: List<ModType>,

    val license: License?,

    val contributors: List<Contributor>,
    val links: ModLinks?,

    val loader: LoaderData?,

    val additional: JsonObject
) {
    companion object {
        const val CURRENT_SCHEMA_VERSION = 1
        const val FILE_NAME = "mod.metadata.json"
    }
}
