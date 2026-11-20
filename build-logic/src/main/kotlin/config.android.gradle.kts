import com.android.build.api.dsl.CommonExtension

fun configureAndroid() {
    extensions.configure<CommonExtension> {
        compileSdk = 37
        compileSdkMinor = 1
        defaultConfig.minSdk = 28
        defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // TODO: review
        buildFeatures.buildConfig = true
    }
}

pluginManager.withPlugin("com.android.application") {
    configureAndroid()
}

pluginManager.withPlugin("com.android.library") {
    configureAndroid()
}
