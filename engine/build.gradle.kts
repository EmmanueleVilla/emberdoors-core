plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

group = "com.shadowings.emberdoors"
version = "1.0.0"

kotlin {
    jvm()
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    )

    sourceSets {
        commonMain.dependencies {
            api(libs.redux.kotlin)
            implementation(libs.redux.kotlin.concurrent)
            implementation(libs.redux.kotlin.thunk)
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
