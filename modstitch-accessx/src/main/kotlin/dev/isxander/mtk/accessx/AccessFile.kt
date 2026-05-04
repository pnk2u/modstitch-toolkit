package dev.isxander.mtk.accessx

import dev.isxander.mtk.accessx.reader.error
import dev.isxander.mtk.accessx.reader.readUncommentedLine
import dev.isxander.mtk.accessx.reader.removeComment
import dev.isxander.mtk.accessx.reader.words
import java.io.LineNumberReader
import java.io.Reader
import java.io.Writer

internal data class AccessFile(
    val format: AccessFormat,
    val entries: List<AccessEntry>,
    val namespace: AccessNamespace,
) {
    val isValid: Boolean by lazy {
        entries.all { it.isValidFor(format) }
    }

    fun convertFormat(targetFormat: AccessFormat): AccessFile {
        return AccessFile(targetFormat, entries, namespace)
            .takeIf { it.isValid } ?: error("Target format does not support entries")
    }

    fun convertNamespace(targetNamespace: AccessNamespace): AccessFile {
        return AccessFile(format, entries, targetNamespace)
    }

    fun write(writer: Writer): Boolean {
        return format.writer.writeFile(this, writer)
    }

    companion object {
        fun parse(reader: Reader): AccessFile {
            val lineReader = LineNumberReader(reader)
            val header = lineReader.readUncommentedLine()

            // An empty file is a perfectly valid access transformer
            if (header == null) {
                return AccessFile(AccessFormat.AT, listOf(), AccessNamespace.Official)
            }

            // AW headers begin with the word "accessWidener"
            val (format, namespace) = if (header.startsWith("accessWidener", ignoreCase = true) || header.startsWith("classTweaker", ignoreCase = true)) {
                val headerData = header.words(limit = 3)

                if (headerData.size != 3) {
                    error("Unexpected header data: $headerData")
                }

                val type = headerData[0].lowercase()
                val version = headerData[1]
                val namespace = when (headerData[2]) {
                    "official" -> AccessNamespace.Official
                    "named" -> AccessNamespace.Named
                    "intermediary" -> AccessNamespace.Intermediary
                    else -> error("Unexpected namespace: ${headerData[2]}")
                }
                val format = when (type) {
                    "accesswidener" if version == "v1" ->
                        AccessFormat.AW_V1
                    "accesswidener" if version == "v2" ->
                        AccessFormat.AW_V2
                    "classtweaker" if version == "v1" ->
                        AccessFormat.CT_V1
                    "classtweaker" if version == "v2" ->
                        AccessFormat.CT_V2
                    else -> error("Unexpected header type: `$header`")
                }

                format to namespace
            } else {
                AccessFormat.AT to AccessNamespace.Official
            }

            val lines = if (format == AccessFormat.AT) {
                sequenceOf(header) + lineReader.lineSequence()
            } else {
                lineReader.lineSequence()
            }

            val entries = lines
                .map { line -> line.trimStart() }
                .filter { !it.startsWith('#') }
                .map { line -> line.removeComment() }
                .filter { line -> line.isNotBlank() }
                .map { line -> line.words() }
                .filter { words -> words.isNotEmpty() }
                .map { words -> format.syntaxTree.parse(words) ?: lineReader.error("Failed to parse $words") }
                .toList()

            return AccessFile(format, entries, namespace)
        }
    }
}