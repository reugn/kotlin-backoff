import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.*

plugins {
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.serialization") version "1.3.50"
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
}

group = "com.github.reugn"
version = "0.1.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
    useJUnitPlatform()
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

val url = "https://github.com/reugn/kotlin-backoff"
publishing {
    publications {
        create<MavenPublication>("kotlin-backoff") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
            artifact(sourcesJar)

            pom.withXml {
                asNode().apply {
                    appendNode("description", url)
                    appendNode("name", rootProject.name)
                    appendNode("url", url)
                    appendNode("licenses").appendNode("license").apply {
                        appendNode("name", url)
                        appendNode("url", url)
                        appendNode("distribution", "repo")
                    }
                    appendNode("developers").appendNode("developer").apply {
                        appendNode("id", "reugn")
                        appendNode("name", "Reugn")
                    }
                    appendNode("scm").apply {
                        appendNode("url", url)
                    }
                }
            }
        }
    }
}

val prop = Properties()
prop.load(FileInputStream("local.properties"))
bintray {
    user = prop.getProperty("user").toString()
    key = prop.getProperty("password").toString()
    publish = true

    setPublications("kotlin-backoff")

    pkg.apply {
        repo = "maven"
        name = project.name
        userOrg = "reug"
        githubRepo = githubRepo
        vcsUrl = url
        description = "Simple Kotlin Exponential backoff library"
        setLicenses("Apache-2.0")
        desc = description
    }
}
