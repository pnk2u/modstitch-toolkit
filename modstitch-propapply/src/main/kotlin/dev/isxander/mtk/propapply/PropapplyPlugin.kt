package dev.isxander.mtk.propapply

import org.gradle.api.Plugin
import org.gradle.api.Project

class PropapplyPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val platform = target.findProperty("modstitch.platform")?.toString()
            ?: error("Project `${target.name}` is missing 'modstitch.platform' property. Cannot apply `modstitch-propapply`")

        when (platform.lowercase()) {
            "fabric-loom" -> target.pluginManager.apply("net.fabricmc.fabric-loom")
            "fabric-loom-remap" -> target.pluginManager.apply("net.fabricmc.fabric-loom-remap")
            "moddevgradle" -> target.pluginManager.apply("net.neoforged.moddev")
            "moddevgradle-legacy" -> target.pluginManager.apply("net.neoforged.moddev.legacyforge")
            else -> error("Cannot apply `modstitch-propapply`, unknown platform: $platform")
        }
    }
}