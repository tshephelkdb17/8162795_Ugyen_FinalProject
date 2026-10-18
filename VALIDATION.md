# Validation record

Validated on 12 October 2026 with JDK 17, Android SDK 35 and the included Gradle wrapper.

- `./gradlew testDebugUnitTest`: **17 tests passed**, 0 failures, 0 errors, 0 skipped.
- `./gradlew assembleDebug`: **passed**; debug APK successfully generated.
- `./gradlew lintDebug`: **passed**, 0 errors and 7 warnings. Warnings remain informational; this is not a warning-free lint claim.
- XML parsing and app string/color reference checks: passed.
- Gradle wrapper JAR integrity and final ZIP integrity: checked.

The tests use JUnit, explicit test fakes and MockWebServer. They do not need the university API. The live login check timed out in this environment, so the supplied credentials and current live API response have **not** been confirmed. No emulator/device UI session was performed; use the manual checklist in README.md to test the screens.

The ZIP includes source and wrapper files. Downloaded toolchains, machine-specific settings, build output, test reports and caches are excluded. Run the commands above to regenerate the APK and reports on your computer.
