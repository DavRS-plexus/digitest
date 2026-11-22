import gradle.kotlin.dsl.accessors._9cccbb759b7db166d79680b5f8744702.androidTestImplementation
import gradle.kotlin.dsl.accessors._9cccbb759b7db166d79680b5f8744702.debugImplementation

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

    // UI testing dependencies
    androidTestImplementation(platform(libs.findLibrary("androidx-compose-bom").get()))
    androidTestImplementation(libs.findLibrary("androidx-compose-ui-test-junit4").get())
    androidTestImplementation(libs.findLibrary("androidx-test-ext-junit").get())
    androidTestImplementation(libs.findLibrary("androidx-test-runner").get())
    debugImplementation(libs.findLibrary("androidx-compose-ui-test-manifest").get())
}
