# Kisan Alert Pitch Deck Facts

Use only the claims below in the pitch deck unless a new source is verified.

## Hackathon Submission Requirement

Source: Gmail message from `Build with AI: Code for Communities <admin@no-reply.hack2skill.com>`
Subject: `3 days to go. Stop planning. Start finishing.Subject`
Date: 2026-07-05

Verified submission requirements from the email:

- Pitch deck must be `10 to 12 slides`
- The deck should cover:
  - `Problem`
  - `Solution`
  - `How the AI works`
  - `Who it serves`
  - `Why it's deployable`
- Pitch deck should be `converted into a PDF`
- Other required submission fields:
  - public GitHub source code
  - working prototype link
  - brief description
  - technologies used

## Repo-Backed Product Facts

Source files:
- `README.md`
- `PRODUCTION_ARCHITECTURE.md`

Safe product claims:

- Kisan Alert currently exists as an Android prototype plus a farmer-facing web companion.
- The active product flow is:
  - `Home`
  - `Ask`
  - `Alerts`
  - `Account`
- The intended production scope is deliberately narrow:
  - phone-first sign-in
  - district-aware Home
  - voice/photo/text Ask
  - local alerts
- The product is designed for:
  - tier 2 / tier 3 India
  - low or mixed literacy contexts
  - shared-device realities
  - short, local-language guidance
- The target production architecture separates:
  - auth
  - farmer profile
  - advisory
  - content/knowledge
  - alerts
  - audit/safety

Important honesty constraints:

- This checkout does **not** currently include a Gradle wrapper or working Gradle on PATH, so full Android verification is blocked in the current environment.
- The production backend is **not** implemented yet.
- The current Android prototype still includes direct Gemini usage that should move server-side in production.

## External Research Facts

### Voice and low-literacy access

Source:
- Google Blog
- `How to help people navigate the internet, voice-first`
- June 2, 2021
- URL: https://blog.google/innovation-and-ai/technology/next-billion-users/voice-users-playbook/

Verified claims:

- Google states that for many new internet users, voice is not just helpful but critical.
- Google states that voice helps people with low literacy levels become more self-sufficient.
- Google states that voice simplifies interaction where typing in scripted languages is difficult.

Relevant excerpt location:
- lines 304 to 310 in the current page capture

### Women, literacy, and local-language chatbot access

Source:
- CGAP
- `Beyond the Chat: AI-Powered Advice for Women Farmers`
- URL: https://www.cgap.org/blog/beyond-chat-ai-powered-advice-for-women-farmers

Verified claims:

- CGAP says chatbots are particularly relevant for women, who often have lower traditional or digital literacy levels and may be less likely to own smartphones and use mobile internet.
- CGAP says Digital Green's Farmer.Chat supports voice, text, or pictures in local languages.
- CGAP says these interaction modes help overcome digital-literacy barriers.

Relevant excerpt location:
- lines 121 to 132

### Extension capacity, RAG, and human-review pattern

Source:
- OpenAI customer story on Digital Green
- URL: https://openai.com/index/digital-green/

Verified claims:

- OpenAI states India has over 400,000 extension agents but an agent-to-farmer ratio of only `1:650`.
- OpenAI states Digital Green created nearly `8,000` training videos in more than `50` languages.
- OpenAI states those videos increased farmer income by an average of `24 percent`.
- OpenAI states Farmer.Chat uses `RAG` over agricultural information and government-partner data.
- OpenAI states India's Ministry of Agriculture validates documents in the knowledge base.
- OpenAI states the system was initially deployed as an assistant to extension agents to add a stage of human review.

Relevant excerpt location:
- lines 34 to 41

## Deck Usage Guidance

You may safely use these deck statements:

- `Voice-first access matters in low-literacy contexts.`
- `Shared devices and multilingual use shape the product design.`
- `Kisan Alert focuses on one short answer and a few next steps, rather than a long chatbot transcript.`
- `Deployability comes from narrow workflow scope, district-aware content, and clear backend service boundaries.`

Do not use these claims unless newly verified:

- Number of farmers reached by Kisan Alert
- Accuracy improvements
- Yield increase
- Reduction in crop loss
- Live pilot deployment status
- Government partnership status for this project specifically
- Any claim that the current prototype is already production-ready
