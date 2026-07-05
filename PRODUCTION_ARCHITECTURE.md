# Kisan Alert Production Architecture

## Product Position

Kisan Alert should launch as a narrow farmer-assistance product, not as a generic agriculture super-app.

The first production version should do four things well:

1. Sign the farmer in with a familiar phone-first flow.
2. Show the most important daily actions on Home.
3. Let the farmer ask by voice or crop photo and receive one short answer card.
4. Deliver local alerts with clear next steps.

Everything else is secondary until trust, language quality, and answer usefulness are stable.

## What To Keep

- `Home / Ask / Alerts / Account` as the only top-level tabs.
- Voice-first interaction for low-literacy and field-use contexts.
- Photo-based crop issue flow for visual diagnosis.
- Language switching and read-aloud support.
- Phone-number-first sign-in.
- Human-support framing when confidence or trust is low.

## What To Remove Or Delay

- Fake outbound call-center behavior.
- Dense dashboard widgets and heavy data summaries.
- Complex settings and advanced toggles.
- Social/community feeds.
- Complex soil forms as the default first interaction.
- Dark theme priority.
- Any feature that implies autonomy without approval or trusted review.

## Primary User Model

- Farmer in tier 2/tier 3 India.
- Literacy may be partial or inconsistent.
- Device may be shared with family.
- Screen use may happen outdoors in daylight.
- Trust is earned through local language, short answers, and clear next steps.

## Core User Journeys

### 1. Assisted Sign-In

1. Enter mobile number.
2. Verify OTP.
3. Confirm language and district.
4. Land on Home.

Production requirement:
- OTP must be real and server-backed.
- District and language should be editable later in Account.

### 2. Daily Home Check

1. Open app.
2. See top weather, crop, and market signals.
3. See 1 to 3 actions for today.
4. Jump to Ask or Alerts.

Production requirement:
- Home content should be district-aware.
- Avoid more than three primary actions on first view.

### 3. Ask Flow

1. Tap Ask.
2. Speak, upload a crop image, or type as fallback.
3. Receive one answer card.
4. Hear it aloud if needed.
5. Escalate to trusted help if confidence is low.

Production requirement:
- The answer format must be constrained.
- The system should return: `summary`, `actions`, `risk`, `confidence`, `needs_human_review`.
- The client should render one farmer-safe answer card, not raw model output or long reasoning text.

### 4. Alerts Flow

1. Open Alerts.
2. See local alerts sorted by urgency.
3. Understand what to do first.
4. Replay alert aloud.

Production requirement:
- Alerts should be local, simple, and actionable.
- Every alert should include a concrete action window.

## Backend Service Boundaries

### Auth Service

- Phone OTP issuance and verification.
- Session tokens.
- Farmer profile lookup and update.

Current repo progress:
- Android now has an `AuthGateway` boundary and a `FarmerProfile` model instead of mixing sign-in state directly into screen logic.
- The prototype now keeps only limited local session state on-device, and backup rules explicitly exclude that state from cloud backup and device transfer.
- The remaining production step is to replace the local demo OTP gateway with a server-backed auth service and persistent farmer profiles.

### Farmer Profile Service

- Farmer id
- Name
- Phone
- Preferred language
- District
- Crop focus
- Trusted helper contact

### Advisory Service

- Voice/text query handling
- Crop-image diagnostic request handling
- Output schema normalization
- Confidence scoring
- Human-review routing when needed

Current repo progress:
- Android now has an `AdvisoryGateway` boundary so the UI layer is no longer directly coupled to Gemini prompt code.
- Voice queries are now rendered as short farmer guidance cards instead of being forced into a crop-disease diagnosis shape.
- The remaining production step is to replace the prototype gateway implementation with a real server-backed service client.

### Content + Knowledge Service

- District-level agronomy guidance
- Weather interpretation rules
- Crop calendars
- Mandi summaries
- Approved localized answer templates

### Alerts Service

- Weather alerts
- Crop-health alerts
- Market alerts
- Delivery status
- Priority ranking

### Audit + Safety Service

- Log important user-visible advice
- Log escalation and review events
- Track failed queries and fallback paths

## Recommended Data Contracts

### FarmerProfile

```json
{
  "id": "farmer_123",
  "name": "Raju Kumar",
  "phone": "+919876543210",
  "language": "hi",
  "district": "Anantapur",
  "primaryCrop": "millet",
  "trustedHelperPhone": "+919000011223"
}
```

### AdvisoryAnswer

```json
{
  "summary": "Drain standing water before adding fertilizer.",
  "actions": [
    "Open the west-side drain before sunset.",
    "Wait for the soil surface to loosen.",
    "Take a fresh leaf photo tomorrow if yellowing spreads."
  ],
  "risk": "medium",
  "confidence": "medium",
  "needsHumanReview": true
}
```

Client rendering rule:
- Show one short summary first.
- Show 1 to 3 actions next.
- Show risk/confidence in a compact secondary line.
- Show a human-review warning when `needsHumanReview` is true.

### FarmerAlert

```json
{
  "id": "alert_456",
  "type": "weather",
  "priority": "high",
  "title": "Heavy rain likely tonight",
  "summary": "Move harvested sacks off the floor and clear drainage.",
  "actionWindow": "next_8_hours",
  "district": "Anantapur",
  "language": "hi"
}
```

## AI And Safety Rules

- Do not let the client own the production LLM key.
- Move model access to a server-side advisory service.
- Use retrieval over approved agronomy content, not free-form model answers only.
- Return constrained JSON, then render farmer-safe cards.
- If confidence is low, display a human-review or helper path.
- High-risk advice should be clearly marked for human confirmation.

## UI System Rules

- White or near-white background by default.
- Indigo for primary text and controls.
- Orange and green as accent/status colors only.
- Tri-colour cues should stay subtle and structural.
- Large tap targets and short paragraphs.
- One primary CTA per section.
- Spoken output should match visible text closely.

## Delivery Phases

### Phase 1

- Real OTP backend
- Farmer profile persistence
- District-aware Home
- Voice + text Ask
- Read-aloud
- Local alerts list

### Phase 2

- Crop image pipeline
- Confidence routing
- Trusted helper escalation flow
- Better multilingual content coverage

### Phase 3

- Offline-friendly caching
- Richer district crop packs
- Extension worker tools
- Feedback loops on answer usefulness

## Current Blockers In This Repo

- No Gradle wrapper for Android verification.
- No server-side auth implementation.
- No backend advisory service.
- Android still contains direct Gemini client usage.
- Some old demo screens remain in the repo even though they are no longer part of the active IA.

## Immediate Next Engineering Steps

1. Add a real backend boundary for auth and advisory.
2. Add `.env.example` and move secrets out of the client path.
3. Replace demo OTP logic with API-backed verification.
4. Replace direct Android model calls with a service client.
5. Remove or archive old unused screens after build verification is available.
