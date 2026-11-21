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

// App Module
include(":app")

// Core Modules
include(":core:error:data")
include(":core:error:domain")
include(":core:navigation")
include(":core:network")
include(":core:resources")
include(":core:storage:data")
include(":core:theme")

// Features Modules
include(":feature:digivice:data")
include(":feature:digivice:domain")
include(":feature:digivice:ui")

// Libs Modules
include(":libs:android-utils")
include(":libs:kotlin-utils")
 