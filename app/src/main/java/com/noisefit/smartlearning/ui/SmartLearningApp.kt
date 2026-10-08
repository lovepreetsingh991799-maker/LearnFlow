package com.noisefit.smartlearning.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.noisefit.smartlearning.data.CourseRepository
import com.noisefit.smartlearning.data.MockCourseApi
import com.noisefit.smartlearning.data.SessionStore
import com.noisefit.smartlearning.data.SharedPreferencesCourseCache
import com.noisefit.smartlearning.presentation.CoursesViewModel
import com.noisefit.smartlearning.presentation.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun SmartLearningApp() {
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val sessionStore = remember { SessionStore(context) }
    val repository = remember {
        CourseRepository(
            api = MockCourseApi(context),
            localDataSource = SharedPreferencesCourseCache(context)
        )
    }
    val loginViewModel = remember { LoginViewModel() }
    val coursesViewModel = remember { CoursesViewModel(repository) }
    var route by rememberSaveable {
        mutableStateOf(
            if (sessionStore.isLoggedIn()) AppRoute.Dashboard else AppRoute.Login
        )
    }

    BackHandler(enabled = route == AppRoute.Details) {
        route = AppRoute.Dashboard
    }

    LaunchedEffect(route) {
        if (route == AppRoute.Dashboard && coursesViewModel.uiState.courses.isEmpty()) {
            coursesViewModel.loadCourses()
        }
    }

    when (route) {
        AppRoute.Login -> {
            LoginScreen(
                state = loginViewModel.uiState,
                onEmailChanged = loginViewModel::onEmailChanged,
                onPasswordChanged = loginViewModel::onPasswordChanged,
                onLogin = {
                    scope.launch {
                        if (loginViewModel.login()) {
                            sessionStore.markLoggedIn()
                            route = AppRoute.Dashboard
                        }
                    }
                }
            )
        }

        AppRoute.Dashboard -> {
            DashboardScreen(
                state = coursesViewModel.uiState,
                onRefresh = {
                    scope.launch {
                        coursesViewModel.loadCourses(forceRefresh = true)
                    }
                },
                onSimulateFailure = {
                    scope.launch {
                        coursesViewModel.loadCourses(
                            forceRefresh = true,
                            simulateFailure = true
                        )
                    }
                },
                onCourseSelected = { courseId ->
                    coursesViewModel.selectCourse(courseId)
                    route = AppRoute.Details
                }
            )
        }

        AppRoute.Details -> {
            val course = coursesViewModel.uiState.selectedCourse
            if (course == null) {
                LaunchedEffect(Unit) {
                    route = AppRoute.Dashboard
                }
            } else {
                CourseDetailsScreen(
                    course = course,
                    onBack = { route = AppRoute.Dashboard },
                    onLessonComplete = { lessonId ->
                        coursesViewModel.completeLesson(course.id, lessonId)
                    }
                )
            }
        }
    }
}

private enum class AppRoute {
    Login,
    Dashboard,
    Details
}
