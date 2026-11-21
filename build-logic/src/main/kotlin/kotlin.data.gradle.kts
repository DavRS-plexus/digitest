plugins {
    id("kotlin.library")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    // Annotation (Keep, for example)
    implementation(libs.findLibrary("androidx-annotation-jvm").get())

    // Retrofit
    implementation(libs.findLibrary("retrofit").get())

    // Koin
    implementation(platform(libs.findLibrary("koin-bom").get()))
    implementation(libs.findLibrary("koin-core").get())
}
