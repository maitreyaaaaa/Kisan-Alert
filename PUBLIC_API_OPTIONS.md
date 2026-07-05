# Public API Options For Kisan Alert

This note is grounded in the public API catalog at:

- [public-apis/public-apis](https://github.com/public-apis/public-apis)

## Recommended For This Prototype

### 1. Open-Meteo

Use for:
- daily forecast
- rain probability
- temperature
- wind

Why it fits:
- free
- no API key
- useful for district-level or lat/lng-linked farm actions

Kisan Alert fit:
- power `Home` weather signal
- power `Alerts` such as rain-risk warnings
- power `Ask` answers like "Should I spray today?"

### 2. Nominatim or GeoDB Cities

Use for:
- district or place lookup
- geocoding
- reverse geocoding

Why it fits:
- helps convert farmer-selected place context into coordinates
- supports weather API integration and regional alert logic

Kisan Alert fit:
- map district name to coordinates
- normalize place names before weather fetches

### 3. LibreTranslate

Use for:
- translating dynamic advisory output
- fallback translation for new server-side answers

Why it fits:
- public listing
- no-auth option exists in the catalog

Important product rule:
- do **not** use translation APIs as the primary source for fixed UI copy
- fixed UI strings should stay curated in-repo
- translation APIs are more appropriate for dynamic advisory payloads generated later

## Not Recommended As The Primary Demo Path

### Client-side live translation for all UI text

Why not:
- unstable quality
- slower UI
- harder to trust in front of judges
- introduces failure modes for a demoable static prototype

For the current web companion:
- keep core UI strings curated in `web-companion/index.html`
- use APIs only for dynamic data and future backend responses

## Suggested Integration Order

1. Open-Meteo for live weather context
2. Geocoding/place normalization for district mapping
3. Server-side translation fallback for dynamic advisory responses

## Current Recommendation

For hackathon submission and demo:
- ship curated multilingual UI locally
- use the public Vercel deployment as the stable prototype
- treat live public APIs as the next production-direction step, not a requirement for the current static demo
