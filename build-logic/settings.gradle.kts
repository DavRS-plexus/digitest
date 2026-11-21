dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("config/libs.versions.toml"))
        }
    }
}