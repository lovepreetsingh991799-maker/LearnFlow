package com.noisefit.smartlearning.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.noisefit.smartlearning.data.CourseLoadResult
import com.noisefit.smartlearning.data.CourseRepository
import com.noisefit.smartlearning.domain.Course

data class CoursesUiState(
    val isLoading: Boolean = false,
    val courses: List<Course> = emptyList(),
    val selectedCourseId: Int? = null,
    val errorMessage: String? = null,
    val cacheNotice: String? = null
) {
    val selectedCourse: Course?
        get() = courses.firstOrNull { it.id == selectedCourseId }
}

class CoursesViewModel(
    private val repository: CourseRepository
) {
    var uiState by mutableStateOf(CoursesUiState())
        private set

    suspend fun loadCourses(
        forceRefresh: Boolean = false,
        simulateFailure: Boolean = false
    ) {
        uiState = uiState.copy(isLoading = true, errorMessage = null, cacheNotice = null)

        when (val result = repository.loadCourses(forceRefresh, simulateFailure)) {
            is CourseLoadResult.Success -> {
                uiState = uiState.copy(
                    isLoading = false,
                    courses = result.courses,
                    errorMessage = null,
                    cacheNotice = if (result.fromCache) "Showing saved courses" else null
                )
            }

            is CourseLoadResult.Error -> {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = result.message,
                    cacheNotice = null
                )
            }
        }
    }

    fun selectCourse(courseId: Int) {
        uiState = uiState.copy(selectedCourseId = courseId)
    }

    fun completeLesson(courseId: Int, lessonId: Int) {
        val updatedCourses = repository.completeLesson(courseId, lessonId)
        uiState = uiState.copy(courses = updatedCourses)
    }
}
