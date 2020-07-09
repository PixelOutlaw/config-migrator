plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    compileOnly(Libs.spigot_api)

    api(Libs.java_semver)
    api(Libs.moshi)
    api(Libs.moshi_adapters)
    api(Libs.klob)

    implementation(platform(Libs.spigot_commons_bom))
    implementation(Libs.kotlin_stdlib_jdk8)
    implementation(Libs.bandsaw)

    kapt(Libs.moshi_kotlin_codegen)
}
