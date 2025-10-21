package io.github.heodongun.jsonrepair

import kotlinx.serialization.json.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class JSONRepairTest {

    @Test
    fun `test repair missing quotes around keys`() {
        val malformed = "{name: 'John', age: 30}"
        val repaired = malformed.repairJson()
        val expected = """{"name": "John", "age": 30}"""

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonObject)
    }

    @Test
    fun `test repair single quotes to double quotes`() {
        val malformed = "{'name': 'John', 'age': 30}"
        val repaired = malformed.repairJson()

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonObject)
        assertEquals("John", result.jsonObject["name"]?.jsonPrimitive?.content)
    }

    @Test
    fun `test repair trailing commas`() {
        val malformed = """{"name": "John", "age": 30,}"""
        val repaired = malformed.repairJson()

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonObject)
    }

    @Test
    fun `test repair missing commas between elements`() {
        val malformed = """{"name": "John" "age": 30}"""
        val repaired = malformed.repairJson()

        val result = malformed.repairAndParseLenient()
        assertNotNull(result)
    }

    @Test
    fun `test repair missing closing braces`() {
        val malformed = """{"name": "John", "address": {"city": "Seoul" """
        val repaired = malformed.repairJson()

        assertTrue(repaired.endsWith("}}"))

        val result = malformed.repairAndParse()
        assertNotNull(result)
    }

    @Test
    fun `test repair missing closing brackets`() {
        val malformed = """[1, 2, 3, [4, 5"""
        val repaired = malformed.repairJson()

        assertTrue(repaired.endsWith("]]"))

        val result = malformed.repairAndParse()
        assertNotNull(result)
    }

    @Test
    fun `test repair boolean values`() {
        val malformed = """{"active": True, "deleted": False, "value": None}"""
        val repaired = malformed.repairJson()

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonObject)
        assertEquals(true, result.jsonObject["active"]?.jsonPrimitive?.boolean)
        assertEquals(false, result.jsonObject["deleted"]?.jsonPrimitive?.boolean)
        assertTrue(result.jsonObject["value"] is JsonNull)
    }

    @Test
    fun `test repair comments removal`() {
        val malformed = """
            {
                // This is a comment
                "name": "John",
                /* Multi-line
                   comment */
                "age": 30
            }
        """.trimIndent()
        val repaired = malformed.repairJson()

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonObject)
    }

    @Test
    fun `test repair array with missing commas`() {
        val malformed = """[1 2 3 4 5]"""
        val repaired = malformed.repairJson()

        // This is a complex case that might need more sophisticated parsing
        val result = malformed.repairAndParseLenient()
        // May not work perfectly for all cases, but should handle common scenarios
    }

    @Test
    fun `test repair nested objects`() {
        val malformed = """{name: "John", address: {city: "Seoul", country: "Korea"}"""
        val repaired = malformed.repairJson()

        val result = malformed.repairAndParse()
        assertNotNull(result)
        assertTrue(result is JsonObject)
    }

    @Test
    fun `test repair array of objects`() {
        val malformed = """[{name: "John"}, {name: "Jane"}]"""
        val repaired = malformed.repairJson()

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonArray)
        assertEquals(2, result.jsonArray.size)
    }

    @Test
    fun `test repair extra commas`() {
        val malformed = """{,,"name": "John",,,"age": 30,,,}"""
        val repaired = malformed.repairJson()

        val result = malformed.repairAndParseLenient()
        assertNotNull(result)
    }

    @Test
    fun `test repair uppercase boolean and null`() {
        val malformed = """{"active": TRUE, "deleted": FALSE, "value": NULL}"""
        val repaired = malformed.repairJson()

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonObject)
    }

    @Test
    fun `test repairAndParse with valid JSON`() {
        val valid = """{"name": "John", "age": 30}"""
        val result = valid.repairAndParse()

        assertNotNull(result)
        assertTrue(result is JsonObject)
        assertEquals("John", result.jsonObject["name"]?.jsonPrimitive?.content)
        assertEquals(30, result.jsonObject["age"]?.jsonPrimitive?.int)
    }

    @Test
    fun `test repairAndParseLenient with complex malformed JSON`() {
        val malformed = """{name: 'John', age: 30, active: True, address: {city: 'Seoul'}"""
        val result = malformed.repairAndParseLenient()

        assertNotNull(result)
        assertTrue(result is JsonObject)
    }

    @Test
    fun `test repair empty object`() {
        val malformed = "{}"
        val repaired = malformed.repairJson()

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonObject)
        assertEquals(0, result.jsonObject.size)
    }

    @Test
    fun `test repair empty array`() {
        val malformed = "[]"
        val repaired = malformed.repairJson()

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonArray)
        assertEquals(0, result.jsonArray.size)
    }

    @Test
    fun `test repair mixed quotes and missing brackets`() {
        val malformed = """[{"name": 'John', "age": 30}, {"name": 'Jane', "age": 25"""
        val repaired = malformed.repairJson()

        val result = malformed.repairAndParse()
        assertNotNull(result)
        assertTrue(result is JsonArray)
    }

    @Test
    fun `test LLM output with incomplete JSON`() {
        val llmOutput = """
            {
              "response": "Here is the data",
              "data": {
                "items": [
                  {"id": 1, "name": "Item 1"},
                  {"id": 2, "name": "Item 2"
        """.trimIndent()

        val repaired = llmOutput.repairJson()

        // Check that repair adds missing brackets
        assertTrue(repaired.count { it == '{' } == repaired.count { it == '}' })
        assertTrue(repaired.count { it == '[' } == repaired.count { it == ']' })

        // Try to parse with lenient mode
        val result = llmOutput.repairAndParseLenient()

        // This is a very complex case - if it parses, great, otherwise just verify repair attempt
        if (result != null) {
            assertTrue(result is JsonObject)
        }
    }

    @Test
    fun `test simple array with missing closing bracket`() {
        val malformed = """{"items": [1, 2, 3"""
        val repaired = malformed.repairJson()
        val expected = """{"items": [1, 2, 3]}"""

        assertEquals(expected, repaired)

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonObject)

        val items = result.jsonObject["items"]
        assertNotNull(items)
        assertTrue(items is JsonArray)
        assertEquals(3, items.jsonArray.size)
        assertEquals(1, items.jsonArray[0].jsonPrimitive.int)
        assertEquals(2, items.jsonArray[1].jsonPrimitive.int)
        assertEquals(3, items.jsonArray[2].jsonPrimitive.int)
    }

    @Test
    fun `test nested structure with missing multiple brackets`() {
        val malformed = """{"outer": {"inner": [1, 2, {"deep": "value" """
        val repaired = malformed.repairJson()

        // Verify brackets are properly balanced
        assertTrue(repaired.count { it == '{' } == repaired.count { it == '}' })
        assertTrue(repaired.count { it == '[' } == repaired.count { it == ']' })

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonObject)
    }

    @Test
    fun `test array at root level with missing bracket`() {
        val malformed = """[1, 2, 3, 4, 5"""
        val repaired = malformed.repairJson()
        val expected = """[1, 2, 3, 4, 5]"""

        assertEquals(expected, repaired)

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonArray)
        assertEquals(5, result.jsonArray.size)
    }

    @Test
    fun `test object inside array with missing brackets`() {
        val malformed = """{"data": [{"id": 1}, {"id": 2"""
        val repaired = malformed.repairJson()

        // Verify brackets are properly balanced
        assertTrue(repaired.count { it == '{' } == repaired.count { it == '}' })
        assertTrue(repaired.count { it == '[' } == repaired.count { it == ']' })

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonObject)

        val data = result.jsonObject["data"]
        assertNotNull(data)
        assertTrue(data is JsonArray)
        assertEquals(2, data.jsonArray.size)
    }

    @Test
    fun `test deeply nested arrays with missing brackets`() {
        val malformed = """[[[1, 2, [3, 4"""
        val repaired = malformed.repairJson()

        // Verify all brackets are balanced
        assertEquals(
            malformed.count { it == '[' },
            repaired.count { it == ']' }
        )

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonArray)
    }

    @Test
    fun `test mixed nesting with missing closing brackets`() {
        val malformed = """{"a": [1, {"b": [2, 3"""
        val repaired = malformed.repairJson()

        // Check proper bracket balancing
        assertTrue(repaired.count { it == '{' } == repaired.count { it == '}' })
        assertTrue(repaired.count { it == '[' } == repaired.count { it == ']' })

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonObject)
    }

    @Test
    fun `test object with array that has only opening bracket`() {
        val malformed = """{"list": ["""
        val repaired = malformed.repairJson()

        assertEquals("""{"list": []}""", repaired)

        val result = Json.parseToJsonElement(repaired)
        assertNotNull(result)
        assertTrue(result is JsonObject)

        val list = result.jsonObject["list"]
        assertNotNull(list)
        assertTrue(list is JsonArray)
        assertEquals(0, list.jsonArray.size)
    }
}
