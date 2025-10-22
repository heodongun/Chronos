package io.github.heodongun.chronos

import kotlinx.serialization.Serializable
import kotlin.math.abs

/**
 * Represents a duration of time with various units.
 * Similar to Python's Pendulum Duration.
 */
@Serializable
data class Duration(
    val years: Int = 0,
    val months: Int = 0,
    val weeks: Int = 0,
    val days: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val nanoseconds: Long = 0
) {

    val totalDays: Double
        get() {
            val yearDays = years * 365.25
            val monthDays = months * 30.436875 // Average days per month
            val weekDays = weeks * 7.0
            val hourDays = hours / 24.0
            val minuteDays = minutes / 1440.0
            val secondDays = seconds / 86400.0
            val nanoDays = nanoseconds / 86400_000_000_000.0
            return yearDays + monthDays + weekDays + days + hourDays + minuteDays + secondDays + nanoDays
        }

    val totalHours: Double get() = totalDays * 24.0
    val totalMinutes: Double get() = totalHours * 60.0
    val totalSeconds: Double get() = totalMinutes * 60.0

    val inDays: Long get() = totalDays.toLong()
    val inHours: Long get() = totalHours.toLong()
    val inMinutes: Long get() = totalMinutes.toLong()
    val inSeconds: Long get() = totalSeconds.toLong()

    val remainingDays: Int
        get() = (totalDays % 7).toInt()

    val remainingSeconds: Long
        get() {
            val totalSecs = hours * 3600L + minutes * 60L + seconds
            return totalSecs % 60
        }

    fun inWords(): String = buildString {
        val parts = mutableListOf<String>()

        if (years != 0) parts.add("$years ${if (abs(years) == 1) "year" else "years"}")
        if (months != 0) parts.add("$months ${if (abs(months) == 1) "month" else "months"}")
        if (weeks != 0) parts.add("$weeks ${if (abs(weeks) == 1) "week" else "weeks"}")
        if (days != 0) parts.add("$days ${if (abs(days) == 1) "day" else "days"}")
        if (hours != 0) parts.add("$hours ${if (abs(hours) == 1) "hour" else "hours"}")
        if (minutes != 0) parts.add("$minutes ${if (abs(minutes) == 1) "minute" else "minutes"}")
        if (seconds != 0) parts.add("$seconds ${if (abs(seconds) == 1) "second" else "seconds"}")

        if (parts.isEmpty()) {
            append("0 seconds")
        } else {
            append(parts.joinToString(" "))
        }
    }

    operator fun plus(other: Duration): Duration = Duration(
        years = years + other.years,
        months = months + other.months,
        weeks = weeks + other.weeks,
        days = days + other.days,
        hours = hours + other.hours,
        minutes = minutes + other.minutes,
        seconds = seconds + other.seconds,
        nanoseconds = nanoseconds + other.nanoseconds
    )

    operator fun minus(other: Duration): Duration = Duration(
        years = years - other.years,
        months = months - other.months,
        weeks = weeks - other.weeks,
        days = days - other.days,
        hours = hours - other.hours,
        minutes = minutes - other.minutes,
        seconds = seconds - other.seconds,
        nanoseconds = nanoseconds - other.nanoseconds
    )

    operator fun times(factor: Int): Duration = Duration(
        years = years * factor,
        months = months * factor,
        weeks = weeks * factor,
        days = days * factor,
        hours = hours * factor,
        minutes = minutes * factor,
        seconds = seconds * factor,
        nanoseconds = nanoseconds * factor
    )

    operator fun unaryMinus(): Duration = Duration(
        years = -years,
        months = -months,
        weeks = -weeks,
        days = -days,
        hours = -hours,
        minutes = -minutes,
        seconds = -seconds,
        nanoseconds = -nanoseconds
    )

    companion object {
        fun duration(
            years: Int = 0,
            months: Int = 0,
            weeks: Int = 0,
            days: Int = 0,
            hours: Int = 0,
            minutes: Int = 0,
            seconds: Int = 0,
            nanoseconds: Long = 0
        ): Duration = Duration(years, months, weeks, days, hours, minutes, seconds, nanoseconds)
    }
}

// Extension properties for DSL-style duration creation
val Int.years: Duration get() = Duration(years = this)
val Int.months: Duration get() = Duration(months = this)
val Int.weeks: Duration get() = Duration(weeks = this)
val Int.days: Duration get() = Duration(days = this)
val Int.hours: Duration get() = Duration(hours = this)
val Int.minutes: Duration get() = Duration(minutes = this)
val Int.seconds: Duration get() = Duration(seconds = this)

val Long.years: Duration get() = Duration(years = this.toInt())
val Long.months: Duration get() = Duration(months = this.toInt())
val Long.weeks: Duration get() = Duration(weeks = this.toInt())
val Long.days: Duration get() = Duration(days = this.toInt())
val Long.hours: Duration get() = Duration(hours = this.toInt())
val Long.minutes: Duration get() = Duration(minutes = this.toInt())
val Long.seconds: Duration get() = Duration(seconds = this.toInt())
