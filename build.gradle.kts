plugins {
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.kotlin.gradle).apply(false)
    alias(libs.plugins.hilt.gradle).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
