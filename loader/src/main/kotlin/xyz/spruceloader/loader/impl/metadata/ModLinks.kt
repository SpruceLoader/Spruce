package xyz.spruceloader.loader.impl.metadata

data class ModLinks(
    val home: String?,
    val source: String?,
    val issues: String?,
    val discord: String?
) {
    companion object {

        @JvmStatic
        fun empty() =
            ModLinks(null, null, null, null)

    }
}
