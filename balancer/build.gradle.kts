plugins {
    alias(libs.plugins.kotlinJvm)
    application
}

dependencies {
    implementation(project(":engine"))
    testImplementation(libs.kotlin.test)
}

application {
    mainClass.set("com.shadowings.emberdoors.balancer.MainKt")
}
