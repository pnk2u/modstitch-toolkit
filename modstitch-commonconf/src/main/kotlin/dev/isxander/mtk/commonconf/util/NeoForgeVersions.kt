package dev.isxander.mtk.commonconf.util

import java.util.regex.Pattern

// Taken from MDG: VersionCapabilitiesInternal.neoForgeVersionToMinecraftVersion
// As of 2026-05-04

private val NEOFORGE_PATTERN = Pattern.compile("^(\\d+\\.\\d+)\\.\\d+(|-.*)$")
private val UNOBF_NEOFORGE_PATTERN = Pattern.compile("^(\\d+\\.\\d+\\.\\d+)\\.\\d+(|-.*)$")
private val UNOBF_NEOFORGE_ALPHA_PATTERN = Pattern.compile("^(\\d+\\.\\d+\\.\\d+)\\.0-alpha\\.\\d+\\+([\\w-]+)(?:\\.[\\d.]+)?$")

internal fun convertNeoForgeVersionToMinecraftVersion(neoForgeVersion: String): String? {
    // NeoForge omits the "1." at the start of the Minecraft version and just adds an incrementing last digit
    var matcher = NEOFORGE_PATTERN.matcher(neoForgeVersion)
    if (matcher.matches()) {
        var mcVersion = "1." + matcher.group(1)
        // Versions such as 21.0.0 are for Minecraft 1.21 and NOT 1.21.0, therefore we strip the trailing .0
        if (mcVersion.endsWith(".0")) {
            mcVersion = mcVersion.substring(0, mcVersion.length - 2)
        }
        return mcVersion
    }

    // Alpha versions also match the regular unobf pattern, check them first
    matcher = UNOBF_NEOFORGE_ALPHA_PATTERN.matcher(neoForgeVersion)
    if (matcher.matches()) {
        var mcVersion = matcher.group(1)
        if (mcVersion.endsWith(".0")) {
            mcVersion = mcVersion.substring(0, mcVersion.length - 2)
        }
        mcVersion += "-" + matcher.group(2)
        return mcVersion
    }

    matcher = UNOBF_NEOFORGE_PATTERN.matcher(neoForgeVersion)
    if (matcher.matches()) {
        var mcVersion = matcher.group(1)
        if (mcVersion.endsWith(".0")) {
            mcVersion = mcVersion.substring(0, mcVersion.length - 2)
        }
        return mcVersion
    }

    return null
}