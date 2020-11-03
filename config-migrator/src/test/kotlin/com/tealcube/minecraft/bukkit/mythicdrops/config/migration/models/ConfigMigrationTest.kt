package com.tealcube.minecraft.bukkit.mythicdrops.config.migration.models

import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.SemVer
import com.tealcube.minecraft.bukkit.mythicdrops.config.migration.adapters.SemVerAdapter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ConfigMigrationTest {
    private val moshi = Moshi.Builder().add(SemVerAdapter).add(ConfigMigrationStep.adapterFactory).build()

    @Test
    fun `can migration with no steps be parsed correctly`() {
        val jsonString = javaClass.getResource("/config_migration_no_steps.json").readText()
        val configMigration = moshi.adapter(ConfigMigration::class.java).fromJson(jsonString)
        if (configMigration == null) {
            Assertions.fail<Unit>("configMigration == null")
            return // appeasing the compiler, will never be run
        }
        assertThat(configMigration.fileGlobs).isEqualTo(listOf("config.yml"))
        assertThat(configMigration.fromVersion).isEqualTo(SemVer.parse("1.2.3"))
        assertThat(configMigration.toVersion).isEqualTo(SemVer.parse("4.5.6"))
        assertThat(configMigration.configMigrationSteps).isEmpty()
    }

    @Test
    fun `can migration with multiple steps be parsed correctly`() {
        val jsonString = javaClass.getResource("/config_migration_multiple_steps.json").readText()
        val configMigration = moshi.adapter(ConfigMigration::class.java).fromJson(jsonString)
        if (configMigration == null) {
            Assertions.fail<Unit>("configMigration == null")
            return // appeasing the compiler, will never be run
        }
        assertThat(configMigration.fileGlobs).isEqualTo(listOf("config.yml"))
        assertThat(configMigration.fromVersion).isEqualTo(SemVer.parse("1.2.3"))
        assertThat(configMigration.toVersion).isEqualTo(SemVer.parse("4.5.6"))
        assertThat(configMigration.configMigrationSteps).isNotEmpty()
        assertThat(configMigration.configMigrationSteps).hasSize(3)
        assertThat(configMigration.configMigrationSteps.any { it is RenameConfigMigrationStep }).isTrue()
        assertThat(configMigration.configMigrationSteps.any { it is DeleteConfigMigrationStep }).isTrue()
        assertThat(configMigration.configMigrationSteps.any { it is SetStringConfigMigrationStep }).isTrue()
    }
}
