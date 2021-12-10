import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `java-library`
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    `maven-publish`
    signing
}

group = "io.github.reugn"

repositories {
    mavenCentral()
}

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:1.0-M1-1.4.0-rc")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenCentral") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
            pom {
                name.set(project.name)
                description.set("An exponential backoff library for Kotlin.")
                url.set("https://github.com/reugn/kotlin-backoff")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("reugn")
                        name.set("reugn")
                        email.set("reugpro@gmail.com")
                        url.set("https://github.com/reugn")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/reugn/kotlin-backoff.git")
                    developerConnection.set("scm:git:ssh://github.com/reugn/kotlin-backoff.git")
                    url.set("https://github.com/reugn/kotlin-backoff")
                }
            }
        }
    }
    repositories {
        maven {
            name = "mavenCentral"
            credentials(PasswordCredentials::class)
            val nexus = "https://s01.oss.sonatype.org/"
            val releasesRepoUrl = uri(nexus + "service/local/staging/deploy/maven2")
            val snapshotsRepoUrl = uri(nexus + "content/repositories/snapshots")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
    }
}

signing {
    sign(publishing.publications["mavenCentral"])
    useGpgCmd()
    sign(configurations.archives.get())
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
