package dev.isxander.mtk.accessx.parser

import dev.isxander.mtk.accessx.AccessEntry
import dev.isxander.mtk.accessx.AccessFormat

internal val SYNTAX_AW_V1: SyntaxTree<AccessEntry> = createFabricSyntaxTree(AccessFormat.AW_V1)
internal val SYNTAX_AW_V2: SyntaxTree<AccessEntry> = createFabricSyntaxTree(AccessFormat.AW_V2)
internal val SYNTAX_CT_V1: SyntaxTree<AccessEntry> = createFabricSyntaxTree(AccessFormat.CT_V1)
internal val SYNTAX_CT_V2: SyntaxTree<AccessEntry> = createFabricSyntaxTree(AccessFormat.CT_V2)

internal fun createFabricSyntaxTree(format: AccessFormat) = syntaxTree<AccessEntry> {
    val accessModifierNode = when {
        format == AccessFormat.AW_V1 -> AccessModifierNodeType.map {
            TransitiveTaggedNodeType.TransitiveTaggedNode(
                it,
                false
            )
        }

        else -> AccessModifierNodeType.transitiveTagged()
    }

    +accessModifierNode() { (modifier, transitive) ->
        val isFinal = when (modifier) {
            AccessModifierNodeType.AccessModifierType.Accessible -> null
            else -> false
        }

        +"class" {
            +StringNodeType() { className ->
                leaf {
                    AccessEntry.AccessModifier.Class(
                        modification = when (modifier) {
                            AccessModifierNodeType.AccessModifierType.Accessible -> AccessEntry.AccessModifier.Modification.Public
                            AccessModifierNodeType.AccessModifierType.Extendable -> AccessEntry.AccessModifier.Modification.Public
                            else -> syntaxError("Unsupported access modifier for class")
                        },
                        transitive = transitive,
                        isFinal,
                        className
                    )
                }
            }
        }
        +"method" {
            +word()() { className ->
                +word()() { methodName ->
                    +word()() { methodDescriptor ->
                        leaf {
                            AccessEntry.AccessModifier.Method(
                                modification = when (modifier) {
                                    AccessModifierNodeType.AccessModifierType.Accessible -> AccessEntry.AccessModifier.Modification.Public
                                    AccessModifierNodeType.AccessModifierType.Extendable -> AccessEntry.AccessModifier.Modification.Protected
                                    else -> syntaxError("Unsupported access modifier for method")
                                },
                                transitive,
                                isFinal,
                                className, methodName, methodDescriptor
                            )
                        }
                    }
                }
            }
        }
        +"field" {
            +word()() { className ->
                +word()() { fieldName ->
                    +word()() { fieldDescriptor ->
                        leaf {
                            AccessEntry.AccessModifier.Field(
                                modification = when (modifier) {
                                    AccessModifierNodeType.AccessModifierType.Accessible -> AccessEntry.AccessModifier.Modification.Public
                                    AccessModifierNodeType.AccessModifierType.Mutable -> AccessEntry.AccessModifier.Modification.Unset
                                    else -> syntaxError("Unsupported access modifier for field")
                                },
                                transitive,
                                isFinal,
                                className, fieldName, fieldDescriptor
                            )
                        }
                    }
                }
            }
        }
    }

    if (format in listOf(AccessFormat.CT_V1, AccessFormat.CT_V2)) {
        +literal("inject-interface").transitiveTagged()() { (_, transitive) ->
            +word()() { className ->
                +word()() { interfaceName ->
                    leaf {
                        AccessEntry.InjectInterface(
                            className,
                            interfaceName,
                            transitive
                        )
                    }
                }
            }
        }
    }

    if (format in listOf(AccessFormat.CT_V2)) {
        +literal("extend-enum").transitiveTagged()() { (_, transitive) ->
            +word()() { enumClassName ->
                +word()() { constantName ->
                    leaf {
                        AccessEntry.ExtendEnum(
                            enumClassName,
                            constantName,
                            transitive
                        )
                    }
                }
            }
        }
    }
}

private object AccessModifierNodeType : SyntaxNodeType<AccessModifierNodeType.AccessModifierType> {
    override fun tryParse(string: String): AccessModifierType? {
        return when (string) {
            "accessible" -> AccessModifierType.Accessible
            "extendable" -> AccessModifierType.Extendable
            "mutable" -> AccessModifierType.Mutable
            else -> null
        }
    }

    enum class AccessModifierType {
        Accessible, Extendable, Mutable
    }
}

private class TransitiveTaggedNodeType<T>(private val type: SyntaxNodeType<T>) :
    SyntaxNodeType<TransitiveTaggedNodeType.TransitiveTaggedNode<T>> {
    override fun tryParse(string: String): TransitiveTaggedNode<T>? {
        val transitiveIndex = string.indexOf("transitive-")
        if (transitiveIndex != 0) {
            // Not transitive
            val node = type.tryParse(string)
                ?: return null
            return TransitiveTaggedNode(node, false)
        }

        val subNodeString = string.substring("transitive-".length)
        val node = type.tryParse(subNodeString)
            ?: return null
        return TransitiveTaggedNode(node, true)
    }

    data class TransitiveTaggedNode<T>(val node: T, val isTransitive: Boolean)
}
private fun <T> SyntaxNodeType<T>.transitiveTagged(): TransitiveTaggedNodeType<T> {
    return TransitiveTaggedNodeType(this)
}
