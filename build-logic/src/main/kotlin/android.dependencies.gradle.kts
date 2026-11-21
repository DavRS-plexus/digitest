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
//    testRuntimeOnly(libs.findLibrary("junit-vintage-engine").get())
    testRuntimeOnly(libs.findLibrary("junit-platform-launcher").get())
    testImplementation(libs.findLibrary("junit-jupiter").get())
    testImplementation(libs.findLibrary("mockk").get())
//    testImplementation(libs.findLibrary("kotlin-test").get())
    testImplementation(libs.findLibrary("kotlinx-coroutines-test").get())
//    testImplementation(libs.findLibrary("androidx-arch-core-testing").get())
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        events("started", "passed", "skipped", "failed")
    }
}
