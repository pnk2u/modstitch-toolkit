package dev.isxander.mtk.accessx.writer

import dev.isxander.mtk.accessx.AccessEntry
import dev.isxander.mtk.accessx.AccessFile
import java.io.Writer
import kotlin.text.iterator

internal object ForgeLikeAccessFileWriter : AccessFileWriter {
    override fun writeFile(file: AccessFile, writer: Writer): Boolean {
        for (entry in file.entries) {
            if (!writeEntry(entry, writer)) return false
        }
        return true
    }

    override fun writeEntry(entry: AccessEntry, writer: Writer): Boolean {
        when (entry) {
            is AccessEntry.AccessModifier -> {
                writer.append(when (entry.modification) {
                    AccessEntry.AccessModifier.Modification.Protected -> "protected"
                    AccessEntry.AccessModifier.Modification.Default -> "default"
                    AccessEntry.AccessModifier.Modification.Private -> "private"
                    else -> "public"
                }).append(when (entry.final) {
                    true -> "+f"
                    false -> "-f"
                    null -> ""
                }).append(' ')

                // AT uses dots instead of slashes here (and only here!) for some reason.
                for (c in entry.className) {
                    writer.append(if (c == '/') '.' else c)
                }

                when (entry) {
                    is AccessEntry.AccessModifier.Class -> writer.appendLine()
                    is AccessEntry.AccessModifier.Field -> writer
                        .append(' ')
                        .append(entry.fieldName)
                        .append(' ')
                        .appendLine(entry.fieldDescriptor ?: "")
                    is AccessEntry.AccessModifier.Method -> writer
                        .append(' ')
                        .append(entry.methodName)
                        .appendLine(entry.methodDescriptor)
                }
            }

            is AccessEntry.InjectInterface -> return false
            is AccessEntry.ExtendEnum -> return false
        }

        return true
    }
}