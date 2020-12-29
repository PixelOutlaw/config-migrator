package io.pixeloutlaw.minecraft.spigot.config

import org.bukkit.configuration.Configuration

/**
 * A variant of [Configuration] that has a `version` field that adheres to Semantic Versioning.
 */
interface VersionedConfiguration : Configuration {
    /**
     * Parsed [SemVer] from the `version` field.
     */
    var version: SemVer
        get() = try {
            SemVer.parse(getString("version") ?: "")
        } catch (iae: IllegalArgumentException) {
            SemVer(0, 0, 0)
        }
        set(value) {
            this.set("version", value.toString())
        }
}
