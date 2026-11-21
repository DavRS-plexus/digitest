plugins {
    id("com.android.library")
    id("config.android")
    id("config.compose")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
//    implementation(libs.findLibrary("androidx-annotation-jvm").get())

    // Koin
    implementation(libs.findLibrary("koin-androidx-compose").get())

    // Compose
    implementation(platform(libs.findLibrary("androidx-compose-bom").get()))
    implementation(libs.findLibrary("androidx-compose-material-icons-core").get())
    implementation(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
    implementation(libs.findLibrary("androidx-compose-material3").get())
    debugImplementation(libs.findLibrary("androidx-compose-ui-tooling").get())
}
