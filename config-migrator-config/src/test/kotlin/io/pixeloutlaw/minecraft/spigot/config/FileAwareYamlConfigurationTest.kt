package io.pixeloutlaw.minecraft.spigot.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

internal class FileAwareYamlConfigurationTest {
    @Test
    fun `does load do nothing when no file is given`() {
        // given
        val configuration = FileAwareYamlConfiguration()

        // when
        configuration.load()

        // then
        assertThat(configuration.getKeys(true)).hasSize(0)
    }

    @Test
    fun `does load load contents when file is given`(@TempDir tempDir: Path) {
        // given
        val tempFile = tempDir.resolve("test.yml")
        val configuration = FileAwareYamlConfiguration(tempFile.toFile())

        // when
        tempFile.toFile().writeText("test: 1")
        configuration.load()

        // then
        assertThat(configuration.getKeys(true)).hasSize(1)
    }

    @Test
    fun `does save do nothing when no file is given`(@TempDir tempDir: Path) {
        // given
        val configuration = FileAwareYamlConfiguration()

        // when
        configuration.save()

        // then
        assertThat(tempDir.toFile().list()).isNotNull().isEmpty()
    }

    @Test
    fun `does save save contents when file is given`(@TempDir tempDir: Path) {
        // given
        val tempFile = tempDir.resolve("test.yml")
        val configuration = FileAwareYamlConfiguration(tempFile.toFile())
        configuration.set("test", 1)

        // when
        configuration.save()

        // then
        assertThat(tempDir.toFile().list()).isNotNull().isNotEmpty()
        assertThat(tempFile.toFile().readText()).isEqualTo("test: 1\n")
    }
}
