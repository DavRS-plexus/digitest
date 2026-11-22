import gradle.kotlin.dsl.accessors._9cccbb759b7db166d79680b5f8744702.testImplementation
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
    testRuntimeOnly(libs.findLibrary("junit-platform-launcher").get())
    testImplementation(libs.findBundle("tests").get())
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        events("started", "passed", "skipped", "failed")
    }
}
