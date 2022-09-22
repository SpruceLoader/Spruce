package xyz.unifycraft.uniloader.loader.impl.metadata

import xyz.unifycraft.uniloader.loader.api.metadata.Version

data class Dependency(
    val id: String,
    val version: Version,
    val unless: List<String>, // Can also be singular
    val only: List<String> // Can also be singular
)
