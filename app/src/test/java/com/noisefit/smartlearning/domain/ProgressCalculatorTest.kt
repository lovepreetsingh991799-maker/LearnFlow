package com.noisefit.smartlearning.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class ProgressCalculatorTest {
    @Test
    fun calculateRoundsProgressToNearestFivePercent() {
        val lessons = listOf(
            Lesson(id = 1, title = "Intro", completed = true),
            Lesson(id = 2, title = "Basics", completed = true),
            Lesson(id = 3, title = "Practice", completed = false),
            Lesson(id = 4, title = "Project", completed = false),
            Lesson(id = 5, title = "Review", completed = false),
            Lesson(id = 6, title = "Exam", completed = false)
        )

        assertEquals(35, ProgressCalculator.calculate(lessons))
    }

    @Test
    fun calculateReturnsZeroForEmptyLessonList() {
        assertEquals(0, ProgressCalculator.calculate(emptyList()))
    }
}
