package xyz.unifycraft.uniloader.loader.impl.metadata

data class Dependency(
    val id: String,
    val version: Version,
    val unless: List<String>, // Can also be singular
    val only: List<String> // Can also be singular
)
