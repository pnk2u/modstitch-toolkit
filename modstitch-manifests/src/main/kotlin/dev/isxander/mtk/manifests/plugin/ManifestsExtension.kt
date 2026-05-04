package dev.isxander.mtk.manifests.plugin

import dev.isxander.mtk.manifests.FabricModJsonSpec
import dev.isxander.mtk.manifests.MinecraftReleasesValueSource
import dev.isxander.mtk.manifests.ModManifestSpec
import dev.isxander.mtk.manifests.NeoForgeModsTomlSpec
import dev.isxander.mtk.manifests.VersionRange
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import javax.inject.Inject

abstract class ManifestsExtension @Inject constructor(
    private val providers: ProviderFactory,
    private val objects: ObjectFactory,
) {
    /**
     * Creates a new [FabricModJsonSpec] instance, optionally configuring it.
     *
     * Returned ad-hoc — store it in a `val` in your build script and reference
     * it from generation tasks, publishing extensions, etc.
     */
    /**
     * Creates a bare [ModManifestSpec] holding only the common fields shared
     * by FMJ and NMT.
     *
     * Useful as a DRY template: define your shared metadata once, then
     * `from(common)` it into both a [fabricModJson] and a [neoForgeModsToml]
     * spec — and reuse the same property handles when wiring the publishing
     * extension.
     */
    @JvmOverloads
    fun manifest(action: Action<ModManifestSpec> = Action {}): ModManifestSpec =
        objects.newInstance(ModManifestSpec::class.java).apply(action::execute)

    @JvmOverloads
    fun fabricModJson(action: Action<FabricModJsonSpec> = Action {}): FabricModJsonSpec =
        objects.newInstance(FabricModJsonSpec::class.java).apply(action::execute)

    /**
     * Creates a new [NeoForgeModsTomlSpec] instance, optionally configuring it.
     *
     * Returned ad-hoc — store it in a `val` in your build script and reference
     * it from generation tasks, publishing extensions, etc.
     */
    @JvmOverloads
    fun neoForgeModsToml(action: Action<NeoForgeModsTomlSpec> = Action {}): NeoForgeModsTomlSpec =
        objects.newInstance(NeoForgeModsTomlSpec::class.java).apply(action::execute)

    /** Lazy provider of every release Minecraft version with a numeric dotted id. */
    fun minecraftReleases(): Provider<List<String>> =
        providers.of(MinecraftReleasesValueSource::class.java) {}

    fun minecraftReleasesMatching(range: String): Provider<List<String>> =
        minecraftReleasesMatching(mavenRange(range))

    /** Lazy provider of every release Minecraft version that satisfies [range]. */
    fun minecraftReleasesMatching(range: VersionRange): Provider<List<String>> =
        minecraftReleases().map { releases -> releases.filter { range.satisfies(it) } }

    /**
     * Lazy provider of every release Minecraft version that satisfies the range
     * supplied by [range]. The range itself is queried lazily, so it can be fed
     * by another `Property<VersionRange>`.
     */
    fun minecraftReleasesMatching(range: Provider<VersionRange>): Provider<List<String>> =
        minecraftReleases().zip(range) { releases, r -> releases.filter { r.satisfies(it) } }

    fun mavenRange(string: String): VersionRange =
        VersionRange.parseMaven(string)
}
