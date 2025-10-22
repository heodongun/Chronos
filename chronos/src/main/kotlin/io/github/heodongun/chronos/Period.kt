package io.github.heodongun.chronos

import kotlin.math.abs

/**
 * Represents a period between two DateTime objects.
 * Provides human-readable time differences.
 */
data class Period(
    val start: DateTime,
    val end: DateTime
) {
    val years: Int
    val months: Int
    val days: Int
    val hours: Int
    val minutes: Int
    val seconds: Long
    val totalSeconds: Long

    init {
        val startInstant = start.timestamp
        val endInstant = end.timestamp
        totalSeconds = endInstant - startInstant

        var tempSeconds = abs(totalSeconds)

        years = (tempSeconds / (365.25 * 24 * 3600)).toInt()
        tempSeconds -= (years * 365.25 * 24 * 3600).toLong()

        months = (tempSeconds / (30.436875 * 24 * 3600)).toInt()
        tempSeconds -= (months * 30.436875 * 24 * 3600).toLong()

        days = (tempSeconds / (24 * 3600)).toInt()
        tempSeconds -= (days * 24 * 3600).toLong()

        hours = (tempSeconds / 3600).toInt()
        tempSeconds -= (hours * 3600).toLong()

        minutes = (tempSeconds / 60).toInt()
        seconds = tempSeconds % 60
    }

    fun isNegative(): Boolean = totalSeconds < 0

    fun inYears(): Double = totalSeconds / (365.25 * 24 * 3600)
    fun inMonths(): Double = totalSeconds / (30.436875 * 24 * 3600)
    fun inDays(): Double = totalSeconds / (24.0 * 3600)
    fun inHours(): Double = totalSeconds / 3600.0
    fun inMinutes(): Double = totalSeconds / 60.0
    fun inSeconds(): Long = totalSeconds

    fun toHumanString(future: Boolean = true): String {
        val absYears = abs(years)
        val absMonths = abs(months)
        val absDays = abs(days)
        val absHours = abs(hours)
        val absMinutes = abs(minutes)
        val absSeconds = abs(seconds)

        val parts = mutableListOf<String>()

        when {
            absYears > 0 -> {
                parts.add("$absYears ${if (absYears == 1) "year" else "years"}")
                if (absMonths > 0) parts.add("$absMonths ${if (absMonths == 1) "month" else "months"}")
            }
            absMonths > 0 -> {
                parts.add("$absMonths ${if (absMonths == 1) "month" else "months"}")
                if (absDays > 0) parts.add("$absDays ${if (absDays == 1) "day" else "days"}")
            }
            absDays > 0 -> {
                parts.add("$absDays ${if (absDays == 1) "day" else "days"}")
                if (absHours > 0) parts.add("$absHours ${if (absHours == 1) "hour" else "hours"}")
            }
            absHours > 0 -> {
                parts.add("$absHours ${if (absHours == 1) "hour" else "hours"}")
                if (absMinutes > 0) parts.add("$absMinutes ${if (absMinutes == 1) "minute" else "minutes"}")
            }
            absMinutes > 0 -> {
                parts.add("$absMinutes ${if (absMinutes == 1) "minute" else "minutes"}")
            }
            absSeconds > 0 -> {
                parts.add("$absSeconds ${if (absSeconds == 1L) "second" else "seconds"}")
            }
            else -> return "now"
        }

        val result = parts.take(2).joinToString(" ")

        return if (isNegative()) {
            "$result ago"
        } else {
            if (future) "in $result" else "$result ago"
        }
    }

    operator fun contains(dateTime: DateTime): Boolean {
        val timestamp = dateTime.timestamp
        return if (start.timestamp <= end.timestamp) {
            timestamp in start.timestamp..end.timestamp
        } else {
            timestamp in end.timestamp..start.timestamp
        }
    }

    fun range(unit: kotlinx.datetime.DateTimeUnit = kotlinx.datetime.DateTimeUnit.DAY, step: Int = 1): Sequence<DateTime> = sequence {
        var current = start
        while (if (start < end) current <= end else current >= end) {
            yield(current)
            current = when (unit) {
                is kotlinx.datetime.DateTimeUnit.DayBased -> current.add(days = step * if (start < end) 1 else -1)
                is kotlinx.datetime.DateTimeUnit.MonthBased -> current.add(months = step * if (start < end) 1 else -1)
                kotlinx.datetime.DateTimeUnit.HOUR -> current.add(hours = step * if (start < end) 1 else -1)
                kotlinx.datetime.DateTimeUnit.MINUTE -> current.add(minutes = step * if (start < end) 1 else -1)
                kotlinx.datetime.DateTimeUnit.SECOND -> current.add(seconds = step.toLong() * if (start < end) 1 else -1)
                else -> current.add(days = step * if (start < end) 1 else -1)
            }
        }
    }

    operator fun iterator(): Iterator<DateTime> = range().iterator()
}
