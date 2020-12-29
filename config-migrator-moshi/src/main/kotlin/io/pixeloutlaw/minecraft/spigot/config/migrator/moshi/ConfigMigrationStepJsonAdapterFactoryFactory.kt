package io.pixeloutlaw.minecraft.spigot.config.migrator.moshi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models.ConfigMigrationStep

/**
 * Utility for generating [JsonAdapter.Factory]s for [ConfigMigrationStep]s.
 */
// How much more enterprise-y can I make this name?
object ConfigMigrationStepJsonAdapterFactoryFactory {
    private val stepTypesWithLabels = mutableMapOf(
        ConfigMigrationStep.SetBooleanConfigMigrationStep::class to "set_boolean",
        ConfigMigrationStep.SetDoubleConfigMigrationStep::class to "set_double",
        ConfigMigrationStep.SetIntConfigMigrationStep::class to "set_int",
        ConfigMigrationStep.SetStringListConfigMigrationStep::class to "set_string_list",
        ConfigMigrationStep.SetStringConfigMigrationStep::class to "set_string",
        ConfigMigrationStep.DeleteConfigMigrationStep::class to "delete",
        ConfigMigrationStep.RenameConfigMigrationStep::class to "rename",
        ConfigMigrationStep.SetStringIfEqualsConfigMigrationStep::class to "set_string_if_equals",
        ConfigMigrationStep.SetStringListIfKeyEqualsStringConfigMigrationStep::class to
            "set_string_list_if_key_equals_string",
        ConfigMigrationStep.ForEachConfigMigrationStep::class to "for_each",
        ConfigMigrationStep.RenameEachConfigMigrationStep::class to "rename_each",
    )

    /**
     * Generates a [JsonAdapter.Factory] for [ConfigMigrationStep]s based off of registered migration step types.
     */
    fun create(): JsonAdapter.Factory {
        val factory = PolymorphicJsonAdapterFactory.of(ConfigMigrationStep::class.java, "stepType")
        return stepTypesWithLabels.entries.fold(factory) { acc, (stepType, label) ->
            acc.withSubtype(stepType.java, label)
        }
    }
}
