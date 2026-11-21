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

    // Testing dependencies
    testImplementation(libs.findLibrary("junit-jupiter").get())
    testImplementation(libs.findLibrary("mockk").get())
    testImplementation(libs.findLibrary("kotlin-test").get())
    testImplementation(libs.findLibrary("kotlinx-coroutines-test").get())
//    testImplementation(libs.findLibrary("androidx-arch-core-testing").get())
}
