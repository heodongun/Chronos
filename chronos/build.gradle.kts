plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.vanniktechMavenPublish)
}

group = "io.github.heodongun"
version = "1.0.0"

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)
    testImplementation(kotlin("test"))
}

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates("io.github.heodongun", "chronos", "1.0.0")

    pom {
        name.set("Chronos")
        description.set("A Kotlin DateTime library inspired by Python's Pendulum with comprehensive timezone support, DSL builders, and Kotlin idioms")
        url.set("https://github.com/heodongun/Chronos")

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
            connection.set("scm:git:git://github.com/heodongun/Chronos.git")
            developerConnection.set("scm:git:ssh://github.com/heodongun/Chronos.git")
            url.set("https://github.com/heodongun/Chronos")
        }
    }
}
