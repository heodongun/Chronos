plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
    `maven-publish`
    signing
}

group = "io.github.heodongun"
version = "1.0.0"

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)
    testImplementation(kotlin("test"))
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.heodongun"
            artifactId = "jsonrepair"
            version = "1.0.0"

            from(components["java"])

            pom {
                name.set("JSONRepair")
                description.set("A lightweight Kotlin library that repairs malformed JSON strings")
                url.set("https://github.com/heodongun/JSONRepair")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("heodongun")
                        name.set("Heo Dongun")
                        email.set("heodongun@gmail.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/heodongun/JSONRepair.git")
                    developerConnection.set("scm:git:ssh://github.com/heodongun/JSONRepair.git")
                    url.set("https://github.com/heodongun/JSONRepair")
                }
            }
        }
    }

    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.findProperty("mavenCentralUsername") as String? ?: ""
                password = project.findProperty("mavenCentralPassword") as String? ?: ""
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}
