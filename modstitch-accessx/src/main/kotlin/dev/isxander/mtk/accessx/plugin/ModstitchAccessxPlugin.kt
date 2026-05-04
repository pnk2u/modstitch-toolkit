package dev.isxander.mtk.accessx.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class ModstitchAccessxPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create(
            "accessx",
            AccessxExtension::class.java
        )
    }
}
