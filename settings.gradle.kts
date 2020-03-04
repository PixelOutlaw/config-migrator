rootProject.name = "config-migrator"

gradle.allprojects {
    group = "io.pixeloutlaw.minecraft.spigot"

    repositories {
        mavenCentral()
        jcenter()
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
