plugins {
    id("modstitch.gradle-plugin-conventions")
}

dependencies {
    // For ConvertAccessxTask wiring on FMJ/NMT specs.
    api(project(":modstitch-accessx"))
}

gradlePlugin {
    plugins {
        register("manifests") {
            id = "dev.isxander.mtk.manifests"
            implementationClass = "dev.isxander.mtk.manifests.plugin.ModstitchManifestsPlugin"
            displayName = "MTK: Manifests"
            description = "Mod manifest generation across loaders."
            tags = listOf("modstitch", "minecraft", "manifest")
        }
    }
}
