package io.pixeloutlaw.minecraft.spigot.config.migrator.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import io.pixeloutlaw.minecraft.spigot.config.SemVer

/**
 * Moshi adapter for the [SemVer] class.
 */
object SemVerAdapter {
    @ToJson
    fun toJson(version: SemVer): String = version.toString()

    @FromJson
    fun fromJson(version: String): SemVer = SemVer.parse(version)
}
