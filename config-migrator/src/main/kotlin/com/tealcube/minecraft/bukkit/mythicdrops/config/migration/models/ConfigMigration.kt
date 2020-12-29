package com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models

import io.pixeloutlaw.minecraft.spigot.config.SemVer

/**
 * Represents a migration.
 *
 * @property fileGlobs gitignore style globs for files to match
 * @property fromVersion semver version to migrate from
 * @property toVersion semver version to migrate to
 * @property configMigrationSteps steps to apply in order
 * @property createBackup if backup should be created for migration
 * @property overwrite if this migration is just overwriting a file from the JAR
 */
data class ConfigMigration(
    val fileGlobs: List<String>,
    val fromVersion: SemVer,
    val toVersion: SemVer,
    val configMigrationSteps: List<ConfigMigrationStep> = emptyList(),
    val createBackup: Boolean = true,
    val overwrite: Boolean = false
)
