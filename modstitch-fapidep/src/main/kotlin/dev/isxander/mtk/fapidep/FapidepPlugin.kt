package dev.isxander.mtk.fapidep

import net.fabricmc.loom.api.fabricapi.FabricApiExtension
import net.fabricmc.loom.configuration.fabricapi.FabricApiExtensionImpl
import org.gradle.api.Plugin
import org.gradle.api.Project

class FapidepPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // TODO: this won't work because the impl references the LoomGradleExtension
        // we need to reimplement!
        target.extensions.create(
            FabricApiExtension::class.java,
            "fapidep",
            FabricApiExtensionImpl::class.java,
        )
    }
}