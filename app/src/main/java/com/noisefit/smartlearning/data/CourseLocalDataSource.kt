package com.noisefit.smartlearning.data

import com.noisefit.smartlearning.domain.Course

interface CourseLocalDataSource {
    fun readCourses(): List<Course>
    fun writeCourses(courses: List<Course>)
}
