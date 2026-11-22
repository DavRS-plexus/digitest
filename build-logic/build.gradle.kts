plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.gradle)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlin.serialization.gradle.plugin)
    implementation(libs.kotlin.compose.compiler.gradle.plugin)
    implementation(libs.koin.compiler.gradle.plugin)
}