package io.github.heodongun.chronos

import kotlinx.datetime.TimeZone
import kotlin.test.*

class PeriodTest {

    @Test
    fun testPeriodCreation() {
        val dt1 = DateTime.of(2024, 3, 15, 14, 30, 0)
        val dt2 = DateTime.of(2024, 3, 20, 14, 30, 0)

        val period = Period(dt1, dt2)
        assertEquals(5, period.days)
    }

    @Test
    fun testPeriodFromDiff() {
        val dt1 = DateTime.of(2024, 3, 15)
        val dt2 = DateTime.of(2024, 3, 20)

        val period = dt2.diff(dt1)
        assertEquals(5, period.days)
    }

    @Test
    fun testPeriodOperator() {
        val dt1 = DateTime.of(2024, 3, 15)
        val dt2 = DateTime.of(2024, 3, 20)

        val period = dt2 - dt1
        assertEquals(5, period.days)
    }

    @Test
    fun testNegativePeriod() {
        val dt1 = DateTime.of(2024, 3, 20)
        val dt2 = DateTime.of(2024, 3, 15)

        val period = Period(dt1, dt2)
        assertTrue(period.isNegative())
    }

    @Test
    fun testPeriodConversions() {
        val dt1 = DateTime.of(2024, 3, 15, 12, 0, 0)
        val dt2 = DateTime.of(2024, 3, 15, 14, 0, 0)

        val period = Period(dt1, dt2)
        assertEquals(2.0, period.inHours())
        assertEquals(120.0, period.inMinutes())
    }

    @Test
    fun testToHumanString() {
        val now = DateTime.now()
        val future = now.add(days = 3, hours = 2)

        val period = Period(now, future)
        val humanString = period.toHumanString(true)
        assertTrue(humanString.contains("day"))
    }

    @Test
    fun testContains() {
        val dt1 = DateTime.of(2024, 3, 15)
        val dt2 = DateTime.of(2024, 3, 20)
        val dt3 = DateTime.of(2024, 3, 17)

        val period = Period(dt1, dt2)
        assertTrue(dt3 in period)
        assertFalse(DateTime.of(2024, 3, 21) in period)
    }

    @Test
    fun testRange() {
        val dt1 = DateTime.of(2024, 3, 15)
        val dt2 = DateTime.of(2024, 3, 20)

        val period = Period(dt1, dt2)
        val dates = period.range().toList()

        assertTrue(dates.size >= 5)
        assertEquals(dt1.day, dates.first().day)
    }

    @Test
    fun testIterator() {
        val dt1 = DateTime.of(2024, 3, 15)
        val dt2 = DateTime.of(2024, 3, 18)

        val period = Period(dt1, dt2)
        var count = 0
        for (dt in period) {
            count++
            if (count > 5) break // Safety limit
        }
        assertTrue(count > 0)
    }
}
