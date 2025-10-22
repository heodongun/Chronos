package io.github.heodongun.chronos

import kotlin.test.*

class DurationTest {

    @Test
    fun testDurationCreation() {
        val duration = Duration(days = 5, hours = 3, minutes = 30)
        assertEquals(5, duration.days)
        assertEquals(3, duration.hours)
        assertEquals(30, duration.minutes)
    }

    @Test
    fun testDurationExtensions() {
        val d1 = 5.days
        assertEquals(5, d1.days)

        val d2 = 3.hours
        assertEquals(3, d2.hours)

        val d3 = 30.minutes
        assertEquals(30, d3.minutes)
    }

    @Test
    fun testDurationAddition() {
        val d1 = 5.days
        val d2 = 3.hours
        val result = d1 + d2

        assertEquals(5, result.days)
        assertEquals(3, result.hours)
    }

    @Test
    fun testDurationSubtraction() {
        val d1 = Duration(days = 10, hours = 5)
        val d2 = Duration(days = 3, hours = 2)
        val result = d1 - d2

        assertEquals(7, result.days)
        assertEquals(3, result.hours)
    }

    @Test
    fun testDurationMultiplication() {
        val d1 = 3.days
        val result = d1 * 2

        assertEquals(6, result.days)
    }

    @Test
    fun testDurationNegation() {
        val d1 = 5.days
        val result = -d1

        assertEquals(-5, result.days)
    }

    @Test
    fun testTotalConversions() {
        val duration = Duration(days = 1, hours = 2, minutes = 30)

        assertTrue(duration.totalHours > 25.0, "Expected totalHours > 25.0, got ${duration.totalHours}")
        assertTrue(duration.totalMinutes > 1500.0, "Expected totalMinutes > 1500.0, got ${duration.totalMinutes}")
        assertTrue(duration.inHours >= 25, "Expected inHours >= 25, got ${duration.inHours}")
    }

    @Test
    fun testInWords() {
        val d1 = Duration(years = 1, months = 2, days = 3)
        val words = d1.inWords()
        assertTrue(words.contains("year"))
        assertTrue(words.contains("month"))
        assertTrue(words.contains("day"))

        val d2 = Duration(hours = 5, minutes = 30)
        val words2 = d2.inWords()
        assertTrue(words2.contains("hour"))
        assertTrue(words2.contains("minute"))
    }

    @Test
    fun testDslBuilder() {
        val d = duration {
            days = 5
            hours = 3
            minutes = 30
        }

        assertEquals(5, d.days)
        assertEquals(3, d.hours)
        assertEquals(30, d.minutes)
    }
}
