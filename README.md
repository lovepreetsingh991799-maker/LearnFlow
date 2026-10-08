# App Video and Screenshot

https://github.com/user-attachments/assets/7ced5d8d-5b94-471d-9492-5846a214c9a5

<img width="540" height="1200" alt="screenshot_1" src="https://github.com/user-attachments/assets/6b51b55c-d7fe-4226-9c1e-74a19fedf29e" />

<img width="540" height="1200" alt="screenshot_2" src="https://github.com/user-attachments/assets/1d13b035-217b-4cc2-9d8a-7376e48cbff4" />

<img width="540" height="1200" alt="screenshot_3" src="https://github.com/user-attachments/assets/8c47ef18-b19b-4fb6-b205-bbebd97ee494" />


# LearnFlow

Small Android learning dashboard built with Kotlin and Jetpack Compose for the mobile developer assignment.

## Architecture

The app uses a lightweight MVVM-style structure: Compose screens render immutable UI state, presentation classes own validation/loading/selection state, a repository coordinates data, and the data layer provides a mock API plus local cache. This keeps the assignment small while preserving production-friendly separation between UI, business logic, and persistence.

## Offline Support

Courses are fetched from `MockCourseApi` and then saved to `SharedPreferences` as JSON through `SharedPreferencesCourseCache`. On a later load, if the API fails, the repository returns the cached courses so the dashboard and lesson details remain available offline. Lesson completion updates are also written back to the cache.

## Security

In production, authentication tokens should not be stored in plain preferences. I would store short-lived access tokens in memory and refresh tokens in Android Keystore-backed encrypted storage, with certificate pinning and token rotation where appropriate.

## Scale

For 1 million users and hundreds of courses, I would add real paginated APIs, Room or SQLDelight for structured offline data, background sync with conflict handling, observability/crash reporting, stronger auth/session management, and UI paging/search/filtering for large course catalogs.

## Second Platform

On iOS/macOS, I would mirror the same boundaries with SwiftUI views, Observable view models, a repository layer, URLSession-backed API services, and SwiftData or Core Data for local course and lesson caching.
