plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
//    gradlePluginPortal()
}

dependencies {
    implementation(libs.gradle)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.compose.compiler.gradle.plugin)
}