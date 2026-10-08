package com.noisefit.smartlearning.data

import android.content.Context
import com.noisefit.smartlearning.domain.Course
import com.noisefit.smartlearning.domain.Lesson
import org.json.JSONArray
import org.json.JSONObject

class SharedPreferencesCourseCache(context: Context) : CourseLocalDataSource {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun readCourses(): List<Course> {
        val rawCache = preferences.getString(KEY_COURSES, null) ?: return emptyList()

        return runCatching {
            val coursesJson = JSONArray(rawCache)
            (0 until coursesJson.length()).map { index ->
                coursesJson.getJSONObject(index).toCourse()
            }
        }.getOrDefault(emptyList())
    }

    override fun writeCourses(courses: List<Course>) {
        val coursesJson = JSONArray().apply {
            courses.forEach { put(it.toJson()) }
        }

        preferences.edit()
            .putString(KEY_COURSES, coursesJson.toString())
            .apply()
    }

    private fun JSONObject.toCourse(): Course {
        val lessonsJson = getJSONArray("lessons")
        val lessons = (0 until lessonsJson.length()).map { index ->
            lessonsJson.getJSONObject(index).toLesson()
        }

        return Course(
            id = getInt("id"),
            title = getString("title"),
            instructor = getString("instructor"),
            lessons = lessons
        )
    }

    private fun JSONObject.toLesson(): Lesson {
        return Lesson(
            id = getInt("id"),
            title = getString("title"),
            completed = getBoolean("completed")
        )
    }

    private fun Course.toJson(): JSONObject {
        return JSONObject()
            .put("id", id)
            .put("title", title)
            .put("instructor", instructor)
            .put(
                "lessons",
                JSONArray().apply {
                    lessons.forEach { put(it.toJson()) }
                }
            )
    }

    private fun Lesson.toJson(): JSONObject {
        return JSONObject()
            .put("id", id)
            .put("title", title)
            .put("completed", completed)
    }

    private companion object {
        const val PREFERENCES_NAME = "smart_learning_cache"
        const val KEY_COURSES = "courses"
    }
}
