package io.github.heodongun.jsonrepair

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Extension function to repair malformed JSON strings
 *
 * This function attempts to fix common JSON formatting errors such as:
 * - Missing or extra quotes
 * - Missing or extra commas
 * - Missing closing brackets/braces
 * - Unescaped characters
 * - Trailing commas
 * - Single quotes instead of double quotes
 * - Comments in JSON
 *
 * @return Repaired JSON string
 */
fun String.repairJson(): String {
    var json = this.trim()

    // Remove comments (single-line and multi-line)
    json = json.replace(Regex("//.*"), "")
    json = json.replace(Regex("/\\*.*?\\*/", RegexOption.DOT_MATCHES_ALL), "")

    // Replace single quotes with double quotes (but not within strings)
    json = fixQuotes(json)

    // Remove trailing commas before closing brackets
    json = json.replace(Regex(",\\s*([}\\]])"), "$1")

    // Add missing commas between array/object elements
    json = addMissingCommas(json)

    // Fix missing quotes around keys
    json = fixUnquotedKeys(json)

    // Fix missing closing brackets/braces
    json = fixMissingBrackets(json)

    // Fix escaped characters
    json = fixEscapedCharacters(json)

    // Fix boolean and null values
    json = fixBooleanAndNull(json)

    // Remove extra commas
    json = removeExtraCommas(json)

    return json.trim()
}

/**
 * Extension function to repair and parse JSON string to JsonElement
 *
 * @return Parsed JsonElement or null if parsing fails
 */
fun String.repairAndParse(): JsonElement? {
    return try {
        val repaired = this.repairJson()
        Json.parseToJsonElement(repaired)
    } catch (e: Exception) {
        null
    }
}

/**
 * Extension function to repair and parse JSON string with lenient mode
 *
 * @return Parsed JsonElement or null if parsing fails
 */
fun String.repairAndParseLenient(): JsonElement? {
    return try {
        val repaired = this.repairJson()
        val lenientJson = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
        lenientJson.parseToJsonElement(repaired)
    } catch (e: Exception) {
        null
    }
}

private fun fixQuotes(json: String): String {
    var result = StringBuilder()
    var inString = false
    var i = 0

    while (i < json.length) {
        val char = json[i]

        when {
            char == '"' && (i == 0 || json[i - 1] != '\\') -> {
                inString = !inString
                result.append(char)
            }
            char == '\'' && !inString -> {
                // Replace single quote with double quote if not in string
                val nextQuoteIndex = json.indexOf('\'', i + 1)
                if (nextQuoteIndex != -1) {
                    result.append('"')
                    result.append(json.substring(i + 1, nextQuoteIndex))
                    result.append('"')
                    i = nextQuoteIndex
                } else {
                    result.append(char)
                }
            }
            else -> result.append(char)
        }
        i++
    }

    return result.toString()
}

private fun addMissingCommas(json: String): String {
    var result = json

    // Add comma between } and { (objects)
    result = result.replace(Regex("}\\s*\\{"), "},{")

    // Add comma between ] and [ (arrays)
    result = result.replace(Regex("]\\s*\\["), "],[")

    // Add comma between } and [
    result = result.replace(Regex("}\\s*\\["), "},[")

    // Add comma between ] and {
    result = result.replace(Regex("]\\s*\\{"), "],{")

    // Add comma between string/number and object/array start
    result = result.replace(Regex("([\"\\d])\\s*([{\\[])"), "$1,$2")

    // Add comma between object/array end and string/number
    result = result.replace(Regex("([}\\]])\\s*([\"])"), "$1,$2")

    // Add comma between closing quote and opening quote (string elements)
    result = result.replace(Regex("\"\\s+\""), "\",\"")

    // Add comma between number and string
    result = result.replace(Regex("(\\d)\\s+(\"[^\"]*\")"), "$1,$2")

    // Add comma between string and number
    result = result.replace(Regex("(\"[^\"]*\")\\s+(\\d)"), "$1,$2")

    return result
}

private fun fixUnquotedKeys(json: String): String {
    // Match unquoted keys in objects (word characters followed by colon)
    return json.replace(Regex("([{,]\\s*)([a-zA-Z_][a-zA-Z0-9_]*)\\s*:")) {
        "${it.groupValues[1]}\"${it.groupValues[2]}\":"
    }
}

private fun fixMissingBrackets(json: String): String {
    var result = json
    val stack = mutableListOf<Char>()

    // Track opening brackets/braces and their order
    for (char in result) {
        when (char) {
            '{' -> stack.add('{')
            '}' -> if (stack.lastOrNull() == '{') stack.removeLastOrNull()
            '[' -> stack.add('[')
            ']' -> if (stack.lastOrNull() == '[') stack.removeLastOrNull()
        }
    }

    // Add missing closing brackets/braces in reverse order (LIFO)
    while (stack.isNotEmpty()) {
        when (stack.removeLast()) {
            '{' -> result += "}"
            '[' -> result += "]"
        }
    }

    return result
}

private fun fixEscapedCharacters(json: String): String {
    var result = json

    // Fix common escape sequence issues
    // This is a simplified version - a complete implementation would handle all cases
    result = result.replace("\\n", "\\\\n")
    result = result.replace("\\t", "\\\\t")
    result = result.replace("\\r", "\\\\r")

    // Fix already escaped sequences (avoid double escaping)
    result = result.replace("\\\\\\\\n", "\\\\n")
    result = result.replace("\\\\\\\\t", "\\\\t")
    result = result.replace("\\\\\\\\r", "\\\\r")

    return result
}

private fun fixBooleanAndNull(json: String): String {
    var result = json

    // Fix True/False/None to lowercase
    result = result.replace(Regex("\\bTrue\\b"), "true")
    result = result.replace(Regex("\\bFalse\\b"), "false")
    result = result.replace(Regex("\\bNone\\b"), "null")

    // Fix TRUE/FALSE/NULL
    result = result.replace(Regex("\\bTRUE\\b"), "true")
    result = result.replace(Regex("\\bFALSE\\b"), "false")
    result = result.replace(Regex("\\bNULL\\b"), "null")

    return result
}

private fun removeExtraCommas(json: String): String {
    var result = json

    // Remove multiple consecutive commas
    result = result.replace(Regex(",+"), ",")

    // Remove commas at the start of objects/arrays (including whitespace)
    result = result.replace(Regex("([{\\[])\\s*,+\\s*"), "$1")

    // Remove commas at the end of objects/arrays (should already be handled but double-check)
    result = result.replace(Regex(",\\s*([}\\]])"), "$1")

    return result
}


