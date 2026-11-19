plugins {
    id("com.android.library")
    id("android.config")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("androidx-annotation-jvm").get())
    implementation(platform(libs.findLibrary("androidx-compose-bom").get()))
    implementation(libs.findLibrary("androidx-compose-ui").get())
    implementation(libs.findLibrary("androidx-compose-foundation").get())
    implementation(libs.findLibrary("androidx-compose-material3").get())
}
