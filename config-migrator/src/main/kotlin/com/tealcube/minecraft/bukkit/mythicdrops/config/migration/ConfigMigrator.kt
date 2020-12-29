package com.tealcube.minecraft.bukkit.mythicdrops.config.migration

import com.github.shyiko.klob.Glob
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models.ConfigMigration
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models.ConfigMigrationStep
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models.NamedConfigMigration
import io.pixeloutlaw.minecraft.spigot.bandsaw.JulLoggerFactory
import io.pixeloutlaw.minecraft.spigot.config.FileAwareYamlConfiguration
import io.pixeloutlaw.minecraft.spigot.config.VersionedFileAwareYamlConfiguration
import java.io.File
import java.nio.file.Path
import java.util.logging.Level

/**
 * Migrates Spigot YAML configurations across versions.
 *
 * @property dataFolder data folder for a plugin
 * @property backupOnMigrate should a backup file be created when migrating?
 */
abstract class ConfigMigrator @JvmOverloads constructor(
    private val dataFolder: File,
    private val backupOnMigrate: Boolean = true
) {
    private companion object {
        val logger by lazy { JulLoggerFactory.getLogger(ConfigMigrator::class) }
    }

    /**
     * Migrations that this instance will run through in the given order.
     */
    abstract val namedConfigMigrations: List<NamedConfigMigration>

    /**
     * Attempts to run all of the migrations that it is aware of. Will not copy configurations from resources
     * into [dataFolder]. Iterates through [ConfigMigration] sorted by name from [namedConfigMigrations] in order,
     * finding any files that match [ConfigMigration.fileGlobs] with a matching [ConfigMigration.fromVersion], and
     * running applicable [ConfigMigrationStep]s.
     */
    fun migrate() {
        logger.info("Beginning migration process")
        val sortedNamedConfigMigrations = namedConfigMigrations.sortedBy {
            it.migrationName.substring(it.migrationName.indexOf("V"), it.migrationName.indexOf("__")).substring(1)
                .toInt()
        }
        for (namedConfigMigration in sortedNamedConfigMigrations) {
            runMigration(namedConfigMigration)
        }
        logger.info("Finished migration process!")
    }

    fun writeYamlFromResourcesIfNotExists(resource: String) {
        val targetFile = dataFolder.toPath().resolve(resource).toFile()
        if (targetFile.exists() || !targetFile.parentFile.exists() && !targetFile.parentFile.mkdirs()) {
            return
        }
        try {
            val text = javaClass.classLoader?.getResource(resource)?.readText()
            if (text != null) {
                FileAwareYamlConfiguration(targetFile).apply {
                    loadFromString(text)
                    save()
                }
            } else {
                logger.log(Level.WARNING, "Unable to write resource because text was unreadable: $resource")
            }
        } catch (inevitable: Throwable) {
            logger.log(Level.WARNING, "Unable to write resource: $resource", inevitable)
            return
        }
    }

    // suppress spread operator warning because Glob has no other interface we can use
    @Suppress("SpreadOperator")
    private fun runMigration(namedConfigMigration: NamedConfigMigration) {
        logger.fine("> Running migration: ${namedConfigMigration.migrationName}")
        val configMigration = namedConfigMigration.configMigration
        logger.fine("=> Looking for files in dataFolder matching ${configMigration.fileGlobs}")
        val matchingPaths =
            Glob.from(*configMigration.fileGlobs.toTypedArray()).iterate(dataFolder.toPath()).asSequence().toList()
        logger.fine("=> Found matching files: ${matchingPaths.joinToString(", ")}")
        logger.fine("=> Loading matched files to check their versions")
        val yamlConfigurations = matchingPaths.map {
            val pathToFile = it.toFile()
            VersionedFileAwareYamlConfiguration(
                pathToFile
            )
        }.filter {
            logger.finest(
                """
                    ==> Checking if ${it.fileName} (${it.version}) has a version matching ${configMigration.fromVersion}
                """.trimLog()
            )
            it.version == configMigration.fromVersion
        }
        logger.fine("=> Found configurations matching versions: ${yamlConfigurations.map { it.fileName }}")
        logger.fine("=> Running migration over matching configurations")
        runMigrationOverConfigurations(yamlConfigurations, configMigration)
        logger.fine("=> Finished running migration over matching configurations!")
        logger.fine("> Finished running migration!")
    }

    private fun runMigrationOverConfigurations(
        yamlConfigurations: List<VersionedFileAwareYamlConfiguration>,
        configMigration: ConfigMigration
    ) {
        for (yamlConfiguration in yamlConfigurations) {
            handleBackups(configMigration, yamlConfiguration)
            if (handleOverwrites(configMigration, yamlConfiguration, dataFolder.toPath())) {
                logger.finest(
                    """
                    |=> Canceling migration for ${yamlConfiguration.fileName} as it has been overwritten!
                    """.trimLog()
                )
                continue
            }
            logger.finest("==> Running migration steps over ${yamlConfiguration.fileName}")
            for (step in configMigration.configMigrationSteps) {
                step.migrate(yamlConfiguration)
            }
            logger.finest(
                """
                |==> Setting configuration version to target version:
                |configuration=${yamlConfiguration.fileName} version=${configMigration.toVersion}
                """.trimLog()
            )
            yamlConfiguration.version = configMigration.toVersion
            logger.finest("==> Saving configuration")
            yamlConfiguration.save()
            logger.finest("==> Finished running migration steps!")
        }
    }

    private fun handleBackups(
        configMigration: ConfigMigration,
        yamlConfiguration: VersionedFileAwareYamlConfiguration
    ) {
        if (configMigration.createBackup && backupOnMigrate) {
            val lastDot = yamlConfiguration.fileName.lastIndexOf(".")
            val backupFilename = yamlConfiguration.fileName.substring(
                0,
                lastDot
            ) + "_${
            yamlConfiguration.version.toString().replace(
                ".",
                "_"
            )
            }" + yamlConfiguration.fileName.substring(lastDot) + ".backup"
            logger.fine("==> Creating backup of file as $backupFilename")
            try {
                yamlConfiguration.file?.let {
                    it.copyTo(File(it.parentFile, backupFilename), overwrite = true)
                }
            } catch (inevitable: Exception) {
                logger.log(Level.SEVERE, "Unable to create a backup of a config!", inevitable)
            }
        }
    }

    // if this returns true, we should skip the migration as we're just overwriting the existing file
    private fun handleOverwrites(
        configMigration: ConfigMigration,
        yamlConfiguration: VersionedFileAwareYamlConfiguration,
        pathToDataFolder: Path
    ): Boolean {
        val pathToYamlFile = yamlConfiguration.file?.toPath()?.toAbsolutePath()?.normalize()
        if (!configMigration.overwrite || pathToYamlFile == null) {
            return false
        }
        val normalizedPathToDataFolder = pathToDataFolder.toAbsolutePath().normalize()
        val relativizedPathToYamlFile = normalizedPathToDataFolder.relativize(pathToYamlFile)
        val pathToResource = relativizedPathToYamlFile.toString().replace("\\", "/")
        try {
            val resourceContents = javaClass.classLoader?.getResource(pathToResource)?.readText() ?: ""
            yamlConfiguration.file?.writeText(resourceContents)
            logger.fine(
                """
                |==> Overwrote contents of ${yamlConfiguration.fileName} with
                |contents of (hopefully) same file from plugin!
                """.trimLog()
            )
        } catch (inevitable: Exception) {
            logger.log(Level.SEVERE, "Unable to overwrite a config!", inevitable)
        }
        return true
    }

    /**
     * Trims the margins from the string and replaces newlines with a space.
     */
    private fun String.trimLog(): String {
        return this.trimMargin().replace("\n", " ")
    }
}
