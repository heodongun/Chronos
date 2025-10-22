package io.github.heodongun.chronos

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable
import kotlin.time.Duration as KotlinDuration

/**
 * Immutable DateTime class inspired by Python's Pendulum library.
 *
 * Provides comprehensive date and time operations with timezone support,
 * human-readable formatting, and Kotlin DSL features.
 */
@Serializable(with = DateTimeSerializer::class)
data class DateTime internal constructor(
    private val instant: Instant,
    private val timeZone: TimeZone
) : Comparable<DateTime> {

    private val localDateTime: LocalDateTime by lazy {
        instant.toLocalDateTime(timeZone)
    }

    // ========== Properties ==========

    val year: Int get() = localDateTime.year
    val month: Int get() = localDateTime.monthNumber
    val day: Int get() = localDateTime.dayOfMonth
    val hour: Int get() = localDateTime.hour
    val minute: Int get() = localDateTime.minute
    val second: Int get() = localDateTime.second
    val nanosecond: Int get() = localDateTime.nanosecond
    val microsecond: Int get() = nanosecond / 1000

    val dayOfWeek: Int get() = localDateTime.dayOfWeek.isoDayNumber
    val dayOfYear: Int get() = localDateTime.dayOfYear
    val weekOfYear: Int
        get() {
            val firstDayOfYear = localDateTime.date.atStartOfDayIn(timeZone).toLocalDateTime(timeZone)
            val daysSinceStart = dayOfYear - 1
            val firstWeekOffset = (firstDayOfYear.dayOfWeek.isoDayNumber - 1)
            return ((daysSinceStart + firstWeekOffset) / 7) + 1
        }

    val daysInMonth: Int get() {
        val nextMonth = localDateTime.date.plus(1, kotlinx.datetime.DateTimeUnit.MONTH)
        return localDateTime.date.daysUntil(LocalDate(nextMonth.year, nextMonth.month, 1))
    }

    val quarter: Int get() = (month - 1) / 3 + 1

    val tz: TimeZone get() = timeZone
    val timezone: TimeZone get() = timeZone
    val timezoneName: String get() = timeZone.id

    val offset: Int
        get() = timeZone.offsetAt(instant).totalSeconds

    val offsetHours: Double
        get() = offset / 3600.0

    val timestamp: Long get() = instant.epochSeconds
    val timestampMillis: Long get() = instant.toEpochMilliseconds()

    // ========== Comparison Methods ==========

    override fun compareTo(other: DateTime): Int = instant.compareTo(other.instant)

    fun isPast(): Boolean = this < now()
    fun isFuture(): Boolean = this > now()

    fun isLeapYear(): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    fun isSameDay(other: DateTime): Boolean {
        val otherLocal = other.inTimezone(timeZone).localDateTime
        return localDateTime.date == otherLocal.date
    }

    fun isBirthday(birthday: DateTime): Boolean {
        val birthdayLocal = birthday.inTimezone(timeZone).localDateTime
        return localDateTime.monthNumber == birthdayLocal.monthNumber &&
               localDateTime.dayOfMonth == birthdayLocal.dayOfMonth
    }

    fun isDst(): Boolean {
        // kotlinx-datetime doesn't provide direct DST info, approximate based on offset
        val winterDate = LocalDateTime(year, 1, 1, 12, 0)
            .toInstant(timeZone)
        val summerDate = LocalDateTime(year, 7, 1, 12, 0)
            .toInstant(timeZone)

        val winterOffset = timeZone.offsetAt(winterDate).totalSeconds
        val summerOffset = timeZone.offsetAt(summerDate).totalSeconds

        return if (winterOffset != summerOffset) {
            offset == summerOffset
        } else {
            false
        }
    }

    fun isLocal(): Boolean = timeZone == TimeZone.currentSystemDefault()
    fun isUtc(): Boolean = timeZone == TimeZone.UTC

    // ========== Timezone Operations ==========

    fun inTimezone(tz: TimeZone): DateTime = DateTime(instant, tz)
    fun inTimezone(tzString: String): DateTime = inTimezone(TimeZone.of(tzString))
    fun inTz(tz: TimeZone): DateTime = inTimezone(tz)
    fun inTz(tzString: String): DateTime = inTimezone(tzString)

    // ========== Modification Methods ==========

    fun set(
        year: Int? = null,
        month: Int? = null,
        day: Int? = null,
        hour: Int? = null,
        minute: Int? = null,
        second: Int? = null,
        nanosecond: Int? = null,
        tz: TimeZone? = null
    ): DateTime {
        val newLocalDateTime = LocalDateTime(
            year ?: this.year,
            month ?: this.month,
            day ?: this.day,
            hour ?: this.hour,
            minute ?: this.minute,
            second ?: this.second,
            nanosecond ?: this.nanosecond
        )
        val newTz = tz ?: timeZone
        return DateTime(newLocalDateTime.toInstant(newTz), newTz)
    }

    fun on(year: Int? = null, month: Int? = null, day: Int? = null): DateTime =
        set(year = year, month = month, day = day)

    fun at(hour: Int? = null, minute: Int? = null, second: Int? = null, nanosecond: Int? = null): DateTime =
        set(hour = hour, minute = minute, second = second, nanosecond = nanosecond)

    fun startOf(unit: DateTimeUnit): DateTime = when (unit) {
        DateTimeUnit.SECOND -> set(nanosecond = 0)
        DateTimeUnit.MINUTE -> set(second = 0, nanosecond = 0)
        DateTimeUnit.HOUR -> set(minute = 0, second = 0, nanosecond = 0)
        DateTimeUnit.DAY -> set(hour = 0, minute = 0, second = 0, nanosecond = 0)
        DateTimeUnit.WEEK -> {
            val daysToSubtract = (dayOfWeek - 1)
            add(days = -daysToSubtract).set(hour = 0, minute = 0, second = 0, nanosecond = 0)
        }
        DateTimeUnit.MONTH -> set(day = 1, hour = 0, minute = 0, second = 0, nanosecond = 0)
        DateTimeUnit.QUARTER -> {
            val firstMonthOfQuarter = ((quarter - 1) * 3) + 1
            set(month = firstMonthOfQuarter, day = 1, hour = 0, minute = 0, second = 0, nanosecond = 0)
        }
        DateTimeUnit.YEAR -> set(month = 1, day = 1, hour = 0, minute = 0, second = 0, nanosecond = 0)
        else -> this
    }

    fun endOf(unit: DateTimeUnit): DateTime = when (unit) {
        DateTimeUnit.SECOND -> set(nanosecond = 999_999_999)
        DateTimeUnit.MINUTE -> set(second = 59, nanosecond = 999_999_999)
        DateTimeUnit.HOUR -> set(minute = 59, second = 59, nanosecond = 999_999_999)
        DateTimeUnit.DAY -> set(hour = 23, minute = 59, second = 59, nanosecond = 999_999_999)
        DateTimeUnit.WEEK -> {
            val daysToAdd = 7 - dayOfWeek
            add(days = daysToAdd).set(hour = 23, minute = 59, second = 59, nanosecond = 999_999_999)
        }
        DateTimeUnit.MONTH -> {
            val lastDay = daysInMonth
            set(day = lastDay, hour = 23, minute = 59, second = 59, nanosecond = 999_999_999)
        }
        DateTimeUnit.QUARTER -> {
            val lastMonthOfQuarter = quarter * 3
            val temp = set(month = lastMonthOfQuarter)
            temp.set(day = temp.daysInMonth, hour = 23, minute = 59, second = 59, nanosecond = 999_999_999)
        }
        DateTimeUnit.YEAR -> set(month = 12, day = 31, hour = 23, minute = 59, second = 59, nanosecond = 999_999_999)
        else -> this
    }

    fun next(dayOfWeek: Int? = null, keepTime: Boolean = false): DateTime {
        val targetDay = dayOfWeek ?: ((this.dayOfWeek % 7) + 1)
        val daysToAdd = if (targetDay > this.dayOfWeek) {
            targetDay - this.dayOfWeek
        } else {
            7 - this.dayOfWeek + targetDay
        }
        val result = add(days = daysToAdd)
        return if (keepTime) result else result.set(hour = 0, minute = 0, second = 0, nanosecond = 0)
    }

    fun previous(dayOfWeek: Int? = null, keepTime: Boolean = false): DateTime {
        val targetDay = dayOfWeek ?: (if (this.dayOfWeek == 1) 7 else this.dayOfWeek - 1)
        val daysToSubtract = if (targetDay < this.dayOfWeek) {
            this.dayOfWeek - targetDay
        } else {
            this.dayOfWeek + 7 - targetDay
        }
        val result = subtract(days = daysToSubtract)
        return if (keepTime) result else result.set(hour = 0, minute = 0, second = 0, nanosecond = 0)
    }

    // ========== Arithmetic Operations ==========

    fun add(
        years: Int = 0,
        months: Int = 0,
        weeks: Int = 0,
        days: Int = 0,
        hours: Int = 0,
        minutes: Int = 0,
        seconds: Long = 0,
        nanoseconds: Long = 0
    ): DateTime {
        var newDateTime = localDateTime

        if (years != 0) newDateTime = newDateTime.date.plus(years, kotlinx.datetime.DateTimeUnit.YEAR)
            .atTime(newDateTime.hour, newDateTime.minute, newDateTime.second, newDateTime.nanosecond)
        if (months != 0) newDateTime = newDateTime.date.plus(months, kotlinx.datetime.DateTimeUnit.MONTH)
            .atTime(newDateTime.hour, newDateTime.minute, newDateTime.second, newDateTime.nanosecond)

        val totalDays = weeks * 7L + days
        val totalNanos = hours * 3_600_000_000_000L +
                         minutes * 60_000_000_000L +
                         seconds * 1_000_000_000L +
                         nanoseconds +
                         totalDays * 86_400_000_000_000L

        val newInstant = newDateTime.toInstant(timeZone) + KotlinDuration.parse("${totalNanos}ns")
        return DateTime(newInstant, timeZone)
    }

    fun subtract(
        years: Int = 0,
        months: Int = 0,
        weeks: Int = 0,
        days: Int = 0,
        hours: Int = 0,
        minutes: Int = 0,
        seconds: Long = 0,
        nanoseconds: Long = 0
    ): DateTime = add(
        years = -years,
        months = -months,
        weeks = -weeks,
        days = -days,
        hours = -hours,
        minutes = -minutes,
        seconds = -seconds,
        nanoseconds = -nanoseconds
    )

    operator fun plus(duration: Duration): DateTime = add(
        years = duration.years,
        months = duration.months,
        days = duration.days,
        hours = duration.hours,
        minutes = duration.minutes,
        seconds = duration.seconds.toLong(),
        nanoseconds = duration.nanoseconds
    )

    operator fun minus(duration: Duration): DateTime = subtract(
        years = duration.years,
        months = duration.months,
        days = duration.days,
        hours = duration.hours,
        minutes = duration.minutes,
        seconds = duration.seconds.toLong(),
        nanoseconds = duration.nanoseconds
    )

    operator fun minus(other: DateTime): Period = Period(this, other)

    fun diff(other: DateTime, absolute: Boolean = false): Period {
        val period = Period(this, other)
        return if (absolute && period.isNegative()) Period(other, this) else period
    }

    fun diffForHumans(
        other: DateTime? = null,
        absolute: Boolean = false
    ): String {
        val reference = other ?: now()
        val period = diff(reference, absolute)
        return period.toHumanString(reference < this)
    }

    // ========== Formatting Methods ==========

    fun toDateString(): String = String.format("%04d-%02d-%02d", year, month, day)

    fun toTimeString(): String = String.format("%02d:%02d:%02d", hour, minute, second)

    fun toDateTimeString(): String = "${toDateString()} ${toTimeString()}"

    fun toIso8601String(): String = instant.toString()

    fun format(pattern: String): String {
        return pattern
            .replace("YYYY", "%04d".format(year))
            .replace("YY", "%02d".format(year % 100))
            .replace("MMMM", getMonthName(month))
            .replace("MMM", getMonthName(month).substring(0, 3))
            .replace("MM", "%02d".format(month))
            .replace("M", month.toString())
            .replace("DD", "%02d".format(day))
            .replace("D", day.toString())
            .replace("dddd", getDayName(dayOfWeek))
            .replace("ddd", getDayName(dayOfWeek).substring(0, 3))
            .replace("HH", "%02d".format(hour))
            .replace("hh", "%02d".format(if (hour == 0) 12 else if (hour > 12) hour - 12 else hour))
            .replace("mm", "%02d".format(minute))
            .replace("ss", "%02d".format(second))
            .replace("SSS", "%03d".format(nanosecond / 1_000_000))
            .replace("A", if (hour < 12) "AM" else "PM")
            .replace("a", if (hour < 12) "am" else "pm")
            .replace("Z", formatOffset())
    }

    private fun formatOffset(): String {
        val totalMinutes = offset / 60
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return String.format("%+03d:%02d", hours, kotlin.math.abs(minutes))
    }

    private fun getMonthName(month: Int): String = when (month) {
        1 -> "January"; 2 -> "February"; 3 -> "March"; 4 -> "April"
        5 -> "May"; 6 -> "June"; 7 -> "July"; 8 -> "August"
        9 -> "September"; 10 -> "October"; 11 -> "November"; 12 -> "December"
        else -> "Unknown"
    }

    private fun getDayName(dayOfWeek: Int): String = when (dayOfWeek) {
        1 -> "Monday"; 2 -> "Tuesday"; 3 -> "Wednesday"; 4 -> "Thursday"
        5 -> "Friday"; 6 -> "Saturday"; 7 -> "Sunday"
        else -> "Unknown"
    }

    override fun toString(): String = toIso8601String()

    companion object {
        fun now(tz: TimeZone = TimeZone.currentSystemDefault()): DateTime =
            DateTime(Clock.System.now(), tz)

        fun today(tz: TimeZone = TimeZone.currentSystemDefault()): DateTime =
            now(tz).startOf(DateTimeUnit.DAY)

        fun tomorrow(tz: TimeZone = TimeZone.currentSystemDefault()): DateTime =
            today(tz).add(days = 1)

        fun yesterday(tz: TimeZone = TimeZone.currentSystemDefault()): DateTime =
            today(tz).subtract(days = 1)

        fun of(
            year: Int,
            month: Int,
            day: Int,
            hour: Int = 0,
            minute: Int = 0,
            second: Int = 0,
            nanosecond: Int = 0,
            tz: TimeZone = TimeZone.currentSystemDefault()
        ): DateTime {
            val localDateTime = LocalDateTime(year, month, day, hour, minute, second, nanosecond)
            return DateTime(localDateTime.toInstant(tz), tz)
        }

        fun fromTimestamp(timestamp: Long, tz: TimeZone = TimeZone.UTC): DateTime =
            DateTime(Instant.fromEpochSeconds(timestamp), tz)

        fun fromTimestampMillis(timestampMillis: Long, tz: TimeZone = TimeZone.UTC): DateTime =
            DateTime(Instant.fromEpochMilliseconds(timestampMillis), tz)

        fun parse(dateTimeString: String, tz: TimeZone = TimeZone.UTC): DateTime {
            val instant = Instant.parse(dateTimeString)
            return DateTime(instant, tz)
        }
    }
}
