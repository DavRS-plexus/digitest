plugins {
    id("com.android.library")
    id("config.android")
    id("config.compose")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {

    // Koin
    implementation(libs.findLibrary("koin-androidx-compose").get())

    // Compose
    implementation(platform(libs.findLibrary("androidx-compose-bom").get()))
    implementation(libs.findBundle("compose").get())
    debugImplementation(libs.findLibrary("androidx-compose-ui-tooling").get())
}
