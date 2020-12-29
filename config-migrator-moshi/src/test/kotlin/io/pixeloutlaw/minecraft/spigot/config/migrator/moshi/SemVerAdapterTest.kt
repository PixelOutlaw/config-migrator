package io.pixeloutlaw.minecraft.spigot.config.migrator.moshi

import io.pixeloutlaw.minecraft.spigot.config.SemVer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

internal class SemVerAdapterTest {
    @Test
    fun `does fromJson return SemVer instance for SemVer value`() {
        // given
        val toParse = "0.0.1"

        // when
        val parsed = SemVerAdapter.fromJson(toParse)

        // then
        assertThat(parsed).isEqualTo(SemVer(0, 0, 1))
    }

    @Test
    fun `does fromJson return SemVer instance for SemVer value with no patch`() {
        // given
        val toParse = "0.1"

        // when
        val parsed = SemVerAdapter.fromJson(toParse)

        // then
        assertThat(parsed).isEqualTo(SemVer(0, 1, 0))
    }

    @Test
    fun `does fromJson return SemVer instance for SemVer value with no minor`() {
        // given
        val toParse = "1"

        // when
        val parsed = SemVerAdapter.fromJson(toParse)

        // then
        assertThat(parsed).isEqualTo(SemVer(1, 0, 0))
    }

    @Test
    fun `does fromJson throw exception for non-SemVer value`() {
        // given
        val toParse = "0.0.0.0.0.0.0.1"

        // then
        assertThatThrownBy {
            // when
            SemVerAdapter.fromJson(toParse)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
