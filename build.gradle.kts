plugins {
    base
    buildSrcVersions
    kotlin("jvm") version Versions.org_jetbrains_kotlin_jvm_gradle_plugin apply false
    id("com.diffplug.gradle.spotless") version Versions.com_diffplug_gradle_spotless_gradle_plugin apply false
    id("io.gitlab.arturbosch.detekt") version Versions.io_gitlab_arturbosch_detekt_gradle_plugin apply false
    id("org.jetbrains.dokka") version Versions.org_jetbrains_dokka
    id("nebula.maven-publish") version Versions.nebula_maven_publish_gradle_plugin apply false
    id("nebula.nebula-bintray") version Versions.nebula_nebula_bintray_gradle_plugin
    id("nebula.project") version Versions.nebula_project_gradle_plugin apply false
    id("nebula.release") version Versions.nebula_release_gradle_plugin
}

subprojects {
    this@subprojects.version = rootProject.version
    pluginManager.withPlugin("java") {
        this@subprojects.pluginManager.apply(nebula.plugin.responsible.NebulaResponsiblePlugin::class.java)
        this@subprojects.pluginManager.apply(nebula.plugin.bintray.BintrayPlugin::class.java)
        this@subprojects.pluginManager.apply(com.diffplug.gradle.spotless.SpotlessPlugin::class.java)

        this@subprojects.tasks.withType<Test> {
            useJUnitPlatform()
            testLogging {
                events("passed", "skipped", "failed")
            }
        }

        this@subprojects.dependencies {
            "testImplementation"(Libs.spigot_api)
            "testImplementation"(Libs.mockito_core)
            "testImplementation"(Libs.truth)
            "testImplementation"(platform(Libs.junit_bom))
            "testImplementation"("org.junit.jupiter:junit-jupiter")
        }
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        this@subprojects.pluginManager.apply(io.gitlab.arturbosch.detekt.DetektPlugin::class.java)
        this@subprojects.pluginManager.apply(org.jetbrains.dokka.gradle.DokkaPlugin::class.java)
        this@subprojects.pluginManager.apply(com.diffplug.gradle.spotless.SpotlessPlugin::class.java)
        this@subprojects.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
            baseline = this@subprojects.file("baseline.xml")
        }
        this@subprojects.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
            kotlin {
                target("src/**/*.kt")
                ktlint()
                trimTrailingWhitespace()
                endWithNewline()
                if (this@subprojects.file("HEADER").exists()) {
                    licenseHeaderFile("HEADER")
                }
            }
        }
        this@subprojects.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            dependsOn("spotlessKotlinApply")
            kotlinOptions {
                javaParameters = true
                jvmTarget = "1.8"
            }
        }
        this@subprojects.tasks.getByName<Jar>("javadocJar") {
            dependsOn("dokkaJavadoc")
            from(tasks.getByName("dokkaJavadoc"))
        }

        this@subprojects.dependencies {
            "testImplementation"(Libs.kotlin_reflect)
            "testImplementation"(Libs.mockk)
        }
    }
}

bintray {
    pkgName.value("config-migrator")
    repo.value("pixeloutlaw-jars")
    userOrg.value("pixeloutlaw")
    syncToMavenCentral.value(false)
}

tasks.withType<Wrapper> {
    gradleVersion = Versions.gradleLatestVersion
    distributionType = Wrapper.DistributionType.ALL
}

tasks.findByName("release")?.finalizedBy("build")
