plugins {
    id("kotlin.library")
    id("io.insert-koin.compiler.plugin")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    // Annotation (Keep, for example)
    implementation(libs.findLibrary("androidx-annotation-jvm").get())

    // Retrofit
    implementation(libs.findLibrary("retrofit").get())
    implementation(libs.findLibrary("gson").get())
    implementation(libs.findLibrary("retrofit-converter-gson").get())

    // Koin
    implementation(platform(libs.findLibrary("koin-bom").get()))
    implementation(libs.findLibrary("koin-core").get())
}
