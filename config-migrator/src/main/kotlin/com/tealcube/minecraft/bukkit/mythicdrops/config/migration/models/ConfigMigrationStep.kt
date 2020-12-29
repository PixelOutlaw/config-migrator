package com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models

import org.bukkit.configuration.ConfigurationSection

/**
 * Represents a step that can be run as part of a migration.
 */
sealed class ConfigMigrationStep {
    abstract fun migrate(configuration: ConfigurationSection)

    data class DeleteConfigMigrationStep(val path: String) : ConfigMigrationStep() {
        override fun migrate(configuration: ConfigurationSection) {
            configuration[path] = null
        }
    }

    data class ForEachConfigMigrationStep(
        val matchRegex: String,
        val configMigrationSteps: List<ConfigMigrationStep> = emptyList()
    ) : ConfigMigrationStep() {
        override fun migrate(configuration: ConfigurationSection) {
            val parentRegex = matchRegex.toRegex()
            val keysThatMatchParent = configuration.getKeys(true).filter { it.matches(parentRegex) }
            for (keyThatMatchesParent in keysThatMatchParent) {
                if (!configuration.isConfigurationSection(keyThatMatchesParent)) {
                    continue
                }
                val matchesSection =
                    configuration.getConfigurationSection(keyThatMatchesParent) ?: configuration.createSection(
                        keyThatMatchesParent
                    )
                for (configMigrationStep in configMigrationSteps) {
                    configMigrationStep.migrate(matchesSection)
                }
            }
        }
    }

    data class RenameEachConfigMigrationStep(val matchRegex: String, val to: String) : ConfigMigrationStep() {
        override fun migrate(configuration: ConfigurationSection) {
            val parentRegex = matchRegex.toRegex()
            val keysThatMatchParent = configuration.getKeys(true).filter { it.matches(parentRegex) }
            for (keyThatMatchesParent in keysThatMatchParent) {
                val oldValue = configuration[keyThatMatchesParent]
                configuration[keyThatMatchesParent] = null
                configuration[to.replace("%self%", keyThatMatchesParent)] = oldValue
            }
        }
    }

    data class RenameConfigMigrationStep(val from: String, val to: String) : ConfigMigrationStep() {
        override fun migrate(configuration: ConfigurationSection) {
            if (configuration[from] == null) {
                return
            }
            configuration[to] = configuration[from]
            configuration[from] = null
        }
    }

    data class SetBooleanConfigMigrationStep(val key: String, val value: Boolean) : ConfigMigrationStep() {
        override fun migrate(configuration: ConfigurationSection) {
            configuration[key] = value
        }
    }

    data class SetDoubleConfigMigrationStep(val key: String, val value: Double) : ConfigMigrationStep() {
        override fun migrate(configuration: ConfigurationSection) {
            configuration[key] = value
        }
    }

    data class SetIntConfigMigrationStep(val key: String, val value: Int) : ConfigMigrationStep() {
        override fun migrate(configuration: ConfigurationSection) {
            configuration[key] = value
        }
    }

    data class SetStringListConfigMigrationStep(val key: String, val value: List<String> = emptyList()) :
        ConfigMigrationStep() {
        override fun migrate(configuration: ConfigurationSection) {
            configuration[key] = value
        }
    }

    data class SetStringListIfKeyEqualsStringConfigMigrationStep(
        val key: String,
        val value: List<String>,
        val ifKey: String,
        val ifValue: String
    ) : ConfigMigrationStep() {
        override fun migrate(configuration: ConfigurationSection) {
            if (configuration.getString(ifKey) == ifValue) {
                configuration[key] = value
            }
        }
    }

    data class SetStringConfigMigrationStep(val key: String, val value: String) : ConfigMigrationStep() {
        override fun migrate(configuration: ConfigurationSection) {
            configuration[key] = value
        }
    }

    data class SetStringIfEqualsConfigMigrationStep(val key: String, val value: String, val ifValue: String) :
        ConfigMigrationStep() {
        override fun migrate(configuration: ConfigurationSection) {
            if (configuration.getString(key) == ifValue) {
                configuration[key] = value
            }
        }
    }
}
