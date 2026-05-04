package dev.isxander.mtk.commonconf

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.*
import javax.inject.Inject

abstract class CommonconfExtension @Inject constructor(objects: ObjectFactory, ) {
    /**
     * Defines the Minecraft version this project targets.
     *
     * - On Loom: `minecraft("com.mojang:minecraft:$thisProperty")`
     * - On ModDevGradle: This Property is essentially treated as a Provider.
     *   Setting this property does nothing. After commonconf enables MDG, it sets and finalizes
     *   this property to the minecraft version the MDG extension provides.
     *   Commonconf attempts to parse [loaderVersion] to convert to a minecraft version.
     *   Then, when it enables MDG, it sets this property definitively, provided by MDG itself.
     */
    val minecraftVersion: Property<String> =
        objects.property()

    /**
     * Defines the mod loader version this project targets.
     *
     * - On Loom: `implementation("net.fabricmc:fabric-loader:$thisProperty")`
     * - On ModDevGradle: `neoForge.version = thisProperty`
     */
    val loaderVersion: Property<String> =
        objects.property()

    /**
     * Defines any accessx files this project uses.
     *
     * - On Loom: `loom.accessWidenerPath = thisProperty`.
     *   If there are multiple files in this collection on Loom, an error will be thrown.
     * - On ModDevGradle: `neoForge.accessTransformers.from(thisProperty)`
     *
     * Consider using the `modstitch-accessx` plugin to convert your accessx files
     * between loader formats.
     */
    val accessxFiles: ConfigurableFileCollection =
        objects.fileCollection()
}