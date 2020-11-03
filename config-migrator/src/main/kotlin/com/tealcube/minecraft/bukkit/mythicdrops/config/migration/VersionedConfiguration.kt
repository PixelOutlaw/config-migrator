package com.tealcube.minecraft.bukkit.mythicdrops.config.migration

import io.pixeloutlaw.minecraft.spigot.bandsaw.JulLoggerFactory
import java.util.logging.Level
import org.bukkit.configuration.Configuration

/**
 * A variant of [Configuration] that has a `version` field that adheres to Semantic Versioning.
 */
interface VersionedConfiguration : Configuration {
    companion object {
        private val logger by lazy { JulLoggerFactory.getLogger(VersionedConfiguration::class) }
    }

    /**
     * Parsed [SemVer] from the `version` field.
     */
    var version: SemVer
        get() = try {
            SemVer.parse(getString("version") ?: "")
        } catch (inevitable: Exception) {
            logger.log(Level.SEVERE, "Unable to parse semantic version from \"version\" field", inevitable)
            SemVer(0, 0, 0)
        }
        set(value) {
            this.set("version", value.toString())
        }
}
