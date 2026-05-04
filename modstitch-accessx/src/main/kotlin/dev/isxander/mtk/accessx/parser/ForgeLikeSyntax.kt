package dev.isxander.mtk.accessx.parser

import dev.isxander.mtk.accessx.AccessEntry
import kotlin.math.min

internal val SYNTAX_AT: SyntaxTree<AccessEntry> = createForgeLikeSyntaxTree()

// There are 4 types of entries:
// <access> <className>
// <access> <className> <fieldName>
// <access> <className> <methodName><methodDesc>
// <access> <className> <fieldOrMethodName> <fieldOrMethodDesc>
//
// We can differentiate between them based on the number of columns and whether
// the descriptor starts with '(' (indicating a method descriptor) or not
// (indicating a field descriptor, if any).
internal fun createForgeLikeSyntaxTree() = syntaxTree<AccessEntry> {
    +AccessTransformerNodeType() { (access, final) ->
        +word()() { className ->
            val className = className.replace('.', '/')

            leaf {
                AccessEntry.AccessModifier.Class(
                    access, transitive = false, final, className
                )
            }

            +word()() { target ->
                // target is either <methodName><methodDesc> OR <fieldName> if this is the leaf
                leaf {
                    val descStart = target.indexOf('(')
                    if (descStart < 0) {
                        AccessEntry.AccessModifier.Field(
                            access, transitive = false, final,
                            className, target, fieldDescriptor = null,
                        )
                    } else {
                        AccessEntry.AccessModifier.Method(
                            access, transitive = false, final,
                            className,
                            methodName = target.substring(0, descStart),
                            methodDescriptor = target.substring(descStart),
                        )
                    }
                }

                +word()() { descriptor ->
                    leaf {
                        if (descriptor.startsWith('(')) {
                            AccessEntry.AccessModifier.Method(
                                access, transitive = false, final, className, target, descriptor
                            )
                        } else {
                            AccessEntry.AccessModifier.Field(
                                access, transitive = false, final, className, target, descriptor
                            )
                        }
                    }
                }
            }
        }
    }
}

private object AccessTransformerNodeType : SyntaxNodeType<AccessTransformerNodeType.AccessTransformerNode> {
    val accessModifierDelimiters = charArrayOf('-', '+')

    override fun tryParse(string: String): AccessTransformerNode? {
        val modDelimiter = min(string.indexOfAny(accessModifierDelimiters).toUInt(), string.length.toUInt()).toInt()
        val accessType = when (string.subSequence(0, modDelimiter)) {
            "public" -> AccessEntry.AccessModifier.Modification.Public
            "protected" -> AccessEntry.AccessModifier.Modification.Protected
            "default" -> AccessEntry.AccessModifier.Modification.Unset
            "private" -> AccessEntry.AccessModifier.Modification.Private
            else -> return null
        }
        val isFinal = when (string.subSequence(modDelimiter, string.length)) {
            "+f" -> true
            "-f" -> false
            "" -> null
            else -> return null
        }

        return AccessTransformerNode(accessType, isFinal)
    }

    data class AccessTransformerNode(val modifier: AccessEntry.AccessModifier.Modification, val final: Boolean?)
}
