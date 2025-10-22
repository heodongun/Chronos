# ⏰ Chronos

A powerful Kotlin DateTime library inspired by Python's [Pendulum](https://pendulum.eustace.io/), featuring comprehensive timezone support, human-readable formatting, and Kotlin DSL.

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-blue.svg)](https://kotlinlang.org)
[![JitPack](https://jitpack.io/v/heodongun/Chronos.svg)](https://jitpack.io/#heodongun/Chronos)
[![Release](https://img.shields.io/github/v/release/heodongun/Chronos)](https://github.com/heodongun/Chronos/releases)

## ✨ Features

- 🌍 **Full Timezone Support** - Work with timezones effortlessly using kotlinx-datetime
- 📅 **Intuitive API** - Create and manipulate dates with a clean, fluent interface
- 🎯 **DSL Builders** - Build DateTime and Duration objects with Kotlin DSL
- ⚡ **Extension Functions** - Convert strings and timestamps naturally
- 🔄 **Period & Duration** - Calculate time differences with ease
- 📝 **Human-Readable** - Format dates like "3 days ago" or "in 2 hours"
- 🎨 **Flexible Formatting** - Custom format patterns with locale support
- 🧮 **Arithmetic Operations** - Add/subtract time naturally with operators
- 🔒 **Immutable** - Thread-safe by design
- 📦 **Serialization** - Built-in kotlinx-serialization support

## 📦 Installation

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.heodongun:Chronos:v1.0.0")
}
```

### Gradle (Groovy)

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.heodongun:Chronos:v1.0.0'
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.heodongun</groupId>
    <artifactId>Chronos</artifactId>
    <version>v1.0.0</version>
</dependency>
```

## 🚀 Quick Start

```kotlin
import io.github.heodongun.chronos.*
import kotlinx.datetime.TimeZone

// Create DateTime instances
val now = DateTime.now()
val today = DateTime.today()
val tomorrow = DateTime.tomorrow()

// With specific values
val birthday = DateTime.of(1990, 6, 15, 10, 30, 0, tz = TimeZone.of("Asia/Seoul"))

// Using DSL
val meeting = dateTime {
    year = 2024
    month = 3
    day = 15
    hour = 14
    minute = 30
    timezone = TimeZone.of("America/New_York")
}

// From timestamps
val dt1 = DateTime.fromTimestamp(1710511800)
val dt2 = 1710511800L.toDateTime()

// From strings
val dt3 = "2024-03-15T14:30:00Z".toDateTime()
val dt4 = DateTime.parse("2024-03-15T14:30:00Z")
```

## 📖 Core Features

### DateTime Creation

```kotlin
// Current time
val now = DateTime.now()
val nowInSeoul = DateTime.now(TimeZone.of("Asia/Seoul"))

// Relative dates
val today = DateTime.today()
val tomorrow = DateTime.tomorrow()
val yesterday = DateTime.yesterday()

// Specific date and time
val dt = DateTime.of(
    year = 2024,
    month = 3,
    day = 15,
    hour = 14,
    minute = 30,
    second = 0,
    tz = TimeZone.UTC
)

// Using DSL
val dt = dateTime {
    year = 2024
    month = 3
    day = 15
    hour = 14
    minute = 30
    timezone = TimeZone.UTC
}
```

### Properties

```kotlin
val dt = DateTime.of(2024, 6, 15, 14, 30, 45)

println(dt.year)        // 2024
println(dt.month)       // 6
println(dt.day)         // 15
println(dt.hour)        // 14
println(dt.minute)      // 30
println(dt.second)      // 45

println(dt.dayOfWeek)   // 6 (Saturday, ISO 8601)
println(dt.dayOfYear)   // 167
println(dt.weekOfYear)  // 24
println(dt.quarter)     // 2
println(dt.daysInMonth) // 30

println(dt.timestamp)   // Unix timestamp
println(dt.timezoneName)// "UTC" or timezone ID
```

### Timezone Operations

```kotlin
val utcTime = DateTime.now(TimeZone.UTC)
val seoulTime = utcTime.inTimezone("Asia/Seoul")
val nyTime = utcTime.inTz(TimeZone.of("America/New_York"))

println("UTC: ${utcTime.hour}:${utcTime.minute}")
println("Seoul: ${seoulTime.hour}:${seoulTime.minute}")
println("NY: ${nyTime.hour}:${nyTime.minute}")

// Check timezone properties
println(seoulTime.offset)       // Offset in seconds
println(seoulTime.offsetHours)  // Offset in hours (float)
println(seoulTime.isDst())      // Daylight Saving Time?
println(seoulTime.isLocal())    // Is local timezone?
println(seoulTime.isUtc())      // Is UTC?
```

### Modification

```kotlin
val dt = DateTime.of(2024, 3, 15, 14, 30, 0)

// Set specific components
val modified = dt.set(year = 2025, month = 6)
val newDate = dt.on(year = 2025, month = 6, day = 20)
val newTime = dt.at(hour = 10, minute = 30)

// Start and end of periods
val startOfDay = dt.startOf(DateTimeUnit.DAY)       // 00:00:00
val endOfDay = dt.endOf(DateTimeUnit.DAY)           // 23:59:59
val startOfMonth = dt.startOf(DateTimeUnit.MONTH)   // First day
val endOfMonth = dt.endOf(DateTimeUnit.MONTH)       // Last day
val startOfYear = dt.startOf(DateTimeUnit.YEAR)
val endOfYear = dt.endOf(DateTimeUnit.YEAR)

// Navigate to next/previous day of week
val nextMonday = dt.next(1)              // Monday = 1
val previousFriday = dt.previous(5)      // Friday = 5
val nextMondaySameTime = dt.next(1, keepTime = true)
```

### Arithmetic Operations

```kotlin
val dt = DateTime.of(2024, 3, 15, 14, 30, 0)

// Add time
val future1 = dt.add(days = 5, hours = 3, minutes = 30)
val future2 = dt.add(years = 1, months = 2)

// Subtract time
val past1 = dt.subtract(days = 5, hours = 3)
val past2 = dt.subtract(weeks = 2)

// Using operators with Duration
val duration = 5.days + 3.hours
val future3 = dt + duration
val past3 = dt - duration

// Complex durations
val complexDuration = duration {
    years = 1
    months = 2
    days = 5
    hours = 3
    minutes = 30
}
val future4 = dt + complexDuration
```

### Duration

```kotlin
// Create durations
val d1 = Duration(days = 5, hours = 3, minutes = 30)
val d2 = 5.days
val d3 = 3.hours + 30.minutes
val d4 = 2.weeks + 3.days

// DSL builder
val d5 = duration {
    days = 5
    hours = 3
    minutes = 30
}

// Duration operations
val sum = 5.days + 3.hours
val diff = 10.days - 2.days
val multiplied = 3.days * 2
val negated = -5.days

// Conversions
val d = Duration(days = 1, hours = 2, minutes = 30)
println(d.totalHours)     // Total as hours (Double)
println(d.totalMinutes)   // Total as minutes (Double)
println(d.inHours)        // Total as hours (Long)
println(d.inMinutes)      // Total as minutes (Long)

// Human readable
println(d.inWords())      // "1 day 2 hours 30 minutes"
```

### Comparison

```kotlin
val dt1 = DateTime.of(2024, 3, 15)
val dt2 = DateTime.of(2024, 3, 20)

// Operators
println(dt1 < dt2)      // true
println(dt1 <= dt2)     // true
println(dt1 > dt2)      // false
println(dt1 >= dt2)     // false
println(dt1 == dt2)     // false
println(dt1 != dt2)     // true

// Helper methods
println(dt1.isPast())               // true/false
println(dt2.isFuture())             // true/false
println(dt1.isLeapYear())           // true/false
println(dt1.isSameDay(dt2))         // false
println(dt1.isBirthday(birthday))   // true/false
```

### Period (Time Differences)

```kotlin
val start = DateTime.of(2024, 3, 15, 14, 30, 0)
val end = DateTime.of(2024, 3, 20, 18, 0, 0)

// Create periods
val period1 = Period(start, end)
val period2 = end.diff(start)
val period3 = end - start

// Access components
println(period1.years)       // Years difference
println(period1.months)      // Months difference
println(period1.days)        // Days difference
println(period1.hours)       // Hours difference
println(period1.minutes)     // Minutes difference
println(period1.seconds)     // Seconds difference

// Conversions
println(period1.inDays())       // Total days (Double)
println(period1.inHours())      // Total hours (Double)
println(period1.inMinutes())    // Total minutes (Double)
println(period1.inSeconds())    // Total seconds (Long)

// Human readable
val now = DateTime.now()
val future = now.add(days = 3, hours = 2)
println(now.diffForHumans(future))  // "in 3 days 2 hours"

val past = now.subtract(days = 5)
println(now.diffForHumans(past))    // "5 days ago"

// Check if date is in period
val middle = DateTime.of(2024, 3, 17)
println(middle in period1)          // true

// Iterate over period
for (dt in period1) {
    println(dt.toDateString())
}

// Custom step
val dates = period1.range(kotlinx.datetime.DateTimeUnit.DAY, step = 2).toList()
```

### Formatting

```kotlin
val dt = DateTime.of(2024, 3, 15, 14, 30, 45, 0, TimeZone.UTC)

// Built-in formats
println(dt.toDateString())          // "2024-03-15"
println(dt.toTimeString())          // "14:30:45"
println(dt.toDateTimeString())      // "2024-03-15 14:30:45"
println(dt.toIso8601String())       // ISO 8601 format

// Custom formatting
println(dt.format("YYYY-MM-DD"))                    // "2024-03-15"
println(dt.format("DD/MM/YYYY"))                    // "15/03/2024"
println(dt.format("MMMM D, YYYY"))                  // "March 15, 2024"
println(dt.format("dddd, MMMM D, YYYY"))            // "Friday, March 15, 2024"
println(dt.format("HH:mm:ss"))                      // "14:30:45"
println(dt.format("hh:mm A"))                       // "02:30 PM"
println(dt.format("YYYY-MM-DD HH:mm:ss Z"))         // With timezone offset

// Format tokens:
// YYYY - 4-digit year
// YY   - 2-digit year
// MMMM - Full month name
// MMM  - Short month name
// MM   - 2-digit month
// M    - Month number
// DD   - 2-digit day
// D    - Day number
// dddd - Full day name
// ddd  - Short day name
// HH   - 24-hour (00-23)
// hh   - 12-hour (01-12)
// mm   - Minutes
// ss   - Seconds
// SSS  - Milliseconds
// A    - AM/PM
// a    - am/pm
// Z    - Timezone offset
```

### Serialization

```kotlin
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Event(
    val name: String,
    val startTime: DateTime,
    val endTime: DateTime
)

val event = Event(
    name = "Meeting",
    startTime = DateTime.of(2024, 3, 15, 14, 0),
    endTime = DateTime.of(2024, 3, 15, 15, 0)
)

// Serialize
val json = Json.encodeToString(event)
println(json)

// Deserialize
val decoded = Json.decodeFromString<Event>(json)
```

## 🎯 Use Cases

### Scheduling Application

```kotlin
fun scheduleRecurringMeeting(start: DateTime, endDate: DateTime) {
    val period = Period(start, endDate)

    for (meetingDate in period.range(kotlinx.datetime.DateTimeUnit.WEEK)) {
        val meeting = meetingDate.at(hour = 10, minute = 0)
        println("Meeting scheduled for: ${meeting.format("dddd, MMMM D at hh:mm A")}")
    }
}
```

### Age Calculator

```kotlin
fun calculateAge(birthday: DateTime): Int {
    val now = DateTime.now()
    val period = now.diff(birthday)
    return period.years
}

val birthday = DateTime.of(1990, 6, 15)
println("Age: ${calculateAge(birthday)} years old")
```

### Business Hours Calculator

```kotlin
fun isWithinBusinessHours(dt: DateTime): Boolean {
    val businessStart = dt.set(hour = 9, minute = 0, second = 0)
    val businessEnd = dt.set(hour = 17, minute = 0, second = 0)

    return dt >= businessStart && dt <= businessEnd && dt.dayOfWeek in 1..5
}
```

### Reminder System

```kotlin
fun formatReminder(dueDate: DateTime): String {
    val now = DateTime.now()
    return when {
        dueDate.isPast() -> "Overdue by ${now.diffForHumans(dueDate)}"
        dueDate.isSameDay(now) -> "Due today at ${dueDate.format("hh:mm A")}"
        else -> "Due ${dueDate.diffForHumans(now)}"
    }
}
```

## 🔧 Advanced Features

### Range Operations

```kotlin
val start = DateTime.of(2024, 1, 1)
val end = DateTime.of(2024, 12, 31)
val period = start..end

// Check if date is in range
val someDate = DateTime.of(2024, 6, 15)
if (someDate in period) {
    println("Date is in 2024")
}

// Iterate with custom step
val quarterlyDates = period.range(
    kotlinx.datetime.DateTimeUnit.MONTH,
    step = 3
).toList()
```

### Timezone Conversions

```kotlin
fun convertMeetingTime(utcMeeting: DateTime): Map<String, String> {
    return mapOf(
        "New York" to utcMeeting.inTz("America/New_York").format("hh:mm A"),
        "London" to utcMeeting.inTz("Europe/London").format("hh:mm A"),
        "Tokyo" to utcMeeting.inTz("Asia/Tokyo").format("hh:mm A"),
        "Seoul" to utcMeeting.inTz("Asia/Seoul").format("hh:mm A")
    )
}
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Inspired by Python's [Pendulum](https://pendulum.eustace.io/) library
- Built on top of Kotlin's [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime)
- Influenced by [date-fns](https://date-fns.org/) and [Moment.js](https://momentjs.com/)

## 📚 Documentation

For more detailed documentation, visit: [https://github.com/heodongun/Chronos](https://github.com/heodongun/Chronos)

## 🐛 Bug Reports

If you find a bug, please open an issue at: [https://github.com/heodongun/Chronos/issues](https://github.com/heodongun/Chronos/issues)

---

Made with ❤️ by Heo Dongun
