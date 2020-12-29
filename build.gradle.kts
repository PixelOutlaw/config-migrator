plugins {
    base
    kotlin("jvm") apply false
    id("io.pixeloutlaw.multi")
    id("nebula.nebula-bintray")
    id("nebula.release")
}

description = "config-migrator"

subprojects {
    this@subprojects.version = rootProject.version
}

bintray {
    pkgName.set("config-migrator")
    repo.set("pixeloutlaw-jars")
    userOrg.set("pixeloutlaw")
    syncToMavenCentral.set(false)
}

tasks.withType<Wrapper> {
    gradleVersion = "6.7.1"
    distributionType = Wrapper.DistributionType.ALL
}
