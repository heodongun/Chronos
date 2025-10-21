plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.vanniktechMavenPublish)
}

group = "io.github.heodongun"
version = "1.0.1"

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)
    testImplementation(kotlin("test"))
}

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates("io.github.heodongun", "jsonrepair", "1.0.1")

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
