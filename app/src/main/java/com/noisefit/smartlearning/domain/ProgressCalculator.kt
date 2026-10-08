package com.noisefit.smartlearning.domain

import kotlin.math.roundToInt

object ProgressCalculator {
    fun calculate(lessons: List<Lesson>): Int {
        return calculate(
            completedLessons = lessons.count { it.completed },
            totalLessons = lessons.size
        )
    }

    fun calculate(completedLessons: Int, totalLessons: Int): Int {
        if (totalLessons <= 0) return 0

        val safeCompleted = completedLessons.coerceIn(0, totalLessons)
        val rawProgress = safeCompleted * 100.0 / totalLessons
        return ((rawProgress / 5.0).roundToInt() * 5).coerceIn(0, 100)
    }
}
