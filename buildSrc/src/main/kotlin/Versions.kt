import kotlin.String
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

/**
 * Generated by https://github.com/jmfayard/buildSrcVersions
 *
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version.
 */
object Versions {
    const val io_pixeloutlaw_spigot_commons: String = "1.16.1.7"

    const val org_jetbrains_kotlin: String = "1.3.72"

    const val com_squareup_moshi: String = "1.9.3"

    const val com_diffplug_gradle_spotless_gradle_plugin: String = "4.5.1"

    const val io_gitlab_arturbosch_detekt_gradle_plugin: String = "1.10.0"

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.7.0"

    const val org_jetbrains_kotlin_jvm_gradle_plugin: String = "1.3.72"

    const val nebula_nebula_bintray_gradle_plugin: String = "8.4.0"

    const val nebula_maven_publish_gradle_plugin: String = "17.3.0"

    const val org_jetbrains_dokka_gradle_plugin: String = "0.10.1"

    const val nebula_project_gradle_plugin: String = "7.0.9"

    const val nebula_release_gradle_plugin: String = "15.0.3"

    const val junit_jupiter: String = "5.6.2"

    const val mockito_core: String = "3.3.3"

    const val java_semver: String = "0.9.0"

    const val spigot_api: String = "1.16.1-R0.1-SNAPSHOT"

    const val junit_bom: String = "5.6.2"

    const val mockk: String = "1.10.0"

    const val truth: String = "1.0.1"

    const val klob: String = "0.2.1"

    /**
     * Current version: "6.5"
     * See issue 19: How to update Gradle itself?
     * https://github.com/jmfayard/buildSrcVersions/issues/19
     */
    const val gradleLatestVersion: String = "6.5.1"
}

/**
 * See issue #47: how to update buildSrcVersions itself
 * https://github.com/jmfayard/buildSrcVersions/issues/47
 */
val PluginDependenciesSpec.buildSrcVersions: PluginDependencySpec
    inline get() =
            id("de.fayard.buildSrcVersions").version(Versions.de_fayard_buildsrcversions_gradle_plugin)
