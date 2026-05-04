plugins {
    id("modstitch.gradle-plugin-conventions")
}

gradlePlugin {
    plugins {
        register("propapply") {
            id = "dev.isxander.mtk.propapply"
            implementationClass = "dev.isxander.mtk.propapply.PropapplyPlugin"
            displayName = "MTK: Property Application"
            description = "Applies one of various plugins based on a gradle property."
            tags = listOf("modstitch", "minecraft")
        }
    }
}
