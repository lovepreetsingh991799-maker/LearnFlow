package com.noisefit.smartlearning.data

import com.noisefit.smartlearning.domain.Course
import com.noisefit.smartlearning.domain.Lesson
import kotlinx.coroutines.delay

class MockCourseApi : CourseApi {
    override suspend fun fetchCourses(simulateFailure: Boolean): List<Course> {
        delay(NETWORK_DELAY_MS)

        if (simulateFailure) {
            error("Mock API failure")
        }

        return listOf(
            Course(
                id = 1,
                title = "Python Programming",
                instructor = "John Smith",
                lessons = buildLessons(
                    completedCount = 13,
                    totalCount = 20,
                    titles = listOf(
                        "Introduction",
                        "Variables & Data Types",
                        "Functions",
                        "OOP",
                        "Collections",
                        "Modules",
                        "Error Handling",
                        "File Operations"
                    )
                )
            ),
            Course(
                id = 2,
                title = "Generative AI",
                instructor = "Sarah Williams",
                lessons = buildLessons(
                    completedCount = 6,
                    totalCount = 16,
                    titles = listOf(
                        "AI Fundamentals",
                        "Prompt Design",
                        "Embeddings",
                        "Vector Search",
                        "Image Generation",
                        "Responsible AI"
                    )
                )
            ),
            Course(
                id = 3,
                title = "Full Stack Development",
                instructor = "David Brown",
                lessons = buildLessons(
                    completedCount = 7,
                    totalCount = 28,
                    titles = listOf(
                        "Web Architecture",
                        "Frontend Basics",
                        "API Design",
                        "Database Modeling",
                        "Authentication",
                        "Deployment",
                        "Monitoring"
                    )
                )
            ),
            Course(
                id = 4,
                title = "Android Jetpack Compose",
                instructor = "Priya Nair",
                lessons = buildLessons(
                    completedCount = 8,
                    totalCount = 18,
                    titles = listOf(
                        "Compose Basics",
                        "Layouts",
                        "Material 3",
                        "State Management",
                        "Navigation",
                        "ViewModels",
                        "Persistence",
                        "Testing"
                    )
                )
            ),
            Course(
                id = 5,
                title = "iOS SwiftUI Essentials",
                instructor = "Michael Chen",
                lessons = buildLessons(
                    completedCount = 14,
                    totalCount = 22,
                    titles = listOf(
                        "Swift Basics",
                        "SwiftUI Views",
                        "State and Binding",
                        "NavigationStack",
                        "Networking",
                        "Local Storage",
                        "Combine",
                        "App Store Build"
                    )
                )
            ),
            Course(
                id = 6,
                title = "Data Science Foundations",
                instructor = "Ananya Rao",
                lessons = buildLessons(
                    completedCount = 9,
                    totalCount = 24,
                    titles = listOf(
                        "Data Cleaning",
                        "NumPy",
                        "Pandas",
                        "Visualization",
                        "Statistics",
                        "Regression",
                        "Classification",
                        "Model Evaluation"
                    )
                )
            ),
            Course(
                id = 7,
                title = "Cloud DevOps",
                instructor = "Robert Evans",
                lessons = buildLessons(
                    completedCount = 5,
                    totalCount = 18,
                    titles = listOf(
                        "Linux Basics",
                        "Git Workflows",
                        "Docker",
                        "CI/CD",
                        "Kubernetes",
                        "Cloud Monitoring",
                        "Infrastructure as Code",
                        "Release Strategy"
                    )
                )
            ),
            Course(
                id = 8,
                title = "UI/UX Product Design",
                instructor = "Emily Carter",
                lessons = buildLessons(
                    completedCount = 12,
                    totalCount = 30,
                    titles = listOf(
                        "Design Thinking",
                        "User Research",
                        "Wireframes",
                        "Design Systems",
                        "Accessibility",
                        "Usability Testing",
                        "Interaction Design",
                        "Developer Handoff"
                    )
                )
            )
        )
    }

    private fun buildLessons(
        completedCount: Int,
        totalCount: Int,
        titles: List<String>
    ): List<Lesson> {
        return (1..totalCount).map { index ->
            Lesson(
                id = index,
                title = titles.getOrElse(index - 1) { "Lesson $index" },
                completed = index <= completedCount
            )
        }
    }

    private companion object {
        const val NETWORK_DELAY_MS = 800L
    }
}
