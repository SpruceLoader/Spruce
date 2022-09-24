package xyz.unifycraft.uniloader.loader.impl

import java.util.Date

//TODO: Move this ot a better place
data class MinecraftVersion(
    val id: String,
    val name: String,
    val release_target: String,
    val world_version: Int,
    val series_id: String,
    val protocol_version: Int,
    val pack_version: PackVersion,
    val build_time: Date,
    val java_component: String,
    val java_version: Int,
    val stable: Boolean
)