package io.github.heodongun.chronos

import kotlinx.datetime.TimeZone

/**
 * DSL builder for creating DateTime objects with a fluent interface.
 */
@DslMarker
annotation class DateTimeDslMarker

@DateTimeDslMarker
class DateTimeBuilder {
    var year: Int = 2024
    var month: Int = 1
    var day: Int = 1
    var hour: Int = 0
    var minute: Int = 0
    var second: Int = 0
    var nanosecond: Int = 0
    var timezone: TimeZone = TimeZone.currentSystemDefault()

    fun build(): DateTime = DateTime.of(
        year = year,
        month = month,
        day = day,
        hour = hour,
        minute = minute,
        second = second,
        nanosecond = nanosecond,
        tz = timezone
    )
}

/**
 * DSL function for creating DateTime objects.
 *
 * Example:
 * ```kotlin
 * val dt = dateTime {
 *     year = 2024
 *     month = 3
 *     day = 15
 *     hour = 14
 *     minute = 30
 *     timezone = TimeZone.of("Asia/Seoul")
 * }
 * ```
 */
fun dateTime(block: DateTimeBuilder.() -> Unit): DateTime {
    val builder = DateTimeBuilder()
    builder.block()
    return builder.build()
}

@DateTimeDslMarker
class DurationBuilder {
    var years: Int = 0
    var months: Int = 0
    var weeks: Int = 0
    var days: Int = 0
    var hours: Int = 0
    var minutes: Int = 0
    var seconds: Int = 0
    var nanoseconds: Long = 0

    fun build(): Duration = Duration(
        years = years,
        months = months,
        weeks = weeks,
        days = days,
        hours = hours,
        minutes = minutes,
        seconds = seconds,
        nanoseconds = nanoseconds
    )
}

/**
 * DSL function for creating Duration objects.
 *
 * Example:
 * ```kotlin
 * val duration = duration {
 *     days = 5
 *     hours = 3
 *     minutes = 30
 * }
 * ```
 */
fun duration(block: DurationBuilder.() -> Unit): Duration {
    val builder = DurationBuilder()
    builder.block()
    return builder.build()
}
