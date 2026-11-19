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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.10")
}