plugins {
    id("modstitch.gradle-plugin-conventions")
}

dependencies {
    implementation(libs.plugins.fabric.loom.asDependency())
}

gradlePlugin {
    plugins {
        register("fapidep") {
            id = "dev.isxander.mtk.fapidep"
            implementationClass = "dev.isxander.mtk.fapidep.FapidepPlugin"
            displayName = "MTK: Fapidep"
            description = "Fabric API dependency helpers for Modstitch."
            tags = listOf("modstitch", "minecraft", "fabric")
        }
    }
}
