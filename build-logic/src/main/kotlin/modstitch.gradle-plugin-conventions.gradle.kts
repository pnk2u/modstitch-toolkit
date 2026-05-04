import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("org.gradle.kotlin.kotlin-dsl")
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
}

val libs = the<LibrariesForLibs>()

group = "dev.isxander"
version = "0.1.0"

repositories {
    mavenCentral()
    gradlePluginPortal()
    exclusiveContent {
        forRepository { maven("https://maven.fabricmc.net") }
        filter {
            includeGroupAndSubgroups("net.fabricmc")
        }
    }
}

dependencies {

}

gradlePlugin {
    website = "https://github.com/isxander/modstitch2"
    vcsUrl = "https://github.com/isxander/modstitch2.git"
}
