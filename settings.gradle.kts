pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        register("libs")
    }
}

rootProject.name = "modstitch-toolkit"

include("modstitch-accessx")
include("modstitch-manifests")
include("modstitch-nol")
include("modstitch-fapidep")
include("modstitch-commonconf")
include("modstitch-modrepos")
include("modstitch-propapply")
