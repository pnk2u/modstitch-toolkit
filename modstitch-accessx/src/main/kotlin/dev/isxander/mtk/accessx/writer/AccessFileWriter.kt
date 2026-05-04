package dev.isxander.mtk.accessx.writer

import dev.isxander.mtk.accessx.AccessEntry
import dev.isxander.mtk.accessx.AccessFile
import java.io.Writer

internal interface AccessFileWriter {
    fun writeFile(file: AccessFile, writer: Writer): Boolean

    fun writeEntry(entry: AccessEntry, writer: Writer): Boolean
}