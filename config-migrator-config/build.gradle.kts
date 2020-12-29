plugins {
    kotlin("jvm")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:_")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:_")

    testImplementation("org.spigotmc:spigot-api:_")
    testImplementation(platform("org.junit:junit-bom:_"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:_")
    testImplementation("io.mockk:mockk:_")
}
