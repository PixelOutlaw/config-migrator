package io.pixeloutlaw.minecraft.spigot.config.migrator.moshi

import com.squareup.moshi.Moshi
import dev.zacsweers.moshix.reflect.MetadataKotlinJsonAdapterFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MoshiConfigMigrationStepConverterTest {
    private val moshi =
        Moshi.Builder().add(SemVerAdapter).add(ConfigMigrationStepJsonAdapterFactoryFactory.create())
            .add(MetadataKotlinJsonAdapterFactory()).build()

    private val moshiConfigMigrationStepConverter: MoshiConfigMigrationStepConverter =
        MoshiConfigMigrationStepConverter(moshi)

    @Test
    fun `does convertToConfigMigration return null for empty String`() {
        // given
        val rawString = ""

        // when
        val actual = moshiConfigMigrationStepConverter.convertToConfigMigration(rawString)

        // then
        assertThat(actual).isNull()
    }

    @Test
    fun `does convertToConfigMigration return null for invalid JSON`() {
        // given
        val rawString = "{}"

        // when
        val actual = moshiConfigMigrationStepConverter.convertToConfigMigration(rawString)

        // then
        assertThat(actual).isNull()
    }

    @Test
    fun `does convertToConfigMigration return null for Moshi instance with no adapters`() {
        // given
        val rawString = ""

        // when
        val actual = MoshiConfigMigrationStepConverter(Moshi.Builder().build()).convertToConfigMigration(rawString)

        // then
        assertThat(actual).isNull()
    }

    @Test
    fun `does convertToConfigMigration return non-null for valid ConfigMigration instance`() {
        // given
        val rawString = javaClass.getResource("/config_migration_multiple_steps.json").readText()

        // when
        val actual = moshiConfigMigrationStepConverter.convertToConfigMigration(rawString)

        // then
        assertThat(actual).isNotNull().hasFieldOrPropertyWithValue("fileGlobs", listOf("config.yml"))
    }
}
