package dev.isxander.mtk.accessx.reader

import java.io.LineNumberReader

/**
 * Reads the next non-blank, non-commented line from the [LineNumberReader].
 *
 * If a line contains an inline comment, only the part before the `#` is returned.
 *
 * @return The uncommented portion of the next relevant line, or `null` if end of stream is reached.
 */
internal fun LineNumberReader.readUncommentedLine(): CharSequence? {
    while (true) {
        val line = readLine()
        if (line == null) {
            return null
        }

        val i = line.indexOfFirst { !it.isWhitespace() }
        if (i < 0 || line[i] == '#') {
            continue
        }

        val j = line.indexOf('#', i)
        return if (j < 0) line else line.subSequence(0, j)
    }
}

internal fun CharSequence.removeComment(): CharSequence {
    val j = indexOf('#')
    return if (j < 0) this else subSequence(0, j)
}

/**
 * Throws a [FormatException] with the given [message].
 *
 * @param message The error message to include in the exception.
 * @throws FormatException Always thrown with the provided message and current line number.
 */
internal fun LineNumberReader.error(message: String): Nothing =
    throw FormatException(message, lineNumber)

/**
 * Exception thrown when a format error occurs while reading a text stream.
 *
 * @param message The error message.
 * @param lineNumber The line number where the error was encountered.
 */
private class FormatException(message: String, val lineNumber: Int): Exception("$message:line $lineNumber")

/**
 * Splits the given [CharSequence] into a list of words using whitespace as the delimiter.
 *
 * @param limit An optional limit on the number of words to return.
 * @return A list of words extracted from the string.
 */
internal fun CharSequence.words(limit: Int = 0): List<String> =
    trim().split(WHITESPACE, limit)

private val WHITESPACE = Regex("\\s+")