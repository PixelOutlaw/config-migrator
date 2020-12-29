package io.pixeloutlaw.minecraft.spigot.config

import java.io.File

/**
 * An extension of [FileAwareYamlConfiguration] that is also a [VersionedConfiguration].
 */
class VersionedFileAwareYamlConfiguration @JvmOverloads constructor(file: File? = null) :
    FileAwareYamlConfiguration(file), VersionedConfiguration {
    override fun toString(): String {
        val keysAndValues = getKeys(true).mapNotNull {
            if (isConfigurationSection(it)) {
                return@mapNotNull null
            }
            it to get(it)
        }.toMap()
        return "VersionedFileAwareYamlConfiguration(version=$version,contents=$keysAndValues)"
    }
}
