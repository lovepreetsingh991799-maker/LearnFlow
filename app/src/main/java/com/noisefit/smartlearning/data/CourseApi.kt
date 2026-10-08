package com.noisefit.smartlearning.data

import com.noisefit.smartlearning.domain.Course

interface CourseApi {
    suspend fun fetchCourses(simulateFailure: Boolean = false): List<Course>
}
