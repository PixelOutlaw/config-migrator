package com.tealcube.minecraft.bukkit.mythicdrops.config.migration.converters

import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models.ConfigMigration

/**
 * Functional interface for converting raw strings into [ConfigMigration]s. Example potential implementations
 * could include JSON parsers, YAML parsers, HOCON parsers, etc.
 */
fun interface ConfigMigrationConverter {
    /**
     * Converts the given raw string into an instance of [ConfigMigration]. Should return null if unable to convert
     * instead of throwing an exception.
     */
    fun convertToConfigMigration(rawString: String): ConfigMigration?
}
