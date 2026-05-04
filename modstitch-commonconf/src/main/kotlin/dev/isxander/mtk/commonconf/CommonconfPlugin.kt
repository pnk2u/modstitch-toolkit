package dev.isxander.mtk.commonconf

import dev.isxander.mtk.commonconf.util.convertNeoForgeVersionToMinecraftVersion
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.util.Constants
import net.neoforged.moddevgradle.dsl.ModDevExtension
import net.neoforged.moddevgradle.dsl.NeoForgeExtension
import net.neoforged.moddevgradle.legacyforge.dsl.LegacyForgeExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.gradle.kotlin.dsl.findByType

class CommonconfPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.create(
            "commonconf",
            CommonconfExtension::class.java
        )

        target.configurations.create("ccJarInJar") {
            isTransitive = false
        }

        target.pluginManager.withPlugin("net.fabricmc.fabric-loom") {
            val extension = target.extensions.getByType<CommonconfExtension>()
            applyLoom(target, extension)
        }
        target.pluginManager.withPlugin("net.neoforged.moddev") {
            val extension = target.extensions.getByType<CommonconfExtension>()
            applyMdg(target, extension)
        }
    }

    private fun applyLoom(target: Project, extension: CommonconfExtension) {
        val loom = target.extensions.getByType<LoomGradleExtensionAPI>()

        target.dependencies {
            "minecraft"(extension.minecraftVersion.map { "com.mojang:minecraft:$it" })
            "implementation"(extension.loaderVersion.map { "net.fabricmc:fabric-loader:$it" })
        }

        loom.accessWidenerPath = target.provider {
            val single = extension.accessxFiles.singleOrNull()
            if (extension.accessxFiles.isEmpty) {
                null
            } else {
                single ?: throw IllegalStateException("commonconf.accessxFiles must have exactly one or zero files in a Loom environment")
            }
        }

        target.configurations.named(Constants.Configurations.INCLUDE) {
            extendsFrom(target.configurations.getByName("ccJarInJar"))
        }
    }

    private fun applyMdg(target: Project, extension: CommonconfExtension) {
        val modDev = target.extensions.getByType<ModDevExtension>()

        // Estimate minecraftVersion with by parsing the loader version
        extension.minecraftVersion.convention(
            extension.loaderVersion.map { convertNeoForgeVersionToMinecraftVersion(it) }
        )

        // Source all accessxFiles to accessTransformers
        modDev.accessTransformers.from(extension.accessxFiles)

        // MDG enabling is not lazy.
        target.afterEvaluate {
            // only apply if there was a successful evaluation
            if (state.failure == null) {
                val neoForge = target.extensions.findByType<NeoForgeExtension>()
                val legacyForge = target.extensions.findByType<LegacyForgeExtension>()

                extension.loaderVersion.finalizeValue()

                neoForge?.enable {
                    version = extension.loaderVersion.get()
                }

                legacyForge?.enable {
                    version = extension.loaderVersion.get()
                }

                // Definitively set minecraftVersion, provided from MDG.
                extension.minecraftVersion = neoForge?.minecraftVersion
                    ?: legacyForge?.minecraftVersion
                extension.minecraftVersion.finalizeValue()

                target.configurations.named("jarJar") {
                    extendsFrom(target.configurations.getByName("ccJarInJar"))
                }
            }
        }
    }
}