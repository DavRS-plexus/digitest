plugins {
    id("kotlin.config")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("androidx-annotation-jvm").get())
}
