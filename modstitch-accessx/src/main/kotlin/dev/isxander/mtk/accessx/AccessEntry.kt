package dev.isxander.mtk.accessx

/**
 * Represents a single entry within an accessx file.
 */
internal sealed interface AccessEntry {
    val transitive: Boolean

    /**
     * Entry that modifies the access of a specific member.
     */
    sealed interface AccessModifier : AccessEntry {
        val modification: Modification
        override val transitive: Boolean
        val final: Boolean?
        val className: String

        data class Class(
            override val modification: Modification,
            override val transitive: Boolean,
            override val final: Boolean?,
            override val className: String,
        ) : AccessModifier

        data class Method(
            override val modification: Modification,
            override val transitive: Boolean,
            override val final: Boolean?,
            override val className: String,
            val methodName: String,
            val methodDescriptor: String,
        ) : AccessModifier

        data class Field(
            override val modification: Modification,
            override val transitive: Boolean,
            override val final: Boolean?,
            override val className: String,
            val fieldName: String,
            val fieldDescriptor: String?,
        ) : AccessModifier

        enum class Modification {
            /** The target keeps its original visibility modifier. */
            Unset,

            /** The target is made private. */
            Private,

            /** The target is made package-private. */
            Default,

            /** The target is made protected. */
            Protected,

            /** The target is made public. */
            Public,
        }
    }

    /**
     * Entry that injects interfaces to a target class.
     */
    data class InjectInterface(
        val targetClass: String,
        val interfaceToInject: String,
        override val transitive: Boolean,
    ) : AccessEntry

    /**
     * Entry that adds additional enum constants to a target class.
     */
    data class ExtendEnum(
        val targetClass: String,
        val enumConstant: String,
        override val transitive: Boolean,
    ) : AccessEntry
}

internal fun AccessEntry.isValidFor(format: AccessFormat): Boolean {
    // InjectInterface only supported by >=CT_V1
    if (format !in listOf(AccessFormat.CT_V1, AccessFormat.CT_V2)) {
        if (this is AccessEntry.InjectInterface) {
            return false
        }
    }

    // ExtendEnum only supported by >=CT_V2
    if (format !in listOf(AccessFormat.CT_V2)) {
        if (this is AccessEntry.ExtendEnum) {
            return false
        }
    }

    // Transitive not supported by AW_V1
    if (format in listOf(AccessFormat.AW_V1)) {
        if (this.transitive) {
            return false
        }
    }

    // Only AT supports making members less accessible than they already are.
    if (format !in listOf(AccessFormat.AT)) {
        if (this is AccessEntry.AccessModifier) {
            if (this.final == true
                || this.final == null
                && this.modification != AccessEntry.AccessModifier.Modification.Public
                && this.modification != AccessEntry.AccessModifier.Modification.Unset) {
                return false
            }
        }
    }

    // AT doesn't require a field descriptor for field entries.
    if (format !in listOf(AccessFormat.AT)) {
        if (this is AccessEntry.AccessModifier.Field) {
            if (this.fieldName.isBlank()) {
                return false
            }
        }
    }

    return true
}