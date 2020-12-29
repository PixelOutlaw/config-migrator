plugins {
    kotlin("jvm")
}

dependencies {
    api("com.squareup.moshi:moshi:_")
    api(project(":config-migrator"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:_")
    implementation("com.squareup.moshi:moshi-adapters:_")

    testImplementation("org.spigotmc:spigot-api:_")
    testImplementation(platform("org.junit:junit-bom:_"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:_")
    testImplementation("io.mockk:mockk:_")
    testImplementation("dev.zacsweers.moshix:moshi-metadata-reflect:_")
}
