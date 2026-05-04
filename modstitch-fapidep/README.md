# `modstitch-fapidep`

A Gradle plugin to depend on specific Fabric API modules, without applying Fabric Loom.

The plugin registers the original Fabric Loom `fabricApi` extension under the name `fapidep`.
It uses the Fabric Loom `FabricApiExtension` and the Fabric Loom `FabricApiExtensionImpl`.
It does not re-implement any functionality at this time.
This plugin includes Fabric Loom as a dependency, but does not apply it.

