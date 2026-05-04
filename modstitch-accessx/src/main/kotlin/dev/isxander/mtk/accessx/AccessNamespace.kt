package dev.isxander.mtk.accessx

internal enum class AccessNamespace {
    /** Used when no mappings are used, such as MDG or Loom-noremap */
    Official,
    /** Used when the tweaker is defined in custom mappings, like is common with Loom-remap */
    Named,
    /** Used when named mappings have been converted into an intermediary format */
    Intermediary,
}