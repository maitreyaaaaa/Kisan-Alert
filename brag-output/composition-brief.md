# Hyperframes Composition Brief: Kisan Alert

## Objective
Create a slower, more readable product walkthrough video for Kisan Alert.

## Output
- Composition directory: `brag-output/composition/`
- Rendered video: `brag-output/brag.mp4`
- Format: landscape - 1920x1080
- Duration: 88 seconds

## Source Material
- Project root: `D:\Google Build Crop checking application\Kisan-Alert`
- Primary files read: `README.md`, `web-companion/index.html`, `web-companion/RESEARCH_NOTES.md`
- Product name: Kisan Alert
- Tagline / strongest claim: `One clear farming action at a time.`
- Key UI or visual moment to recreate: the white-first, indigo-led companion flow across Home, Ask, Alerts, and Account
- Copy that must appear verbatim:
  - `Built for low-text, local-language farming use`
  - `One clear farming action at a time.`
  - `Assisted sign-in`
  - `Ask Kisan Alert`

## Creative Direction
- Tone preset: polished
- Creative direction: calm, trustworthy hackathon walkthrough for a farmer-facing product
- Interpretation: fewer simultaneous elements, longer holds, and full readability before transitions
- Angle: the app should feel designed for real rural use, not as a generic AI product
- Hook: start with the promise and let it hold
- Outro / punchline: end on multilingual support and a minimal Kisan Alert logo lockup
- Avoid:
  - Demo wording
  - Rapid montage pacing
  - Unrelated redesign
  - Generic SaaS claims

## Visual Identity
- Background: `#fafafa`
- Text: `#1e2033`
- Accent: `#f9730c`
- Secondary accent: `#d5e2ff`
- Success accent: `#257a4b`
- Display font: `Season Mix`
- Body font: `Matter`
- Visual references from the project: frosted navigation, tricolour cue line, orange highlights, soft blue wash cards, rounded white surfaces

## Storyboard
Use the storyboard in `brag-output/brag-plan.md` as the creative contract.

Scene summary:
1. Product promise - 8s
2. Who this is for - 8s
3. Assisted sign-in - 10s
4. Home daily action - 12s
5. Ask question - 10s
6. Ask answer - 10s
7. Alerts - 8s
8. Account - 8s
9. Multilingual support - 8s
10. Logo close - 6s

## Audio
- Audio role: warm bed
- Audio arc: quiet and supportive from start to finish
- Music: `assets/music/happy-beats-business-moves-vol-11-by-ende-dot-app.mp3`
- Music treatment: low volume, no rhythmic editing, softer end fade
- Music cue guidance: do not pace scenes to beats
- Audio-reactive treatment: none
- Audio-coupled moments:
  - scene changes only
- SFX selection guidance: omit for this cut
- Exact SFX choice: none
- Audio files: use local assets only

## Hyperframes Instructions
Use deterministic GSAP timing with longer scene durations and scene-by-scene fades.

Requirements:
- Show real product copy and UI structure from the source project
- Remove all `demo` language from on-screen copy
- Keep all text readable without rushing
- Stay close to the app's actual palette and typography
- Include the planned music bed at a restrained level
- Run Hyperframes lint, validate, inspect, and render locally
