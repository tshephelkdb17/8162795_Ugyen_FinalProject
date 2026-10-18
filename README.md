# s8162795Assignment2

Android university assignment: sign in through the Brisbane API, explore a RecyclerView collection, and open full entity details.

## Open and run

1. Extract the ZIP. Open the **s8162795Assignment2** folder (the one containing `settings.gradle.kts`) in Android Studio.
2. Use Gradle JDK **17** (Settings > Build, Execution, Deployment > Build Tools > Gradle).
3. Allow Gradle sync to download dependencies. Install Android SDK Platform 35 and any requested build tools.
4. Choose an Android emulator or physical device running API 24 or later, select `app`, and press Run.
5. Enter your numeric student ID **without s** and your case-sensitive first name. Do not include square brackets. The supplied ID is 8162795. The specification's eight-digit example conflicts with this seven-digit ID; do not invent a digit. If the server rejects your registered details, confirm them with your lecturer.

The password is entered at runtime and is not embedded in production source. This project contains no mock/demo login bypass. Live API availability and credentials are separate from local test results.

## Toolchain

- Kotlin 2.1.20, AGP 8.9.2, Gradle 8.11.1, JDK 17
- compileSdk/targetSdk 35, minSdk 24
- XML Views, ViewBinding, RecyclerView, Material Components
- Hilt 2.57.1 with KAPT, Retrofit 2.11.0, Gson, OkHttp
- StateFlow, ViewModel, coroutines

The complete Gradle wrapper is included. No `local.properties` is included because SDK paths are machine-specific. Android Studio normally creates it for you. Do not mix these pinned plugins with another version-catalog configuration.

## Architecture and dependency injection

- `domain`: pure Kotlin entities, typed errors, and repository interfaces.
- `data`: Retrofit DTOs/API, response validation, exception mapping, concrete repositories.
- `presentation`: ViewModels expose immutable StateFlow; Activities render state and perform navigation.
- `di`: Hilt provides singleton Retrofit/OkHttp/Gson and binds repository interfaces to implementations. An IO dispatcher qualifier enables deterministic tests.

Activities do not construct repositories or make HTTP requests. ViewModels depend on domain interfaces. `@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`, `@Inject`, `@Provides`, and `@Binds` are used in the actual runtime graph.

Small repository calls do not require pass-through use-case classes. Pure domain packages remain independent of Android/network libraries. Kotlin package separation is used within one Gradle module.

## API

Base URL: https://nit3213apinew.onrender.com/

- `POST br/auth`: `username` and `password`; response requires nonblank `keypass`.
- `GET dashboard/{keypass}`: response requires `entities` and nonnegative `entityTotal`.

The topic's entity schema is unspecified. Every returned field is mapped into domain fields. The dashboard excludes `description` (case-insensitive), while Details retains all fields and does not truncate the description. Nested JSON values are displayed as JSON text. Empty lists, request errors, timeout, retry, and sign-out are handled.

## Tests

From the project directory on macOS/Linux:

```bash
chmod +x gradlew
./gradlew testDebugUnitTest
./gradlew assembleDebug
./gradlew lintDebug
```

On Windows use `gradlew.bat` instead of `./gradlew`.

Tests use standard JUnit, explicit repository fakes, and MockWebServer (no JVM instrumentation agent). Tests cover invalid input, case preservation, loading/success/error, retry, duplicate taps, missing keypass, empty collections, HTTP request paths/bodies, JSON validation, network errors, timeout, cancellation, and description preservation. MockWebServer tests use local HTTP only; they do not call the real server. Test reports appear under `app/build/reports/tests/testDebugUnitTest/`.

## Manual acceptance checks

- Login with valid and invalid credentials; check the message and retry.
- Turn off networking and verify an understandable error.
- Open a dashboard item and verify every field including the entire description.
- Rotate Login/Dashboard/Details; verify no overlapping login requests or lost detail content. Password input deliberately does not persist across recreation.
- Navigate back from Details and sign out; back should not reopen a signed-out dashboard.
- Try larger font settings and check scrolling and system-bar spacing.

## Scope and limitations

This is an assignment application, not a production authentication service. There is no offline database, pagination, or persistent login. The keypass is screen state, not a stored password. Selected details are passed as a small field snapshot in Intent extras, which survive Activity recreation. If the API starts returning very large entities, use disk-backed storage and pass an entity identifier to avoid Android Binder size limits. Dynamic schema rendering is functional but can be specialised after inspecting the actual topic.

## Git and submission

This ZIP contains source, not a fabricated Git history. Start a repository and commit your real work incrementally. Do not commit `local.properties`, build folders, passwords, or signing keys. Add your own verified emulator screenshots, testing evidence, and Git repository URL before submission. Understand and explain the code and follow your university's AI-use acknowledgement rules.

## References

- https://developer.android.com/training/dependency-injection/hilt-android
- https://developer.android.com/kotlin/coroutines/test
- https://developer.android.com/topic/libraries/view-binding
- https://square.github.io/retrofit/

See `VALIDATION.md` for the checks actually performed when packaging this copy.
# 8162795_Ugyen_FinalProject

## UI refresh
See UI_UPDATE.md for the design changes, verification limits and how to apply them without losing Git history.
