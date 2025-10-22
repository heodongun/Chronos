package io.github.heodongun.chronos

import kotlinx.datetime.TimeZone

/**
 * Extension functions for convenient DateTime operations.
 */

// String extensions for parsing
fun String.toDateTime(tz: TimeZone = TimeZone.UTC): DateTime = DateTime.parse(this, tz)

fun String.toDateTimeOrNull(tz: TimeZone = TimeZone.UTC): DateTime? = try {
    DateTime.parse(this, tz)
} catch (e: Exception) {
    null
}

// Long extensions for timestamp conversion
fun Long.toDateTime(tz: TimeZone = TimeZone.UTC): DateTime = DateTime.fromTimestamp(this, tz)

fun Long.toDateTimeFromMillis(tz: TimeZone = TimeZone.UTC): DateTime = DateTime.fromTimestampMillis(this, tz)

// DateTime range operator
operator fun DateTime.rangeTo(other: DateTime): Period = Period(this, other)

// Convenient factory functions
fun dateTime(
    year: Int,
    month: Int,
    day: Int,
    hour: Int = 0,
    minute: Int = 0,
    second: Int = 0,
    nanosecond: Int = 0,
    tz: TimeZone = TimeZone.currentSystemDefault()
): DateTime = DateTime.of(year, month, day, hour, minute, second, nanosecond, tz)

fun now(tz: TimeZone = TimeZone.currentSystemDefault()): DateTime = DateTime.now(tz)

fun today(tz: TimeZone = TimeZone.currentSystemDefault()): DateTime = DateTime.today(tz)

fun tomorrow(tz: TimeZone = TimeZone.currentSystemDefault()): DateTime = DateTime.tomorrow(tz)

fun yesterday(tz: TimeZone = TimeZone.currentSystemDefault()): DateTime = DateTime.yesterday(tz)
