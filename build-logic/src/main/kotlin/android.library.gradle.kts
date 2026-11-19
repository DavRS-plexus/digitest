plugins {
    id("com.android.library")
    id("android.config")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("androidx-annotation-jvm").get())
}
