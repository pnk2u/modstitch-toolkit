import org.gradle.api.provider.Provider
import org.gradle.plugin.use.PluginDependency

fun Provider<PluginDependency>.asDependency(): Provider<String> =
    map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }
