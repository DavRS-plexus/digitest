plugins {
    id("com.android.library")
    id("config.android")
    id("io.insert-koin.compiler.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    // Koin
    implementation(libs.findLibrary("koin-android").get())

    // Navigation
    implementation(libs.findLibrary("androidx-navigation-compose").get())
    implementation(libs.findLibrary("kotlinx-serialization-json").get())

    // Unit testing
    testRuntimeOnly(libs.findLibrary("junit-platform-launcher").get())
    testImplementation(libs.findBundle("tests").get())
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        events("started", "passed", "skipped", "failed")
    }
}
