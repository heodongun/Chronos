package io.github.heodongun.chronos

import kotlinx.datetime.TimeZone
import kotlin.test.*

class DateTimeTest {

    @Test
    fun testDateTimeCreation() {
        val dt = DateTime.of(2024, 3, 15, 14, 30, 0, 0, TimeZone.UTC)
        assertEquals(2024, dt.year)
        assertEquals(3, dt.month)
        assertEquals(15, dt.day)
        assertEquals(14, dt.hour)
        assertEquals(30, dt.minute)
        assertEquals(0, dt.second)
    }

    @Test
    fun testNowTodayTomorrowYesterday() {
        val now = DateTime.now()
        val today = DateTime.today()
        val tomorrow = DateTime.tomorrow()
        val yesterday = DateTime.yesterday()

        assertTrue(now >= today)
        assertTrue(tomorrow > today)
        assertTrue(yesterday < today)
        assertEquals(1, tomorrow.diff(today, true).days)
        assertEquals(1, today.diff(yesterday, true).days)
    }

    @Test
    fun testFromTimestamp() {
        val timestamp = 1710511800L // 2024-03-15 14:30:00 UTC
        val dt = DateTime.fromTimestamp(timestamp, TimeZone.UTC)
        assertEquals(2024, dt.year)
        assertEquals(3, dt.month)
        assertEquals(15, dt.day)
    }

    @Test
    fun testProperties() {
        val dt = DateTime.of(2024, 6, 15, 14, 30, 45, 0, TimeZone.UTC)
        assertEquals(2024, dt.year)
        assertEquals(6, dt.month)
        assertEquals(15, dt.day)
        assertEquals(14, dt.hour)
        assertEquals(30, dt.minute)
        assertEquals(45, dt.second)
        assertEquals(2, dt.quarter)
        assertTrue(dt.dayOfWeek in 1..7)
        assertTrue(dt.dayOfYear > 0)
    }

    @Test
    fun testLeapYear() {
        assertTrue(DateTime.of(2024, 1, 1).isLeapYear())
        assertFalse(DateTime.of(2023, 1, 1).isLeapYear())
        assertTrue(DateTime.of(2000, 1, 1).isLeapYear())
        assertFalse(DateTime.of(1900, 1, 1).isLeapYear())
    }

    @Test
    fun testComparison() {
        val dt1 = DateTime.of(2024, 3, 15)
        val dt2 = DateTime.of(2024, 3, 16)
        val dt3 = DateTime.of(2024, 3, 15)

        assertTrue(dt1 < dt2)
        assertTrue(dt2 > dt1)
        assertEquals(dt1, dt3)
        assertNotEquals(dt1, dt2)
    }

    @Test
    fun testTimezoneConversion() {
        val utcDt = DateTime.of(2024, 3, 15, 12, 0, 0, 0, TimeZone.UTC)
        val seoulDt = utcDt.inTimezone(TimeZone.of("Asia/Seoul"))

        assertEquals(utcDt.timestamp, seoulDt.timestamp)
        assertNotEquals(utcDt.hour, seoulDt.hour)
    }

    @Test
    fun testSet() {
        val dt = DateTime.of(2024, 3, 15, 14, 30, 0)
        val modified = dt.set(year = 2025, month = 6, day = 20)

        assertEquals(2025, modified.year)
        assertEquals(6, modified.month)
        assertEquals(20, modified.day)
        assertEquals(14, modified.hour) // unchanged
    }

    @Test
    fun testStartOfAndEndOf() {
        val dt = DateTime.of(2024, 3, 15, 14, 30, 45)

        val startOfDay = dt.startOf(DateTimeUnit.DAY)
        assertEquals(0, startOfDay.hour)
        assertEquals(0, startOfDay.minute)
        assertEquals(0, startOfDay.second)

        val endOfDay = dt.endOf(DateTimeUnit.DAY)
        assertEquals(23, endOfDay.hour)
        assertEquals(59, endOfDay.minute)
        assertEquals(59, endOfDay.second)

        val startOfMonth = dt.startOf(DateTimeUnit.MONTH)
        assertEquals(1, startOfMonth.day)
        assertEquals(0, startOfMonth.hour)

        val startOfYear = dt.startOf(DateTimeUnit.YEAR)
        assertEquals(1, startOfYear.month)
        assertEquals(1, startOfYear.day)
    }

