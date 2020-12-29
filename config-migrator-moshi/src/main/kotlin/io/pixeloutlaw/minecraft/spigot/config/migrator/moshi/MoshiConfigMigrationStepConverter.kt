package io.pixeloutlaw.minecraft.spigot.config.migrator.moshi

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.converters.ConfigMigrationConverter
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models.ConfigMigration
import java.io.IOException

/**
 * Implementation of [ConfigMigrationConverter] that uses a [Moshi] instance to parse JSON into
 * [ConfigMigration]s.
 */
class MoshiConfigMigrationStepConverter(private val moshi: Moshi) : ConfigMigrationConverter {
    override fun convertToConfigMigration(rawString: String): ConfigMigration? {
        return try {
            moshi.adapter(ConfigMigration::class.java).fromJson(rawString)
        } catch (ioe: IOException) {
            null // thrown if the raw string cannot be parsed at all
        } catch (iae: IllegalArgumentException) {
            null // thrown if an adapter can't be found
        } catch (jde: JsonDataException) {
            null // thrown if given invalid JSON
        }
    }
}
