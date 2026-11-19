pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

    // Shared build config
    includeBuild("build-logic")
}
// TODO: remove
//plugins {
//    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
//}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("build-logic/config/libs.versions.toml"))
        }
    }
}

rootProject.name = "Digitest"

//includeBuild("build-logic")

// App Module
include(":app")

// Core Modules
// ...

// Features Modules
// ...

// Libs Modules
include(":libs:android-utils")
include(":libs:kotlin-utils")
 