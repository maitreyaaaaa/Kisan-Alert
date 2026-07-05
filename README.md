<div align="center">
<img width="1200" height="475" alt="GHBanner" src="https://ai.google.dev/static/site-assets/images/share-ais-513315318.png" />
</div>

# Kisan Alert Prototype

This repository currently contains a prototype Android app plus a farmer-facing web companion.

Public web companion: https://web-companion-seven.vercel.app

Original AI Studio prototype link: https://ai.studio/apps/78bd7f39-1e90-4117-ba1d-c7894324982a

## Active Product Flow

The active farmer-facing flow in this repo is:

- `Home`: district-aware daily actions, top alert, weather signal, and trusted-review path
- `Ask`: voice-first query flow, crop-image sample flow, typed fallback, and one farmer-safe answer card
- `Alerts`: short local alerts with clear next steps
- `Account`: language, voice, profile, district, primary crop, trusted helper details, and shared-device-safe local preference persistence

The app is intentionally being reduced toward this narrower flow. Older screens such as dense crop/RSK demo surfaces still exist in the repo, but they are not part of the active IA anymore.

## Run Locally

**Prerequisites:**  [Android Studio](https://developer.android.com/studio)

1. Open Android Studio
2. Select **Open** and choose the directory containing this project
3. Allow Android Studio to fix any incompatibilities as it imports the project.
4. Create a file named `.env` in the project directory and set `GEMINI_API_KEY` in that file if you want to use the current prototype advisory flow (see `.env.example`)
5. Run the app on an emulator or physical device

## Current Verification Limitation

This checkout still does not include `gradlew` or `gradlew.bat`, and `gradle` is not installed on PATH in the current working environment.

That means source-level review and targeted tests can be updated in-repo, but full Android build, lint, and test verification are currently blocked until the Gradle toolchain is available.

## Important Architecture Note

The current Android app still uses direct Gemini access for prototype flows.

That is not the intended production architecture.

Production should move to:

- server-side OTP auth
- server-side advisory and crop-diagnosis APIs
- constrained JSON response contracts
- approved agronomy retrieval and audit logging

The current UI direction is already aligned to that target:

- one short answer card instead of raw model output
- explicit human-review framing for risky advice
- voice questions rendered as generic farmer guidance instead of being forced into a disease-diagnosis frame
- district/crop-aware home and alert content
- phone-first assisted sign-in for shared-device use

See [PRODUCTION_ARCHITECTURE.md](./PRODUCTION_ARCHITECTURE.md) for the target system direction.
