package com.tealcube.minecraft.bukkit.mythicdrops.config.migration.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.SemVer

/**
 * Moshi adapter for the [SemVer] class.
 */
object SemVerAdapter {
    @ToJson
    fun toJson(version: SemVer): String = version.toString()

    @FromJson
    fun fromJson(version: String): SemVer = SemVer.parse(version)
}
