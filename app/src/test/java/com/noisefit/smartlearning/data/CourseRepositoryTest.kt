package com.noisefit.smartlearning.data

import com.noisefit.smartlearning.domain.Course
import com.noisefit.smartlearning.domain.Lesson
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CourseRepositoryTest {
    @Test
    fun loadCoursesUsesCacheBeforeApiWhenNotForced() = runBlocking {
        val cachedCourses = listOf(courseWithCompletedLesson(lessonThreeCompleted = true))
        val api = FakeCourseApi(remoteCourses = listOf(courseWithCompletedLesson(false)))
        val localDataSource = FakeCourseLocalDataSource(cachedCourses)
        val repository = CourseRepository(api, localDataSource)

        val result = repository.loadCourses()

        assertTrue(result is CourseLoadResult.Success)
        assertEquals(true, (result as CourseLoadResult.Success).fromCache)
        assertEquals(0, api.callCount)
        assertTrue(result.courses.first().lessons.first { it.id == 3 }.completed)
    }

    @Test
    fun forceRefreshKeepsCompletedLessonsFromCache() = runBlocking {
        val api = FakeCourseApi(remoteCourses = listOf(courseWithCompletedLesson(false)))
        val localDataSource = FakeCourseLocalDataSource(
            initialCourses = listOf(courseWithCompletedLesson(lessonThreeCompleted = true))
        )
        val repository = CourseRepository(api, localDataSource)

        val result = repository.loadCourses(forceRefresh = true)

        assertTrue(result is CourseLoadResult.Success)
        assertEquals(false, (result as CourseLoadResult.Success).fromCache)
        assertTrue(result.courses.first().lessons.first { it.id == 3 }.completed)
        assertTrue(localDataSource.courses.first().lessons.first { it.id == 3 }.completed)
    }

    private fun courseWithCompletedLesson(lessonThreeCompleted: Boolean): Course {
        return Course(
            id = 1,
            title = "Python Programming",
            instructor = "John Smith",
            lessons = listOf(
                Lesson(id = 1, title = "Introduction", completed = true),
                Lesson(id = 2, title = "Variables & Data Types", completed = true),
                Lesson(id = 3, title = "Functions", completed = lessonThreeCompleted)
            )
        )
    }
}

private class FakeCourseApi(
    private val remoteCourses: List<Course>
) : CourseApi {
    var callCount = 0
        private set

    override suspend fun fetchCourses(simulateFailure: Boolean): List<Course> {
        callCount += 1
        if (simulateFailure) error("Fake API failure")
        return remoteCourses
    }
}

private class FakeCourseLocalDataSource(
    initialCourses: List<Course>
) : CourseLocalDataSource {
    var courses = initialCourses
        private set

    override fun readCourses(): List<Course> = courses

    override fun writeCourses(courses: List<Course>) {
        this.courses = courses
    }
}
