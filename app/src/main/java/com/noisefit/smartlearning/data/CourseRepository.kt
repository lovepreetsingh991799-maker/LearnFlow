package com.noisefit.smartlearning.data

import com.noisefit.smartlearning.domain.Course

class CourseRepository(
    private val api: CourseApi,
    private val localDataSource: CourseLocalDataSource
) {
    suspend fun loadCourses(
        forceRefresh: Boolean = false,
        simulateFailure: Boolean = false
    ): CourseLoadResult {
        val cachedCourses = localDataSource.readCourses()

        if (!forceRefresh && cachedCourses.isNotEmpty()) {
            return CourseLoadResult.Success(cachedCourses, fromCache = true)
        }

        return runCatching {
            api.fetchCourses(simulateFailure = simulateFailure)
        }.fold(
            onSuccess = { courses ->
                localDataSource.writeCourses(courses)
                CourseLoadResult.Success(courses, fromCache = false)
            },
            onFailure = {
                if (cachedCourses.isNotEmpty()) {
                    CourseLoadResult.Success(cachedCourses, fromCache = true)
                } else {
                    CourseLoadResult.Error("Unable to load courses. Please try again.")
                }
            }
        )
    }

    fun completeLesson(courseId: Int, lessonId: Int): List<Course> {
        val updatedCourses = localDataSource.readCourses().map { course ->
            if (course.id != courseId) {
                course
            } else {
                course.copy(
                    lessons = course.lessons.map { lesson ->
                        if (lesson.id == lessonId) lesson.copy(completed = true) else lesson
                    }
                )
            }
        }

        localDataSource.writeCourses(updatedCourses)
        return updatedCourses
    }
}

sealed interface CourseLoadResult {
    data class Success(
        val courses: List<Course>,
        val fromCache: Boolean
    ) : CourseLoadResult

    data class Error(
        val message: String
    ) : CourseLoadResult
}
