package io.pixeloutlaw.minecraft.spigot.config.migrator.moshi

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.converters.ConfigMigrationConverter
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models.ConfigMigration
import io.pixeloutlaw.minecraft.spigot.bandsaw.JulLoggerFactory
import java.io.IOException
import java.util.logging.Level

/**
 * Implementation of [ConfigMigrationConverter] that uses a [Moshi] instance to parse JSON into
 * [ConfigMigration]s.
 */
class MoshiConfigMigrationStepConverter(private val moshi: Moshi) : ConfigMigrationConverter {
    private companion object {
        val logger = JulLoggerFactory.getLogger(MoshiConfigMigrationStepConverter::class)
    }

    override fun convertToConfigMigration(rawString: String): ConfigMigration? {
        return try {
            moshi.adapter(ConfigMigration::class.java).fromJson(rawString)
        } catch (ioe: IOException) {
            logger.log(Level.WARNING, "Unable to open file", ioe)
            null // thrown if the raw string cannot be parsed at all
        } catch (iae: IllegalArgumentException) {
            logger.log(Level.WARNING, "Unable to find adapter for ConfigMigration class", iae)
            null // thrown if an adapter can't be found
        } catch (jde: JsonDataException) {
            logger.log(Level.WARNING, "Unable to parse raw file contents into JSON", jde)
            null // thrown if given invalid JSON
        }
    }
}
