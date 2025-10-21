# JSONRepair

A lightweight Kotlin library that repairs malformed JSON strings, especially useful for parsing outputs from Large Language Models (LLMs).

망가진 JSON 형식을 자동으로 복구하는 Kotlin 경량 라이브러리입니다. LLM 출력 파싱에 특히 유용합니다.

---

## Features

- **Automatic JSON Repair**: Fixes common JSON formatting errors
- **Extension Function API**: Simple and intuitive Kotlin DSL
- **LLM Output Parsing**: Designed for incomplete or malformed JSON from AI models
- **Multiple Parsing Modes**: Standard and lenient parsing options
- **Zero Dependencies**: Lightweight library with no external dependencies
- **Comprehensive Error Handling**: Repairs various JSON syntax issues

### Supported Repairs

✅ Missing or misplaced quotes
✅ Single quotes to double quotes conversion
✅ Missing commas between elements
✅ Trailing commas removal
✅ Missing closing brackets/braces
✅ Unquoted object keys
✅ Comments in JSON (single-line `//` and multi-line `/* */`)
✅ Boolean/null case fixing (`True` → `true`, `False` → `false`, `None` → `null`)
✅ Extra commas removal
✅ Escape character fixes

---

## Installation

### Using JitPack (Recommended)

#### Gradle (Groovy)
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.heodongun:JSONRepair:v1.0.0'
}
```

#### Gradle Kotlin DSL
```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.heodongun:JSONRepair:v1.0.0")
}
```

#### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.heodongun</groupId>
    <artifactId>JSONRepair</artifactId>
    <version>v1.0.0</version>
</dependency>
```

### Using Maven Central (Coming Soon)
Maven Central distribution is pending namespace verification.

---

## Usage

### Basic Repair

```kotlin
import io.github.heodongun.jsonrepair.repairJson
import io.github.heodongun.jsonrepair.repairAndParse

fun main() {
    // Repair malformed JSON string
    val malformed = "{name: 'John', age: 30}"
    val repaired = malformed.repairJson()
    println(repaired)  // {"name": "John", "age": 30}
}
```

### Repair and Parse

```kotlin
import io.github.heodongun.jsonrepair.repairAndParse
import kotlinx.serialization.json.*

fun main() {
    val malformed = "{'name': 'John', 'age': 30, 'active': True}"

    // Repair and parse to JsonElement
    val result: JsonElement? = malformed.repairAndParse()

    if (result is JsonObject) {
        println(result["name"]?.jsonPrimitive?.content)  // John
        println(result["age"]?.jsonPrimitive?.int)       // 30
        println(result["active"]?.jsonPrimitive?.boolean) // true
    }
}
```

### Lenient Parsing

```kotlin
import io.github.heodongun.jsonrepair.repairAndParseLenient

fun main() {
    // For very complex or unusual JSON
    val malformed = "{name: 'John', age: 30"  // Missing closing brace

    val result = malformed.repairAndParseLenient()
    result?.let {
        println("Successfully parsed complex JSON")
    }
}
```

### LLM Output Parsing

```kotlin
import io.github.heodongun.jsonrepair.repairAndParse

fun parseLLMResponse(llmOutput: String) {
    // LLM often generates incomplete JSON
    val incomplete = """
        {
          "response": "Here is the data",
          "items": [
            {"id": 1, "name": "Item 1"},
            {"id": 2, "name": "Item 2"
    """.trimIndent()

    val result = incomplete.repairAndParse()
    result?.let { json ->
        println("Parsed LLM output successfully")
        // Process the repaired JSON
    }
}
```

---

## API Reference

### Extension Functions

#### `String.repairJson(): String`
Repairs a malformed JSON string and returns the repaired version.

```kotlin
val repaired = malformedJson.repairJson()
```

#### `String.repairAndParse(): JsonElement?`
Repairs and parses JSON string to `JsonElement`. Returns `null` if parsing fails.

```kotlin
val json: JsonElement? = malformedJson.repairAndParse()
```

#### `String.repairAndParseLenient(): JsonElement?`
Repairs and parses JSON with lenient mode enabled. More forgiving for unusual cases.

```kotlin
val json: JsonElement? = malformedJson.repairAndParseLenient()
```

---

## Examples

### Example 1: Missing Quotes
```kotlin
val input = "{name: John, age: 30}"
val output = input.repairJson()
// Result: {"name": "John", "age": 30}
```

### Example 2: Single Quotes
```kotlin
val input = "{'name': 'John', 'city': 'Seoul'}"
val output = input.repairJson()
// Result: {"name": "John", "city": "Seoul"}
```

### Example 3: Trailing Commas
```kotlin
val input = """{"name": "John", "age": 30,}"""
val output = input.repairJson()
// Result: {"name": "John", "age": 30}
```

### Example 4: Missing Brackets
```kotlin
val input = """{"items": [1, 2, 3"""
val output = input.repairJson()
// Result: {"items": [1, 2, 3]}
```

### Example 5: Comments
```kotlin
val input = """
{
    // User information
    "name": "John",
    /* Age field */
    "age": 30
}
"""
val output = input.repairJson()
// Result: {"name": "John", "age": 30}
```

### Example 6: Python-style Boolean/None
```kotlin
val input = """{"active": True, "deleted": False, "value": None}"""
val output = input.repairJson()
// Result: {"active": true, "deleted": false, "value": null}
```

---

## Technical Details

- **Language**: Kotlin 2.0+
- **Platform**: JVM (Java 21+)
- **Serialization**: kotlinx-serialization-json
- **Build System**: Gradle 8.14
- **License**: Apache License 2.0

---

## Comparison with Python json-repair

This library is inspired by the Python [json-repair](https://github.com/mangiucugna/json_repair) library but implemented natively in Kotlin with idiomatic Kotlin APIs.

| Feature | JSONRepair (Kotlin) | json-repair (Python) |
|---------|---------------------|----------------------|
| Extension Functions | ✅ | ❌ |
| kotlinx-serialization | ✅ | ❌ |
| Type Safety | ✅ | Partial |
| Lenient Parsing | ✅ | ✅ |
| Comment Removal | ✅ | ✅ |
| Quote Fixing | ✅ | ✅ |

---

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## License

```
Apache License 2.0

Copyright 2025 heodongun

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

## Author

Created by [heodongun](https://github.com/heodongun)
Inspired by Python's json-repair library

---

## Acknowledgments

- Inspired by [mangiucugna/json_repair](https://github.com/mangiucugna/json_repair)
- Built with [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)

---

## Changelog

### Version 1.0.0 (2025-01-21)
- Initial release
- Core JSON repair functionality
- Extension function API
- Comprehensive test coverage
- Maven Central distribution
