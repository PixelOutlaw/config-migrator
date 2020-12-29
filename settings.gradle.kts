import de.fayard.refreshVersions.bootstrapRefreshVersions

buildscript {
    repositories { gradlePluginPortal() }
    dependencies.classpath("de.fayard.refreshVersions:refreshVersions:0.9.7")
}

bootstrapRefreshVersions()

gradle.allprojects {
    group = "io.pixeloutlaw.minecraft.spigot"

    repositories {
        jcenter()
        maven {
            url = uri("https://dl.bintray.com/pixeloutlaw/pixeloutlaw-jars")
        }
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
        }
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots")
        }
    }
}

rootProject.name = "config-migrator"

include(
    "config-migrator-config",
    "config-migrator",
    "config-migrator-moshi"
)
