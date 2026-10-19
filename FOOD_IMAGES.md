# Food images update

Adds bundled pictures for Sushi, Pizza, Tacos, Croissant, Pad Thai, Hamburger and Curry to dashboard cards and Details. Pictures are AI-generated illustrative depictions, not API-provided photographs or exact ingredient statements. Generated with the built-in OpenAI image generator on 19 October 2026.

## Design and behaviour
The shared FoodImages helper matches the dishName field ignoring case, spaces and punctuation. It never uses list positions. Unknown dish names or other topics hide the picture and clear the previous drawable, preserving the existing text-only behaviour. Images are decorative for accessibility because the dish title is already displayed. Descriptions remain excluded from the dashboard and complete in Details. Pictures are loaded from drawable-nodpi; no additional dependencies or image HTTP requests are introduced. Login and dashboard data still require the API.

## Generation prompt
Each dish was generated separately using: "Create one standalone landscape food image for an Android food catalogue card. Subject: [dish], recognisable appetising plated dish. Premium editorial food photography style, natural soft window light, ceramic plate on neutral warm stone table, subtle deep teal linen accent, realistic ingredients, centred composition with the whole dish comfortably inside the middle 75 percent for cropping. No text, no letters, no logos, no watermarks, no collage. Landscape 3:2."

## Verification
All changed XML parses and drawable references resolve against the project plus patch. Existing layout IDs are retained. Pictures were visually inspected. An Android build/device validation is still required; no new Gradle or unit-test pass is claimed. Existing business logic, Hilt and unit tests are unchanged.

After applying, run ./gradlew testDebugUnitTest assembleDebug lintDebug. Scroll through all seven dishes and open each Details screen. Confirm picture correspondence, full descriptions, Back and Sign out, and readable large text. Add this feature to the README and project report after verification.
