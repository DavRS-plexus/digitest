import com.android.build.api.dsl.CommonExtension
import org.gradle.kotlin.dsl.configure

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

fun configureCompose() {
    extensions.configure<CommonExtension> {
        buildFeatures.compose = true
    }
}

pluginManager.withPlugin("com.android.library") {
    configureCompose()
}

pluginManager.withPlugin("com.android.application") {
    configureCompose()
}
