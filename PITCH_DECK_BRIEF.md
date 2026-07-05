# Kisan Alert Pitch Deck Brief

## Source Of Truth

This deck brief is constrained by the latest Code for Communities submission email:

- Pitch deck must be `10 to 12 slides`
- Must cover:
  - `problem`
  - `solution`
  - `how the AI works`
  - `who it serves`
  - `why it's deployable`
- Final deck should be converted to `PDF`

Submission package also requires:

- Public GitHub repo
- Working prototype link
- Brief description
- Technologies used

## Evidence We Can Claim

### Product State In Repo

From `README.md` and current web/app work:

- Prototype includes an Android app plus a farmer-facing web companion
- Active IA is:
  - `Home`
  - `Ask`
  - `Alerts`
  - `Account`
- Current product direction is intentionally narrowed toward:
  - district-aware daily actions
  - voice-first ask flow
  - short, safe answer cards
  - local alerts
  - language and shared-device support

### Production/Deployability Claims We Can Safely Make

From `PRODUCTION_ARCHITECTURE.md`:

- Production path is not a generic agriculture super-app
- It is scoped to four deployable workflows:
  - phone-first sign-in
  - daily Home actions
  - Ask via voice/photo/text
  - local alerts
- Recommended service boundaries already identified:
  - Auth service
  - Farmer profile service
  - Advisory service
  - Content/knowledge service
  - Alerts service
  - Audit/safety service
- Constrained advisory output contract already defined:
  - `summary`
  - `actions`
  - `risk`
  - `confidence`
  - `needsHumanReview`

### Research-Backed Problem Statements We Can Use

From referenced external sources already reviewed for this repo:

- Google Next Billion Users:
  - voice is critical for many new internet users with low literacy
  - simpler output and multilingual voice support matter
- OpenAI x Digital Green:
  - agricultural extension is capacity-constrained
  - India has a large but still stretched extension network
  - validated knowledge base + human review is an effective safety pattern
- CGAP:
  - rural women often have lower literacy and less smartphone/mobile internet access than men
  - voice, pictures, and local-language support can reduce digital-literacy barriers

## Slide Structure

## Slide 1 - Title

**Kisan Alert**
Voice-first crop guidance for farmers using shared phones in local languages.

Purpose:
- State the product in one line
- Position it as a narrow, practical farmer-assistance system

## Slide 2 - The Problem

Core message:
- Farmers often need timely, local, understandable guidance
- Existing advisory access is fragmented, text-heavy, or dependent on human reach

Support points:
- Extension systems are capacity constrained
- Low literacy, multilingual usage, and shared-device realities make generic chat UX fail
- Farmers need short action steps, not long explanations

## Slide 3 - Why Existing Interfaces Break

Core message:
- The bottleneck is not only information availability; it is accessibility and trust

Support points:
- Voice is critical for many low-literacy new internet users
- Multilingual speaking patterns and pauses must be supported
- Women and other last-mile users can face additional device and literacy barriers

## Slide 4 - Our Solution

Core message:
- Kisan Alert reduces the experience to four farmer-safe surfaces

Show:
- `Home` for daily actions
- `Ask` for voice/photo/text input
- `Alerts` for local warnings
- `Account` for language, district, crop, helper, and voice settings

## Slide 5 - Who It Serves

Primary user:
- Farmer in tier 2/tier 3 India

Context:
- Partial literacy possible
- Outdoor/daylight use
- Shared family device
- Strong need for local language and trusted escalation

## Slide 6 - How The AI Works

Keep this simple and concrete:

1. Farmer asks by voice, image, or text
2. Advisory service interprets the query
3. System retrieves approved agronomy and local context
4. Model returns constrained structured output
5. App shows one short answer card with 1 to 3 actions
6. Low-confidence cases route to human review / trusted helper framing

## Slide 7 - Safety And Trust Design

Core message:
- This is not an unconstrained chatbot

Support points:
- Constrained answer schema
- Human-review path when needed
- Explicit trust framing for risky guidance
- No production frontend LLM key exposure
- Audit and safety service planned in target architecture

## Slide 8 - Why This Can Deploy

Core message:
- The product is scoped for realistic pilot deployment

Support points:
- Narrow workflow, not a super-app
- District-aware Home and alerts are suitable for pilot geography
- Phone-first onboarding fits current usage norms
- Architecture already separated into deployable backend services
- Shared-device-safe persistence is already part of product design

## Slide 9 - Current Prototype Evidence

What is real today:
- Android prototype exists
- Web companion exists
- Demoable flows already built:
  - assisted sign-in
  - Home daily action flow
  - Ask with short answer card
  - Alerts
  - Account with language/voice/helper context

Important honesty note:
- Full Android verification is currently limited in this checkout because Gradle wrapper/tooling is missing
- Production backend is not yet implemented

## Slide 10 - Technology Stack

Current prototype stack:
- Kotlin
- Jetpack Compose
- Android
- HTML/CSS/JS web companion
- Google Gemini prototype integration

Target production stack:
- Server-side auth and advisory APIs
- Structured JSON contracts
- Retrieval over approved agronomy content
- Audit logging and review path

## Slide 11 - Pilot Rollout Vision

Short-term pilot flow:
- One district
- One or two primary crops
- Voice-first onboarding
- Daily actions + Ask + alerts
- Trusted escalation path

Success signals:
- farmer comprehension
- repeat usage
- action completion
- reduction in ambiguous advisory output

## Slide 12 - Closing

Closing line:
- Kisan Alert turns crop guidance into short, local-language, deployable farmer support.

Final ask:
- Pilot this in a real district workflow where language, trust, and timely action matter more than feature count.

## Presenter Notes Guidance

- Keep claims narrow and factual
- Do not claim production readiness
- Do not claim live backend deployment unless verified
- Do not claim agronomic accuracy metrics that we have not measured
- Emphasize:
  - usability
  - trust
  - scope discipline
  - deployability path
