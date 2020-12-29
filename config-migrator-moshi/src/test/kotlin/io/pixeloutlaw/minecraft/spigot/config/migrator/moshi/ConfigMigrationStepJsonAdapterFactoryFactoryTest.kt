package io.pixeloutlaw.minecraft.spigot.config.migrator.moshi

import com.squareup.moshi.Moshi
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models.ConfigMigrationStep
import dev.zacsweers.moshix.reflect.MetadataKotlinJsonAdapterFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ConfigMigrationStepJsonAdapterFactoryFactoryTest {
    private val moshi =
        Moshi.Builder().add(SemVerAdapter).add(ConfigMigrationStepJsonAdapterFactoryFactory.create())
            .add(MetadataKotlinJsonAdapterFactory()).build()

    @Test
    fun `does ConfigMigrationStepJsonAdapterFactoryFactory allow parsing ConfigMigrationSteps`() {
        val jsonString = javaClass.getResource("/delete_config_migration_step.json").readText()
        val configMigrationStep = moshi.adapter(ConfigMigrationStep::class.java).fromJson(jsonString)
        assertThat(configMigrationStep).isInstanceOf(ConfigMigrationStep.DeleteConfigMigrationStep::class.java)
    }
}
