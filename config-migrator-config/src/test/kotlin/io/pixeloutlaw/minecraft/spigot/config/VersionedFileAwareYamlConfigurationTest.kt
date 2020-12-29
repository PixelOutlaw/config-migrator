package io.pixeloutlaw.minecraft.spigot.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

internal class VersionedFileAwareYamlConfigurationTest {
    @Test
    fun `does getting version return default value when no version field exists`() {
        // given
        val configuration = VersionedFileAwareYamlConfiguration()

        // when
        configuration.load()

        // then
        assertThat(configuration.version).isEqualTo(SemVer(0, 0, 0))
    }

    @Test
    fun `does getting version read from file`(@TempDir tempDir: Path) {
        // given
        val tempFile = tempDir.resolve("test.yml")
        val configuration = VersionedFileAwareYamlConfiguration(tempFile.toFile())

        // when
        tempFile.toFile().writeText("version: 1")
        configuration.load()

        // then
        assertThat(configuration.version).isEqualTo(SemVer(1, 0, 0))
    }

    @Test
    fun `does setting version set the version field in the Configuration map`() {
        // given
        val configuration = VersionedFileAwareYamlConfiguration()

        // when
        configuration.version = SemVer(1, 0, 0)

        // then
        assertThat(configuration["version"]).isEqualTo("1.0.0")
    }
}
