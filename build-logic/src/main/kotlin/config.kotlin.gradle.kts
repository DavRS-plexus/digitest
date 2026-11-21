import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    // Testing dependencies
    testImplementation(libs.findLibrary("junit-jupiter").get())
    testImplementation(libs.findLibrary("mockk").get())
    testImplementation(libs.findLibrary("kotlin-test").get())
    testImplementation(libs.findLibrary("kotlinx-coroutines-test").get())
}
