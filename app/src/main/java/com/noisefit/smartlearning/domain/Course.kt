package com.noisefit.smartlearning.domain

data class Lesson(
    val id: Int,
    val title: String,
    val completed: Boolean
)

data class Course(
    val id: Int,
    val title: String,
    val instructor: String,
    val lessons: List<Lesson>
) {
    val progress: Int
        get() = ProgressCalculator.calculate(lessons)

    val lessonCount: Int
        get() = lessons.size
}
