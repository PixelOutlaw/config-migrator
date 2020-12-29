/*
 * This file is part of MythicDrops, licensed under the MIT License.
 *
 * Copyright (C) 2019 Richard Harrah
 *
 * Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tealcube.minecraft.bukkit.mythicdrops.config.migration.migrators

import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.ConfigMigrator
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.converters.ConfigMigrationConverter
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models.NamedConfigMigration
import io.pixeloutlaw.minecraft.spigot.bandsaw.JulLoggerFactory
import java.io.File
import java.io.IOException
import java.util.logging.Level
import java.util.zip.ZipException
import java.util.zip.ZipFile

/**
 * Implementation of [ConfigMigrator] that loads files from a specified [jarFile] using a given regex pattern. These
 * files will be loaded and passed into a [ConfigMigrationConverter].
 *
 * Defaults to using [DEFAULT_CONFIG_MIGRATION_PATTERN] as the regex pattern.
 */
class JarConfigMigrator @JvmOverloads constructor(
    private val jarFile: File,
    dataFolder: File,
    private val configMigrationConverter: ConfigMigrationConverter,
    private val configMigrationPatternString: String = DEFAULT_CONFIG_MIGRATION_PATTERN,
    backupOnMigrate: Boolean = true
) : ConfigMigrator(dataFolder, backupOnMigrate) {
    companion object {
        /**
         * Default regex pattern for loading configuration files out of a JAR.
         */
        const val DEFAULT_CONFIG_MIGRATION_PATTERN = "config/migration/V\\d+__.+\\.migration\\.json"
        private val logger by lazy { JulLoggerFactory.getLogger(JarConfigMigrator::class) }
    }

    private val cachedConfigMigrationResources: List<String> by lazy {
        val zipFile = try {
            ZipFile(jarFile)
        } catch (zipException: ZipException) {
            return@lazy emptyList<String>()
        } catch (ioException: IOException) {
            return@lazy emptyList<String>()
        }
        val configMigrationRegex = configMigrationPatternString.toRegex()
        val retValue = mutableListOf<String>()
        zipFile.use {
            val entries = zipFile.entries()
            while (entries.hasMoreElements()) {
                val zipEntry = entries.nextElement()
                val zipEntryName = zipEntry.name
                if (configMigrationRegex.matches(zipEntryName)) {
                    retValue.add(zipEntryName)
                }
            }
        }
        logger.fine("Migrations: $retValue")
        retValue.toList().sortedBy {
            it.substring(it.indexOf("V"), it.indexOf("__")).substring(1).toInt()
        }
    }
    private val cachedConfigMigrationContents: Map<String, String> by lazy {
        cachedConfigMigrationResources.mapNotNull {
            try {
                javaClass.classLoader?.getResource(it)?.let { resource ->
                    it to resource.readText()
                }
            } catch (inevitable: Throwable) {
                logger.log(Level.WARNING, "Unable to load migration resource: $it", inevitable)
                null
            }
        }.toMap()
    }
    private val cachedNamedConfigMigrations: List<NamedConfigMigration> by lazy {
        cachedConfigMigrationContents.mapNotNull {
            val configMigration = configMigrationConverter.convertToConfigMigration(it.value)
            if (configMigration != null) {
                NamedConfigMigration(
                    it.key,
                    configMigration
                )
            } else {
                logger.log(Level.WARNING, "Resource was not a valid config migration: ${it.key}")
                null
            }
        }
    }

    override val namedConfigMigrations: List<NamedConfigMigration> = cachedNamedConfigMigrations
}
