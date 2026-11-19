plugins {
    id("kotlin.library")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.14.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.14.3")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        events("started", "passed", "skipped", "failed")
    }
}