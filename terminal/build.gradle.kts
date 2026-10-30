plugins {
    alias(libs.plugins.kotlinJvm)
    application
}

dependencies {
    implementation(project(":engine"))
    testImplementation(libs.kotlin.test)
}

application {
    mainClass.set("com.shadowings.emberdoors.terminal.MainKt")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
