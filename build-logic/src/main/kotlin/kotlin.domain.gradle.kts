plugins {
    id("kotlin.library")
    id("io.insert-koin.compiler.plugin")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    // Koin
    implementation(platform(libs.findLibrary("koin-bom").get()))
    implementation(libs.findLibrary("koin-core").get())
}
