package xyz.unifycraft.uniloader.loader.impl.game

import java.util.Date

data class MinecraftVersion(
    val id: String,
    val name: String,
    val releaseTarget: String,
    val worldVersion: Int,
    val seriesId: String,
    val protocolVersion: Int,
    val packVersion: PackVersion,
    val buildTime: Date,
    val javaComponent: String,
    val javaVersion: Int,
    val stable: Boolean
)
