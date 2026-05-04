package dev.isxander.mtk.accessx

import dev.isxander.mtk.accessx.parser.SYNTAX_AT
import dev.isxander.mtk.accessx.parser.SYNTAX_AW_V1
import dev.isxander.mtk.accessx.parser.SYNTAX_AW_V2
import dev.isxander.mtk.accessx.parser.SYNTAX_CT_V1
import dev.isxander.mtk.accessx.parser.SYNTAX_CT_V2
import dev.isxander.mtk.accessx.parser.SyntaxTree
import dev.isxander.mtk.accessx.writer.AccessFileWriter
import dev.isxander.mtk.accessx.writer.FabricAccessFileWriter
import dev.isxander.mtk.accessx.writer.ForgeLikeAccessFileWriter

/**
 * Represents the supported class tweaker formats.
 */
enum class AccessFormat {
    /** (Neo)Forge's `accesstransformer.cfg`. */
    AT,

    /** Fabric's `accessWidener v1`. */
    AW_V1,

    /** Fabric's `accessWidener v2`. */
    AW_V2,

    /** Fabric's `classTweaker v1` */
    CT_V1,

    /** Fabric's `classTweaker v2` */
    CT_V2;

    // lazily evaluate as the syntax trees reference this enum
    internal val syntaxTree: SyntaxTree<AccessEntry> by lazy {
        when (this) {
            AT -> SYNTAX_AT
            AW_V1 -> SYNTAX_AW_V1
            AW_V2 -> SYNTAX_AW_V2
            CT_V1 -> SYNTAX_CT_V1
            CT_V2 -> SYNTAX_CT_V2
        }
    }

    // lazily evaluate as the writers reference this enum
    internal val writer: AccessFileWriter by lazy {
        when (this) {
            AT -> ForgeLikeAccessFileWriter
            AW_V1 -> FabricAccessFileWriter(AW_V1)
            AW_V2 -> FabricAccessFileWriter(AW_V2)
            CT_V1 -> FabricAccessFileWriter(CT_V1)
            CT_V2 -> FabricAccessFileWriter(CT_V2)
        }
    }
}