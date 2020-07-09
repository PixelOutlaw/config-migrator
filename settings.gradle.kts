rootProject.name = "config-migrator"

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

include(
    "config-migrator"
)
