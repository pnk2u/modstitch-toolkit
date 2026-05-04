# MTK: `propapply`

A Gradle plugin to apply one of various modding Gradle plugins based on a project property.

This plugin does not depend on the plugins it applies in any way.
You must still define the plugins with `apply false` somewhere in your project.
otherwise Gradle will not know about them and will throw an error when you try to apply them with this plugin.

This plugin is intended to be used in conjunction with [modstitch-commonconf](./modstitch-commonconf/README.md)
to configure the applied plugin in a type-safe way. This can then by used with the 
[Stonecutter plugin](https://stonecutter.kikugie.dev/) to write a single buildscript for all Stonecutter targets.

## Usage

`build.gradle.kts`:
```kotlin
plugins {
    id("dev.isxander.mtk.propapply") version "0.1.0"
}
```

`gradle.properties`:
```properties
# Applies `net.fabricmc.fabric-loom`
modstitch.platform=fabric-loom

# Applies `net.neoforged.moddev`
modstitch.platform=moddevgradle
```

`root.gradle.kts`:
```kotlin
plugins {
    id("net.fabricmc.fabric-loom") version "x.y.z" apply false
    id("net.neoforged.moddev") version "x.y.z" apply false
}
```