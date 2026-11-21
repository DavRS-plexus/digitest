plugins {
    id("config.kotlin")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    // Kotlin
    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
}
