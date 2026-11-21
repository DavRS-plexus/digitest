plugins {
    id("com.android.library")
    id("config.android")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    // Koin
    implementation(libs.findLibrary("koin-android").get())

    // Navigation
    implementation(libs.findLibrary("androidx-navigation-compose").get())
    implementation(libs.findLibrary("kotlinx-serialization-json").get())
}
