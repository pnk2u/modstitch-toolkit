plugins {
    id("modstitch.gradle-plugin-conventions")
}

gradlePlugin {
    plugins {
        register("modrepos") {
            id = "dev.isxander.mtk.modrepos"
            implementationClass = "dev.isxander.mtk.modrepos.ModreposPlugin"
            displayName = "MTK: Modrepos"
            description = "Provides shorthands to many common minecraft-specific repositories."
            tags = listOf("modstitch-toolkit", "minecraft")
        }
    }
}
