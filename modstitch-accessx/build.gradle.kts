plugins {
    id("modstitch.gradle-plugin-conventions")
}

gradlePlugin {
    plugins {
        register("accessx") {
            id = "dev.isxander.mtk.accessx"
            implementationClass = "dev.isxander.mtk.accessx.plugin.ModstitchAccessxPlugin"
            displayName = "MTK: Accessx"
            description = "Access widener/transformer conversion between Fabric and Forge formats."
            tags = listOf("modstitch", "minecraft", "access-widener", "access-transformer")
        }
    }
}
