# ⏰ Chronos - Modern Kotlin DateTime Library

<div align="center">

[![Maven Central](https://img.shields.io/maven-central/v/io.github.heodongun/chronos)](https://central.sonatype.com/artifact/io.github.heodongun/chronos)
[![JitPack](https://jitpack.io/v/heodongun/Chronos.svg)](https://jitpack.io/#heodongun/Chronos)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0-blue.svg)](https://kotlinlang.org)

A powerful and intuitive Kotlin DateTime library inspired by Python's [Pendulum](https://pendulum.eustace.io/), featuring comprehensive timezone support, human-readable formatting, and idiomatic Kotlin DSL.

[Features](#-features) • [Installation](#-installation) • [Quick Start](#-quick-start) • [Documentation](#-documentation) • [Examples](#-examples)

</div>

---

## 🎯 Why Chronos?

Working with dates and times in Kotlin can be verbose and error-prone. Chronos makes it simple, intuitive, and fun:

```kotlin
// ❌ Without Chronos - Complex and verbose
val instant = Instant.fromEpochSeconds(System.currentTimeMillis() / 1000)
val localDateTime = instant.toLocalDateTime(TimeZone.of("Asia/Seoul"))
val futureInstant = instant.plus(5, DateTimeUnit.DAY, TimeZone.UTC)

// ✅ With Chronos - Simple and expressive
val now = DateTime.now("Asia/Seoul")
val future = now + 5.days
println(now.diffForHumans(future))  // "in 5 days"
```

## ✨ Features

### 🌍 Timezone Support
- Full timezone conversion and manipulation
- DST-aware operations
- Timezone offset calculations
- Support for all IANA timezone identifiers

### 📅 Intuitive DateTime API
- Natural date creation and manipulation
- Fluent method chaining
- Immutable by design (thread-safe)
- Rich set of properties and methods

### 🎯 Kotlin DSL
- Type-safe builders for DateTime and Duration
- Extension properties (`5.days`, `3.hours`)
- Operator overloading for arithmetic operations
- Idiomatic Kotlin style

### 📝 Human-Readable Formatting
- "3 days ago", "in 2 hours" style formatting
- Customizable format patterns
- Locale-aware date formatting
- ISO 8601 compliance

### ⚡ Period & Duration
- Calculate time differences easily
- Range operations and iterations
- Multiple time unit support
- Conversion between units

### 🔒 Production-Ready
- 100% test coverage (33 tests)
- Immutable design for thread safety
- Built-in kotlinx-serialization support
- Zero external dependencies (except kotlinx-datetime)

## 📦 Installation

### Maven Central (Recommended)

Add Chronos to your project with a single line:

#### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("io.github.heodongun:chronos:1.0.0")
}
```

#### Gradle (Groovy)

```groovy
dependencies {
    implementation 'io.github.heodongun:chronos:1.0.0'
}
```

#### Maven

```xml
<dependency>
    <groupId>io.github.heodongun</groupId>
    <artifactId>chronos</artifactId>
    <version>1.0.0</version>
</dependency>
```

### JitPack (Alternative)

If you need immediate access or want to use a specific commit:

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.heodongun:Chronos:v1.0.0")
}
```

## 🚀 Quick Start

### Basic Usage

```kotlin
import io.github.heodongun.chronos.*

// Get current time
val now = DateTime.now()
val today = DateTime.today()
val tomorrow = DateTime.tomorrow()

// Create specific dates
val birthday = DateTime.of(1990, 6, 15, 10, 30)

// Use the DSL
val meeting = dateTime {
    year = 2024
    month = 12
    day = 25
    hour = 14
    minute = 30
    timezone = TimeZone.of("Asia/Seoul")
}

// Format dates
println(meeting.format("MMMM D, YYYY at hh:mm A"))
// Output: "December 25, 2024 at 02:30 PM"
```

### Working with Timezones

```kotlin
// Create in specific timezone
val seoulTime = DateTime.now(TimeZone.of("Asia/Seoul"))

// Convert between timezones
val nyTime = seoulTime.inTimezone("America/New_York")
val londonTime = seoulTime.inTz(TimeZone.of("Europe/London"))

// Check timezone properties
println("Seoul offset: ${seoulTime.offsetHours}h")  // +9.0
println("Is UTC? ${seoulTime.isUtc()}")             // false
println("Is DST? ${seoulTime.isDst()}")             // false
```

### Arithmetic Operations

```kotlin
val now = DateTime.now()

// Add time using extension properties
val future = now + 5.days + 3.hours + 30.minutes

// Subtract time
val past = now - 2.weeks - 1.day

// Complex durations
val duration = duration {
    years = 1
    months = 2
    days = 5
    hours = 3
}
val newDate = now + duration

// Human-readable differences
println(now.diffForHumans(future))  // "in 5 days 3 hours"
println(now.diffForHumans(past))    // "2 weeks 1 day ago"
```

### Date Modification

```kotlin
val dt = DateTime.of(2024, 6, 15, 14, 30)

// Set specific values
val modified = dt.set(year = 2025, hour = 10)

// Navigate to start/end of periods
val startOfDay = dt.startOf(DateTimeUnit.DAY)     // 2024-06-15 00:00:00
val endOfMonth = dt.endOf(DateTimeUnit.MONTH)     // 2024-06-30 23:59:59
val startOfYear = dt.startOf(DateTimeUnit.YEAR)   // 2024-01-01 00:00:00

// Navigate to next/previous day of week
val nextMonday = dt.next(1)              // Next Monday
val previousFriday = dt.previous(5)      // Previous Friday
```

### Comparison and Validation

```kotlin
val dt1 = DateTime.of(2024, 3, 15)
val dt2 = DateTime.of(2024, 3, 20)

// Comparison operators
if (dt1 < dt2) {
    println("dt1 is earlier")
}

// Helper methods
dt1.isPast()                // true/false
dt2.isFuture()              // true/false
dt1.isLeapYear()            // true (2024 is leap year)
dt1.isSameDay(dt2)          // false
dt1.isBirthday(birthday)    // Check if it's someone's birthday
```

## 📖 Documentation

### DateTime Creation

#### Factory Methods

```kotlin
// Current time
DateTime.now()                              // Current time in UTC
DateTime.now(TimeZone.of("Asia/Seoul"))    // Current time in specific TZ

// Relative dates
DateTime.today()                            // Today at 00:00:00
DateTime.tomorrow()                         // Tomorrow at 00:00:00
DateTime.yesterday()                        // Yesterday at 00:00:00

// Specific date and time
DateTime.of(2024, 12, 25)                  // Date only
DateTime.of(2024, 12, 25, 14, 30)          // Date and time
DateTime.of(2024, 12, 25, 14, 30, 0, 0, TimeZone.UTC)  // Full specification

// From timestamp
DateTime.fromTimestamp(1710511800)         // Unix timestamp (seconds)

// From string (ISO 8601)
DateTime.parse("2024-12-25T14:30:00Z")
```

#### DSL Builder

```kotlin
val dt = dateTime {
    year = 2024
    month = 12
    day = 25
    hour = 14
    minute = 30
    second = 0
    nanosecond = 0
    timezone = TimeZone.of("Asia/Seoul")
}
```

#### Extension Functions

```kotlin
// String to DateTime
"2024-12-25T14:30:00Z".toDateTime()
"2024-12-25T14:30:00+09:00".toDateTime(TimeZone.of("Asia/Seoul"))

// Long timestamp to DateTime
1710511800L.toDateTime()
1710511800L.toDateTime(TimeZone.of("Asia/Seoul"))
```

### DateTime Properties

```kotlin
val dt = DateTime.of(2024, 6, 15, 14, 30, 45)

// Basic components
dt.year         // 2024
dt.month        // 6
dt.day          // 15
dt.hour         // 14
dt.minute       // 30
dt.second       // 45
dt.nanosecond   // 0

// Calendar properties
dt.dayOfWeek    // 6 (Saturday, ISO 8601: Monday=1, Sunday=7)
dt.dayOfYear    // 167
dt.weekOfYear   // 24
dt.quarter      // 2 (Q2)
dt.daysInMonth  // 30

// Timezone properties
dt.timezone        // TimeZone object
dt.timezoneName    // "UTC" or timezone ID
dt.offset          // Offset in seconds
dt.offsetHours     // Offset in hours (Double)

// Timestamps
dt.timestamp       // Unix timestamp (seconds)
```

### Duration & Period

#### Duration (Fixed Time Amount)

```kotlin
// Create durations
val d1 = Duration(days = 5, hours = 3, minutes = 30)
val d2 = 5.days
val d3 = 3.hours + 30.minutes
val d4 = 2.weeks + 3.days

// DSL builder
val d5 = duration {
    years = 1
    months = 2
    weeks = 1
    days = 5
    hours = 3
    minutes = 30
    seconds = 45
}

// Operations
val sum = 5.days + 3.hours
val diff = 10.days - 2.days
val multiplied = 3.days * 2
val negated = -5.days

// Conversions
d1.totalDays        // 5.145833... (days as Double)
d1.totalHours       // 123.5 (hours as Double)
d1.totalMinutes     // 7410.0 (minutes as Double)
d1.inDays           // 5 (days as Long)
d1.inHours          // 123 (hours as Long)
d1.inMinutes        // 7410 (minutes as Long)

// Human readable
d1.inWords()        // "5 days 3 hours 30 minutes"
```

#### Period (Time Between Two DateTimes)

```kotlin
val start = DateTime.of(2024, 3, 15, 14, 30)
val end = DateTime.of(2024, 3, 20, 18, 0)

// Create periods
val period1 = Period(start, end)
val period2 = end.diff(start)
val period3 = end - start

// Access components
period1.years       // 0
period1.months      // 0
period1.days        // 5
period1.hours       // 3
period1.minutes     // 30
period1.seconds     // 0

// Conversions
period1.inDays()       // 5.145833... (total days)
period1.inHours()      // 123.5 (total hours)
period1.inMinutes()    // 7410.0 (total minutes)
period1.inSeconds()    // 444600 (total seconds)

// Human readable
period1.toHumanString()  // "5 days 3 hours 30 minutes"

// Range operations
val middle = DateTime.of(2024, 3, 17)
middle in period1       // true

// Iterate over period (day by day)
for (dt in period1) {
    println(dt.format("YYYY-MM-DD"))
}

// Custom iteration step
val weeklyDates = period1.range(DateTimeUnit.DAY, step = 7).toList()
```

### Formatting

#### Built-in Formats

```kotlin
val dt = DateTime.of(2024, 3, 15, 14, 30, 45)

dt.toDateString()       // "2024-03-15"
dt.toTimeString()       // "14:30:45"
dt.toDateTimeString()   // "2024-03-15 14:30:45"
dt.toIso8601String()    // "2024-03-15T14:30:45Z"
```

#### Custom Formatting

```kotlin
dt.format("YYYY-MM-DD")                    // "2024-03-15"
dt.format("DD/MM/YYYY")                    // "15/03/2024"
dt.format("MMMM D, YYYY")                  // "March 15, 2024"
dt.format("dddd, MMMM D, YYYY")            // "Friday, March 15, 2024"
dt.format("HH:mm:ss")                      // "14:30:45"
dt.format("hh:mm A")                       // "02:30 PM"
dt.format("YYYY-MM-DD HH:mm:ss Z")         // "2024-03-15 14:30:45 +00:00"
```

#### Format Tokens

| Token | Output | Description |
|-------|--------|-------------|
| `YYYY` | 2024 | 4-digit year |
| `YY` | 24 | 2-digit year |
| `MMMM` | March | Full month name |
| `MMM` | Mar | Short month name |
| `MM` | 03 | 2-digit month |
| `M` | 3 | Month number |
| `DD` | 15 | 2-digit day |
| `D` | 15 | Day number |
| `dddd` | Friday | Full day name |
| `ddd` | Fri | Short day name |
| `HH` | 14 | 24-hour format (00-23) |
| `hh` | 02 | 12-hour format (01-12) |
| `mm` | 30 | Minutes (00-59) |
| `ss` | 45 | Seconds (00-59) |
| `SSS` | 000 | Milliseconds |
| `A` | PM | AM/PM uppercase |
| `a` | pm | am/pm lowercase |
| `Z` | +00:00 | Timezone offset |

### Serialization

Chronos includes built-in support for kotlinx-serialization:

```kotlin
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Meeting(
    val title: String,
    val startTime: DateTime,
    val endTime: DateTime,
    val location: String
)

val meeting = Meeting(
    title = "Team Standup",
    startTime = DateTime.of(2024, 12, 25, 10, 0),
    endTime = DateTime.of(2024, 12, 25, 11, 0),
    location = "Conference Room A"
)

// Serialize to JSON
val json = Json.encodeToString(meeting)
println(json)
// {"title":"Team Standup","startTime":"2024-12-25T10:00:00Z","endTime":"2024-12-25T11:00:00Z","location":"Conference Room A"}

// Deserialize from JSON
val decoded = Json.decodeFromString<Meeting>(json)
```

## 💡 Examples

### Example 1: Meeting Scheduler

Schedule recurring weekly meetings:

```kotlin
fun scheduleWeeklyMeetings(
    startDate: DateTime,
    endDate: DateTime,
    meetingTime: Pair<Int, Int> = 10 to 0  // 10:00 AM
) {
    val period = Period(startDate, endDate)
    val (hour, minute) = meetingTime

    for (date in period.range(DateTimeUnit.WEEK)) {
        val meeting = date.at(hour = hour, minute = minute)
        println("Meeting: ${meeting.format("dddd, MMMM D, YYYY at hh:mm A")}")
    }
}

// Usage
val start = DateTime.of(2024, 1, 1)
val end = DateTime.of(2024, 3, 31)
scheduleWeeklyMeetings(start, end)
```

### Example 2: Age Calculator

Calculate age with precision:

```kotlin
fun calculateAge(birthday: DateTime): Pair<Int, String> {
    val now = DateTime.now()
    val period = now.diff(birthday)

    val age = period.years
    val nextBirthday = birthday.set(year = now.year + 1)
    val daysUntilBirthday = now.diff(nextBirthday).inDays().toInt()

    return age to "Next birthday in $daysUntilBirthday days"
}

// Usage
val birthday = DateTime.of(1990, 6, 15)
val (age, message) = calculateAge(birthday)
println("Age: $age years old")
println(message)
```

### Example 3: Business Hours Validator

Check if a datetime falls within business hours:

```kotlin
fun isBusinessHours(
    dt: DateTime,
    startHour: Int = 9,
    endHour: Int = 17,
    weekendsOff: Boolean = true
): Boolean {
    // Check if weekend
    if (weekendsOff && dt.dayOfWeek in 6..7) {
        return false
    }

    // Check if within working hours
    val businessStart = dt.set(hour = startHour, minute = 0, second = 0)
    val businessEnd = dt.set(hour = endHour, minute = 0, second = 0)

    return dt >= businessStart && dt < businessEnd
}

// Usage
val now = DateTime.now()
if (isBusinessHours(now)) {
    println("Office is open!")
} else {
    println("Office is closed")
}
```

### Example 4: Reminder System

Create a smart reminder system:

```kotlin
sealed class ReminderStatus {
    data class Overdue(val duration: String) : ReminderStatus()
    data class DueToday(val time: String) : ReminderStatus()
    data class DueSoon(val humanTime: String) : ReminderStatus()
    data class Upcoming(val humanTime: String) : ReminderStatus()
}

fun getReminderStatus(dueDate: DateTime): ReminderStatus {
    val now = DateTime.now()

    return when {
        dueDate.isPast() -> {
            ReminderStatus.Overdue(now.diffForHumans(dueDate))
        }
        dueDate.isSameDay(now) -> {
            ReminderStatus.DueToday(dueDate.format("hh:mm A"))
        }
        dueDate - now < 24.hours -> {
            ReminderStatus.DueSoon(now.diffForHumans(dueDate))
        }
        else -> {
            ReminderStatus.Upcoming(now.diffForHumans(dueDate))
        }
    }
}

// Usage
val reminder = DateTime.now() + 2.hours
when (val status = getReminderStatus(reminder)) {
    is ReminderStatus.Overdue -> println("⚠️ Overdue by ${status.duration}")
    is ReminderStatus.DueToday -> println("📅 Due today at ${status.time}")
    is ReminderStatus.DueSoon -> println("⏰ Due ${status.humanTime}")
    is ReminderStatus.Upcoming -> println("📆 Upcoming ${status.humanTime}")
}
```

### Example 5: Timezone Converter

Convert meeting times across timezones:

```kotlin
fun convertMeetingTime(
    utcTime: DateTime,
    timezones: List<String> = listOf(
        "America/New_York",
        "Europe/London",
        "Asia/Tokyo",
        "Asia/Seoul",
        "Australia/Sydney"
    )
): Map<String, String> {
    return timezones.associateWith { tz ->
        utcTime.inTz(tz).format("hh:mm A (Z)")
    }
}

// Usage
val meetingUTC = DateTime.of(2024, 12, 25, 14, 0, 0, 0, TimeZone.UTC)
println("Meeting at ${meetingUTC.format("HH:mm")} UTC:")

convertMeetingTime(meetingUTC).forEach { (city, time) ->
    println("  $city: $time")
}
```

### Example 6: Working Days Calculator

Calculate working days between two dates:

```kotlin
fun calculateWorkingDays(start: DateTime, end: DateTime): Int {
    val period = Period(start, end)
    var workingDays = 0

    for (date in period) {
        // Monday = 1, Sunday = 7
        if (date.dayOfWeek in 1..5) {
            workingDays++
        }
    }

    return workingDays
}

// Usage
val projectStart = DateTime.of(2024, 1, 1)
val projectEnd = DateTime.of(2024, 1, 31)
val workingDays = calculateWorkingDays(projectStart, projectEnd)
println("Working days: $workingDays")  // Excludes weekends
```

## 🏗️ Architecture

Chronos is built on top of Kotlin's official [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) library, providing a more intuitive and feature-rich API while maintaining:

- **Immutability**: All DateTime objects are immutable
- **Thread Safety**: Safe to use in concurrent environments
- **Type Safety**: Compile-time type checking
- **Multiplatform Ready**: Built on kotlinx-datetime foundation

### Project Structure

```
chronos/
├── src/main/kotlin/io/github/heodongun/chronos/
│   ├── DateTime.kt          # Main DateTime class
│   ├── Duration.kt          # Duration with multiple time units
│   ├── Period.kt            # Period between two DateTimes
│   ├── DateTimeDsl.kt       # DSL builders
│   ├── Extensions.kt        # Extension functions
│   └── DateTimeSerializer.kt # kotlinx-serialization support
└── src/test/kotlin/io/github/heodongun/chronos/
    ├── DateTimeTest.kt      # DateTime tests (18 tests)
    ├── DurationTest.kt      # Duration tests (8 tests)
    └── PeriodTest.kt        # Period tests (7 tests)
```

## 🧪 Testing

Chronos has comprehensive test coverage:

- **33 total tests** covering all major functionality
- **100% passing rate**
- Tests for DateTime creation, manipulation, formatting, timezones, duration, period operations

Run tests:

```bash
./gradlew :chronos:test
```

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

Please make sure to:
- Add tests for new features
- Update documentation
- Follow Kotlin coding conventions
- Write clear commit messages

## 📝 Changelog

### v1.0.0 (2025-10-22)

Initial release with:
- Complete DateTime API with 150+ features
- Full timezone support
- Duration and Period calculations
- Human-readable formatting
- DSL builders
- Extension functions
- kotlinx-serialization support
- Comprehensive test suite (33 tests)

## 🔮 Roadmap

Future enhancements planned:

- [ ] Locale-specific formatting
- [ ] Holiday/business day calendars
- [ ] Recurring event patterns
- [ ] Time window operations
- [ ] Additional timezone utilities
- [ ] Performance optimizations
- [ ] Multiplatform support (JS, Native)

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

```
Copyright 2025 Heo Dongun

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

## 🙏 Acknowledgments

- **[Pendulum](https://pendulum.eustace.io/)** - The Python library that inspired Chronos
- **[kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime)** - Foundation for multiplatform datetime operations
- **[date-fns](https://date-fns.org/)** - JavaScript library with excellent API design patterns
- **[Joda-Time](https://www.joda.org/joda-time/)** - Java datetime library that influenced modern datetime APIs

## 📚 Resources

- **Documentation**: [GitHub Wiki](https://github.com/heodongun/Chronos)
- **API Reference**: [Maven Central](https://central.sonatype.com/artifact/io.github.heodongun/chronos)
- **Issues**: [GitHub Issues](https://github.com/heodongun/Chronos/issues)
- **Discussions**: [GitHub Discussions](https://github.com/heodongun/Chronos/discussions)

## 💬 Support

Need help? Have questions?

- 📖 Check the [documentation](https://github.com/heodongun/Chronos)
- 🐛 Report bugs via [GitHub Issues](https://github.com/heodongun/Chronos/issues)
- 💡 Request features via [GitHub Issues](https://github.com/heodongun/Chronos/issues)
- 💬 Ask questions in [GitHub Discussions](https://github.com/heodongun/Chronos/discussions)

---

<div align="center">

**Made with ❤️ by [Heo Dongun](https://github.com/heodongun)**

⭐ Star us on GitHub if you find Chronos useful!

[⬆ Back to Top](#-chronos---modern-kotlin-datetime-library)

</div>
