buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Deps.kotlinGradlePlugin)
        classpath(Deps.sqlDelightGradlePlugin)
        classpath(Deps.hiltGradlePlugin)
        classpath(Deps.androidGradle)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}