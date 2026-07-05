# Kisan Alert Research Notes

## Goal

Turn the current demo into a simpler farmer-first experience for India, especially tier 2 and tier 3 usage contexts where literacy, language switching, trust, and shared-device realities matter more than feature count.

## Sources Reviewed

- Sarvam AI brand guidelines: https://www.sarvam.ai/brand
- Sarvam AI website: https://www.sarvam.ai/
- Google Next Billion Users voice playbook article: https://blog.google/innovation-and-ai/technology/next-billion-users/voice-users-playbook/
- Google Design: UX Design for Next Billion: https://design.google/library/connectivity-culture-and-credit
- IFPRI on generative AI voice advisory in India: https://www.ifpri.org/blog/generative-ai-powered-voice-technology-in-agricultural-advisory-services-lessons-from-india/
- Rockefeller Foundation on Digital Green FarmerChat: https://www.rockefellerfoundation.org/grantee-impact-stories/how-an-ai-based-app-is-bridging-the-information-gap-for-indias-farmers/
- OpenAI + Digital Green case study: https://openai.com/index/digital-green/
- CGAP on AI-powered advice for women farmers: https://www.cgap.org/blog/beyond-chat-ai-powered-advice-for-women-farmers

## Design Signals From Sarvam

- Typography:
  - `Matter` is used for UI body, labels, navigation, and product copy.
  - `Season Mix` is used for stronger display moments and premium CTAs.
- Visual language:
  - White or near-white surfaces.
  - Frosted/glass navigation elements.
  - Indigo text and controls.
  - Blue-to-orange gradients and soft radial glow fields.
  - Rounded containers, smooth transitions, premium but not flashy motion.
- Brand reasoning:
  - Sarvam explicitly ties the system to "AI for all from India."
  - The system emphasizes continuity, inclusivity, legibility, and motion as signal rather than decoration.

## Farmer UX Signals

- Voice is critical for low-literacy and first-time internet users.
- Shorter outputs are easier to trust and act on than long paragraphs.
- Natural language, pauses, and multilingual speech switching need support.
- Visual affordances matter more than text-heavy hierarchies.
- Local language support is necessary, but language alone is not enough for low-literacy users.
- Shared-device and community contexts affect trust and privacy.
- Advice quality must be specific, localized, and expert-backed.
- Trust improves when tools feel like assisted advisory, not black-box automation.

## Product Decisions Taken

### Worth Building Now

- Assisted phone-number sign-in with language-first onboarding.
- A daily home screen with only the most urgent farm actions.
- One answer card per query, with 1 to 3 next steps.
- District-aware alerts and weather guidance.
- Read-aloud support and a visible trusted-helper path for shared-device use.

### Not Worth Building Yet

- Community feed or social layer before the core advisory loop is trustworthy.
- Complex soil forms as the first interaction for most farmers.
- Fake automation theatrics such as outbound warning calls without real execution.
- Large settings surfaces, achievement dashboards, or dense analytics views.
- Dark-theme customization as a primary roadmap item.

### Keep

- Home as a daily action dashboard.
- Voice-first asking flow.
- Photo-based crop issue flow.
- Alerts as a dedicated, simple list.
- Strong language selection and read-aloud support.
- Phone-number-first sign-in for continuity with rural usage norms.

### Remove or De-prioritize

- Dense sliders as the primary first-screen interaction.
- Desktop-style dashboards with too many simultaneous widgets.
- Fake outbound call theatrics as a headline feature.
- Heavy settings complexity.
- Dark theme as a priority. The direction is white-first and daylight-readable.
- Permissions or features that are not actually implemented.

### New IA

- `Home`: Today, weather, crop stage, quick actions, trust/status.
- `Ask`: Voice, photo, typed fallback, one focused answer card.
- `Alerts`: High-priority warnings and what to do now.
- `Account`: Language, voice playback, village, family contact, support.

## Practical UI Rules Used In This Iteration

- Minimum 44px touch targets.
- One primary CTA per section.
- Fewer than five top-level tabs.
- Pictorial cue plus text for major actions.
- Simple cards with 1 to 3 action points.
- Indian language scripts visible in the interface, not hidden behind settings.
- High-contrast indigo text on light backgrounds.
- Tri-colour cues used as accent, not as a literal flag treatment everywhere.

## Production Direction Applied In The Current Web Pass

- The sign-in flow stays assisted and explicit instead of pretending OTP or account recovery is complete.
- Home favors district-aware action cards over generic dashboard density.
- The ask flow now supports simpler stateful answers rather than a single frozen demo response.
- The support path is framed as trusted help and escalation, not as fake call-center automation.
- Account keeps language, voice, and helper contact visible because shared-device use is part of the real context.
- Shared-device safety now takes priority over convenience: sign-out should clear personal identity fields, while only language, district, crop, and voice preferences may persist locally.