    @Test
    fun testAddSubtract() {
        val dt = DateTime.of(2024, 3, 15, 14, 30, 0)

        val added = dt.add(days = 5, hours = 3, minutes = 30)
        assertEquals(20, added.day)
        assertEquals(18, added.hour)
        assertEquals(0, added.minute)

        val subtracted = dt.subtract(days = 5, hours = 3, minutes = 30)
        assertEquals(10, subtracted.day)
        assertEquals(11, subtracted.hour)
        assertEquals(0, subtracted.minute)
    }

    @Test
    fun testArithmeticOperators() {
        val dt = DateTime.of(2024, 3, 15, 14, 30, 0)
        val duration = Duration(days = 5, hours = 3)

        val result1 = dt + duration
        assertEquals(20, result1.day)

        val result2 = dt - duration
        assertEquals(10, result2.day)
    }

    @Test
    fun testDiff() {
        val dt1 = DateTime.of(2024, 3, 15, 14, 30, 0)
        val dt2 = DateTime.of(2024, 3, 20, 14, 30, 0)

        val period = dt2.diff(dt1)
        assertEquals(5, period.days)
    }

    @Test
    fun testDiffForHumans() {
        val now = DateTime.now()
        val future = now.add(days = 3, hours = 2)
        val past = now.subtract(days = 5)

        val futureStr = now.diffForHumans(future)
        assertTrue(futureStr.contains("day"))

        val pastStr = now.diffForHumans(past)
        assertTrue(pastStr.contains("day"))
    }

    @Test
    fun testFormatting() {
        val dt = DateTime.of(2024, 3, 15, 14, 30, 45, 0, TimeZone.UTC)

        assertEquals("2024-03-15", dt.toDateString())
        assertEquals("14:30:45", dt.toTimeString())
        assertEquals("2024-03-15 14:30:45", dt.toDateTimeString())

        val formatted = dt.format("YYYY-MM-DD HH:mm:ss")
        assertEquals("2024-03-15 14:30:45", formatted)

        val formatted2 = dt.format("DD/MM/YYYY")
        assertEquals("15/03/2024", formatted2)
    }

    @Test
    fun testIsSameDay() {
        val dt1 = DateTime.of(2024, 3, 15, 10, 0, 0)
        val dt2 = DateTime.of(2024, 3, 15, 20, 0, 0)
        val dt3 = DateTime.of(2024, 3, 16, 10, 0, 0)

        assertTrue(dt1.isSameDay(dt2))
        assertFalse(dt1.isSameDay(dt3))
    }

    @Test
    fun testNextAndPrevious() {
        val dt = DateTime.of(2024, 3, 15) // Friday

        val nextMonday = dt.next(1) // Monday
        assertTrue(nextMonday > dt)

        val previousMonday = dt.previous(1)
        assertTrue(previousMonday < dt)
    }

    @Test
    fun testExtensions() {
        val dt = "2024-03-15T14:30:00Z".toDateTimeOrNull()
        assertNotNull(dt)
        assertEquals(2024, dt.year)

        val timestamp = 1710511800L
        val dtFromTimestamp = timestamp.toDateTime()
        assertNotNull(dtFromTimestamp)
    }

    @Test
    fun testDslBuilder() {
        val dt = dateTime {
            year = 2024
            month = 3
            day = 15
            hour = 14
            minute = 30
        }

        assertEquals(2024, dt.year)
        assertEquals(3, dt.month)
        assertEquals(15, dt.day)
        assertEquals(14, dt.hour)
        assertEquals(30, dt.minute)
    }
}
