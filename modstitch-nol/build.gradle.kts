plugins {
    id("modstitch.gradle-plugin-conventions")
}

gradlePlugin {
    plugins {
        register("nol") {
            id = "dev.isxander.mtk.neoforge-on-loom"
            implementationClass = "dev.isxander.mtk.nol.plugin.ModstitchNolPlugin"
            displayName = "MTK: NeoForge on Loom (NoL)"
            description = "This plugin helps you develop NeoForge mods on Fabric Loom."
            tags = listOf("modstitch", "minecraft", "neoforge", "fabric-loom")
        }
    }
}
