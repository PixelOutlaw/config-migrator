plugins {
    kotlin("jvm")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:_")

    api(project(":config-migrator-config"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:_")
    implementation("com.github.shyiko.klob:klob:_")
    implementation(platform("io.pixeloutlaw.spigot-commons:spigot-commons-bom:_"))
    implementation("io.pixeloutlaw.spigot-commons:bandsaw")

    testImplementation("org.spigotmc:spigot-api:_")
    testImplementation(platform("org.junit:junit-bom:_"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:_")
    testImplementation("io.mockk:mockk:_")
}
