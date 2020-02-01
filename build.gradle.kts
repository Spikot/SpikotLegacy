import java.net.URL

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.60"
    id("org.jetbrains.kotlin.kapt") version "1.3.60"
    id("org.jetbrains.dokka") version "0.10.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.3.60"
    id("maven-publish")
}

val kotlin_version = "1.3.60"
val coroutine_version = "1.3.0-M2"
val paper_version = "1.12.2-R0.1-SNAPSHOT"

group = "kr.heartpattern"
version = "4.0.0-SNAPSHOT"

repositories {
    maven("https://maven.heartpattern.kr/repository/maven-public/")
}

dependencies {
    // Kotlin family
    compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    compile("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutine_version")
    compile("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")

    // compile(dependencies)
    compile("io.github.microutils:kotlin-logging:1.5.4")
    compile("com.esotericsoftware.yamlbeans:yamlbeans:1.13")
    compile("net.swiftzer.semver:semver:1.1.1")
    compile("com.github.salomonbrys.kotson:kotson:2.5.0")

    // compile(only dependencies)
    compileOnly("com.google.auto.service:auto-service-annotations:1.0-rc6")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.4.0")
    compileOnly("com.destroystokyo.paper:paper-api:$paper_version")
    compileOnly("org.spigotmc:plugin-annotations:1.1.0-SNAPSHOT") {
        exclude("org.bukkit", "bukkit")
    }

    // Test
    testCompile("org.jetbrains.kotlin:kotlin-test:1.3.50")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.5.2")

    // KAPT
    kapt("com.google.auto.service:auto-service:1.0-rc6")
    kapt("org.spigotmc:plugin-annotations:1.1.0-SNAPSHOT")
    kapt("kr.heartpattern:SpikotAnnotationProcessor:4.0.0-SNAPSHOT")
    kapt("kr.heartpattern:SpikotClassLocator:4.0.0-SNAPSHOT")
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-XXLanguage:+InlineClasses",
                "-Xuse-experimental=kotlin.Experimental"
            )
        }
    }

    create<Jar>("createPlugin") {
        archiveFileName.set("Spikot-Plugin.jar")
        from(
            configurations.getByName("compile").map {
                if (it.isDirectory)
                    it
                else
                    zipTree(it)
            }
        )
        with(jar.get())
    }

    dokka {
        outputFormat = "html"
        outputDirectory = "./build/kdoc/"
        configuration {
            externalDocumentationLink {
                url = URL("https://hub.spigotmc.org/javadocs/spigot/")
                packageListUrl = URL("https://hub.spigotmc.org/javadocs/spigot/package-list")
            }
        }
    }

    create<Jar>("dokkaJar") {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        archiveFileName.set("Spikot-kdoc.zip")
        archiveClassifier.set("javadoc")
        from(dokka)
    }

    create<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
}

// Load local.gradle.kts if exists
if (File("local.gradle.kts").exists())
    apply("local.gradle.kts")

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "Spikot"
            from(components["java"])
            artifact(tasks["dokkaJar"])
            artifact(tasks["sourcesJar"])
        }
    }
    repositories {
        maven(
            if (version.toString().endsWith("SNAPSHOT"))
                "https://maven.heartpattern.kr/repository/maven-public-snapshots/"
            else
                "https://maven.heartpattern.kr/repository/maven-public-releases/"
        ) {
            credentials {
                username = properties["nexusUser"] as String
                password = properties["nexusPassword"] as String
            }
        }
    }
}