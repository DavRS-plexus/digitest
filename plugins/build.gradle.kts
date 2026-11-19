plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:9.5.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.10")
    implementation("org.jetbrains.kotlin:kotlin-serialization:2.2.10")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.57.1")
    implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.2.10")
}