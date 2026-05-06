# Chapter 4: Implementation and Testing

This chapter explains how **GROCO – Smart Grocery Supply Management System** was implemented as a single Android application and how its behavior was validated through layered testing. The implementation combines AI/ML components (object detection, image-based recognition, and language-vision extraction) with software modules for persistence, state management, network aggregation, and background reminders. The chapter emphasizes module-level design and integration decisions, not function-level walkthroughs.

[[VISUAL_SLOT: FIG_4_1_IMPL_VERIFICATION_MAP]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_4_1_impl_verification_map.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/testing_pyramid.png]]
[[VISUAL_CAPTION: Figure 4.1 End-to-end implementation and verification map for GROCO – Smart Grocery Supply Management System]]

## 4.1 Development Workflow and Integration Stages

Development followed staged integration instead of big-bang feature merges. Each stage had a measurable output and a verification gate. This approach reduced risk because unstable parts could be isolated before cross-module dependencies increased.

The workflow progressed in six stages:

1. **Foundation stage**: app shell, navigation, dependency injection, and local persistence entities.
2. **Perception stage**: camera preview, object detection, and capture-state orchestration.
3. **Recognition stage**: embedding-based similarity lookup plus AI-assisted title and expiry extraction.
4. **Comparison stage**: multi-source price adapter integration and clustering/ranking logic.
5. **Context stage**: location settings and request shaping for better compare relevance.
6. **Automation stage**: periodic expiry reminders and flow-level regression validation.

In stage 1, the team established local-first state ownership. ObjectBox entities were created for grocery items, comparison history, and location configuration. This was critical because all later modules required reliable read/write behavior and deterministic app startup.

Stage 2 integrated the scan route using CameraX with live image analysis. ML Kit object detection was used in stream mode so that product regions could be tracked continuously while users aligned the camera. The UI showed live overlays, which created immediate visual feedback and made capture behavior understandable.

Stage 3 connected perception output to AI enrichment. The scanning process was intentionally split into two captures: front-pack image for item identity and label image for expiry extraction. A local embedding vector was generated for similarity search in ObjectBox; if no reliable historical match was found, Gemini-based inference supplied semantic title and expiry details. This stage established the practical hybrid pattern used across the system: deterministic local logic first, cloud inference when needed.

Stage 4 implemented the comparison engine as a repository-driven fan-out across source adapters. Adapter output was normalized, deduplicated, filtered, and clustered. The output structure included both results and site-level errors so partial failures would not collapse the whole response.

Stage 5 integrated location settings as a first-class input to compare requests. City, area, pincode, coordinates, and max-results controls were persisted and then consumed by the compare pipeline, improving relevance and constraining response size.

Stage 6 added WorkManager-based reminder scheduling and test hardening. At this stage, the system moved from passive inventory storage to proactive household assistance by notifying users about near-expiry or expired items.

[Table 4.1 Development stages, deliverables, and validation gates]

| Stage | Primary modules | AI/ML role | Validation gate |
|---|---|---|---|
| 1 | Compose shell, Hilt, ObjectBox entities | None | Launch and persistence checks |
| 2 | CameraX + ML Kit + overlay | Real-time object detection | Scan navigation smoke |
| 3 | Embedding lookup + Gemini extraction | Recognition and expiry inference | Stored item quality checks |
| 4 | Adapters + repository + matcher | Relevance scoring and clustering | Compare results rendering |
| 5 | Location settings repository/UI | Query conditioning | Settings save/load + compare context |
| 6 | WorkManager reminder worker + E2E flows | Time-based alerting | Periodic job + Maestro flows |

The final result of this workflow is a cohesive mobile architecture where inventory management remains usable even when network variability affects compare performance.

## 4.2 Implementation Details

### 4.2.1 Scanning and Detection Flow

The scanning module is centered around a dedicated screen that manages camera lifecycle and scan-state progression. CameraX provides two synchronized surfaces:

- `Preview` for user feedback.
- `ImageAnalysis` for frame processing.

Frames are passed to an object detector (ML Kit, stream mode with classification). Detected regions are drawn on a custom overlay that transforms image coordinates to view coordinates. This transformation layer is essential because preview dimensions and analysis buffers are not always aligned, especially under rotation.

Capture is event-driven rather than continuous. Instead of persisting every frame, the module stores only the next valid analyzed frame when the user taps capture. This reduces noise and storage overhead. For each detected region, the system:

- converts the frame to bitmap,
- clamps bounding coordinates to valid bounds,
- crops the object region,
- records labels/confidence/track ID,
- tags capture step.

The scan flow is step-based:

- **Step A**: capture front view (item identity intent).
- **Step B**: capture label view (expiry intent).

Alternating between these two steps gives better practical outcomes than single-image extraction, because packaging front faces and expiry labels are often on different surfaces.

[[VISUAL_SLOT: FIG_4_2_SCAN_OVERLAY_CROP_FLOW]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_4_2_scan_overlay_crop_flow.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/legacy_reportfinal_img-001.jpg]]
[[VISUAL_CAPTION: Figure 4.2 Live scan, overlay, capture, and crop data path]]

### 4.2.2 Feature Embedding and Item Recognition Logic

Each cropped object bitmap is converted to a compact embedding (fixed-size grayscale feature vector). This vector is indexed in ObjectBox using HNSW, enabling nearest-neighbor retrieval against previously stored items.

Recognition follows a two-level strategy:

1. **Local similarity first**: if a strong nearest-neighbor match is found, reuse stored title.
2. **AI fallback**: if similarity is weak or absent, request title from Gemini inference.

This approach keeps the fast path deterministic while preserving semantic flexibility for unseen items.

Software benefits:

- repeated scans of the same product become consistent,
- cloud calls are reduced for known products,
- recognition behavior remains modular (embedding logic and AI prompting can evolve independently).

AI/ML benefits:

- local embedding search captures visual continuity,
- language-vision inference resolves human-readable naming ambiguity.

This module-level separation is future-proof. A stronger on-device encoder can replace the current vectorizer without major changes to compare, notification, or home-screen modules.

[Table 4.2 Recognition decision logic]

| Condition | Decision | System behavior |
|---|---|---|
| Reliable nearest neighbor | Use stored title | Faster and consistent naming |
| No reliable neighbor | Call Gemini title extraction | Better handling of unseen products |
| Weak detector labels | Prefer semantic title fallback | Improved robustness under noisy classification |

### 4.2.3 Expiry Extraction Pipeline

Expiry extraction is tied to the second capture step. The label image is sent to Gemini with strict output expectation: return expiry date in `DD/MM/YYYY` format, or `null` if not inferable.

After inference:

- output is parsed defensively,
- valid dates are normalized to local midnight,
- invalid outputs fall back to a safe default timestamp.

This software guardrail is important because model outputs can vary with packaging quality, glare, and mixed date formats.

The stored grocery entity contains:

- title,
- embedding,
- image path,
- expiry timestamp,
- quantity/unit defaults,
- low-stock threshold,
- reminder window.

As a result, one complete two-step scan creates an item that is immediately usable by home inventory display, compare action, and reminder scheduling.

[[VISUAL_SLOT: FIG_4_3_IDENTITY_EXPIRY_PIPELINE]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_4_3_identity_expiry_pipeline.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/legacy_reportfinal_img-005.jpg]]
[[VISUAL_CAPTION: Figure 4.3 Two-step identity + expiry extraction pipeline]]

### 4.2.4 Comparison Engine Integration

The comparison engine is implemented through a repository that accepts a normalized compare request with query text, location context, and per-site limits.

Key software behaviors:

- request validity checks,
- query-location fingerprinting for cache reuse,
- cache TTL for repeated comparisons,
- parallel adapter fan-out,
- merged result structuring with transparent site error reporting.

The adapter layer is mixed generic + site-specific:

- generic parsing supports LD+JSON, embedded JSON, and HTML-card extraction,
- specific adapters handle irregular payload shapes,
- deduplication removes repeated offers,
- normalized offer objects are returned to domain matching.

Domain matching applies relevance and clustering logic:

- text normalization removes punctuation and low-value words,
- quantity extraction normalizes units (`ml`, `l`, `g`, `kg`, `pcs` variants),
- compatibility scoring penalizes mismatched quantity contexts,
- fuzzy similarity produces cluster membership and confidence,
- final offers are sorted by price for direct user utility.

A practical reliability feature is partial-failure tolerance. If some sites fail due to anti-bot responses or parser drift, successful sites still contribute valid results, while failed sites are shown through structured error fields.

In debug mode, the view-model can return deterministic demo data when live fetches fail. This keeps UI and E2E flows testable even under unstable internet conditions.

[[VISUAL_SLOT: FIG_4_4_PARALLEL_ADAPTER_FANOUT]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_4_4_parallel_adapter_fanout.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/compare_pipeline.png]]
[[VISUAL_CAPTION: Figure 4.4 Parallel adapter fan-out and match/rank aggregation]]

### 4.2.5 Notification Pipeline

The notification module converts stored expiry metadata into time-based reminders.

At app startup:

- a notification channel is ensured,
- unique periodic work is scheduled through WorkManager.

At worker execution:

- inventory items are loaded from ObjectBox,
- remaining days are computed,
- reminder windows are checked,
- near-expiry and expired alerts are emitted when permission allows.

The worker explicitly checks notification permission before posting alerts. This prevents runtime policy violations on modern Android versions.

This pipeline demonstrates a complete loop: AI-derived expiry data enters storage during scanning and later drives autonomous reminder behavior.

[Table 4.3 Reminder rules and expected behavior]

| Rule input | Condition | Action |
|---|---|---|
| `daysRemaining > remindBeforeDays` | Not in alert window | No notification |
| `0 <= daysRemaining <= remindBeforeDays` | Near expiry | Send “expires in N day(s)” alert |
| `daysRemaining < 0` | Already expired | Send “expired N day(s) ago” alert |
| Permission denied | Policy restriction | Skip notification safely |

## 4.3 Experimental Setup (software/hardware/runtime assumptions)

The implementation was evaluated in an Android-native environment with no mandatory backend service.

### Software assumptions

- Kotlin + Gradle Kotlin DSL project.
- Jetpack Compose UI and navigation.
- Hilt for dependency injection.
- CameraX and ML Kit for scanning/detection.
- Firebase AI (Gemini) for title and expiry inference.
- ObjectBox for local persistence and vector lookup.
- Ktor + OkHttp + Jsoup for price-source retrieval.
- WorkManager for periodic reminders.
- JUnit and Maestro for test automation.

### Hardware/runtime assumptions

- Android device or emulator (camera-enabled for live scan).
- Network access for compare calls and cloud inference.
- Notification-capable Android runtime.
- `compileSdk 36`, `targetSdk 36`, `minSdk 24`.
- JVM target 11, with JDK 17 preferred for stable project builds.

### Automation assumptions

- `adb` available and connected to emulator/device.
- Maestro CLI available for black-box mobile flow execution.
- Optional live compare tests enabled through environment toggle.

In addition to tool availability, runtime posture assumes predictable Android lifecycle behavior during background work. Periodic reminder execution depends on OS scheduling policies, battery optimizations, and permission persistence across app restarts. Therefore, validation was designed to include both immediate behavior checks (UI and repository outcomes) and delayed behavior checks (worker-triggered reminder outcomes). This dual perspective is important for mobile systems where foreground and background execution guarantees differ significantly.

[[VISUAL_SLOT: FIG_4_5_ENV_STACK]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_4_5_environment_stack.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/timeline_gantt.png]]
[[VISUAL_CAPTION: Figure 4.5 Environment stack and runtime boundaries]]

[Table 4.4 Experimental setup summary]

| Dimension | Assumption | Purpose |
|---|---|---|
| Platform | Android app | Single-device deployment model |
| Persistence | Local ObjectBox | Offline-capable inventory core |
| AI/ML | ML Kit + Gemini | Detection + semantic extraction |
| Compare runtime | Ktor adapter fan-out | Multi-source pricing |
| Background execution | WorkManager | Periodic reminder automation |
| Validation tooling | JUnit + Maestro + adb | Layered quality checks |

## 4.4 Test Strategy (unit, integration, UI, end-to-end)

Testing follows a layered model so deterministic logic is validated early and cross-module behavior is validated later.

### Unit tests

Unit tests focus on domain logic where expected outputs are explicit:

- quantity extraction and unit normalization,
- quantity compatibility scoring,
- offer clustering and ranking,
- empty-input behavior.

This layer protects the compare engine from silent ranking regressions.

### Integration tests

Integration tests validate repository orchestration and adapter aggregation under realistic network variability. Live catalog tests (environment-gated) measure whether relevant offers are produced across item categories. This layer checks practical resilience under partial site failures.

### UI tests

UI-level checks focus on behavior visible to users:

- inventory listing,
- quantity controls,
- compare bottom sheet,
- location settings form,
- route accessibility.

### End-to-end tests

Maestro flows perform device-level black-box validation for:

- seeded inventory lifecycle,
- scan-route navigation smoke,
- quantity changes,
- location save/reset behavior,
- compare execution and result visibility.

This combination provides confidence in both internals (logic) and user journeys (interaction).

For reproducibility, verification is organized around explicit execution gates. Typical project-level checks include unit tests, linting, debug assembly, instrumentation where available, and full Maestro suite execution. The strategy separates deterministic failures (logic/test assertions) from environment-dependent failures (network variability, external site behavior, emulator availability). This distinction improves triage speed because a failing category immediately indicates whether remediation should happen in application code, infrastructure setup, or external integration assumptions.

[[VISUAL_SLOT: FIG_4_6_TEST_PYRAMID]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_4_6_test_pyramid.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/testing_pyramid.png]]
[[VISUAL_CAPTION: Figure 4.6 Test pyramid for GROCO – Smart Grocery Supply Management System]]

[Table 4.5 Test layers and defect focus]

| Layer | Focus | Typical defect category |
|---|---|---|
| Unit | Domain logic | Parsing/scoring errors |
| Integration | Module interaction | Aggregation/caching failures |
| UI | Compose state and navigation | Binding/navigation regressions |
| E2E | Full user journey | Cross-module runtime failures |

## 4.5 Representative Test Cases (input vs expected output tables)

Representative tests are listed below with input and expected output patterns.

[Table 4.6 Quantity and normalization cases]

| Test ID | Module | Input | Expected output |
|---|---|---|---|
| QTY-01 | Quantity extraction | `"Amul Milk 500 ml"` | Value `500.0`, unit `ml` |
| QTY-02 | Quantity extraction | `"Amul Ice Cream 1 pack (1 L)"` | Value `1.0`, unit `l` preferred over pack |
| QTY-03 | Compatibility scoring | `"500 ml"` vs `"0.5 l"` | High compatibility |
| QTY-04 | Compatibility scoring | `"1 kg"` vs `"1 l"` | Low compatibility due to unit mismatch |
| QTY-05 | Text normalization | `"Best Fresh Milk Offer"` | Reduced to high-signal tokens |

[Table 4.7 Offer matcher and ranking cases]

| Test ID | Module | Input | Expected output |
|---|---|---|---|
| MAT-01 | Clustering | Two 500 ml milk offers + one 1 L offer | 500 ml offers grouped in top cluster |
| MAT-02 | Ranking | Cluster prices `35`, `34`, `36` | Sorted ascending, `34` first |
| MAT-03 | Empty behavior | Valid query with no offers | Empty matched list |
| MAT-04 | Relevance filter | Query tokens absent in title | Offer filtered unless fallback path triggered |

[Table 4.8 Scan and persistence cases]

| Test ID | Module | Input | Expected output |
|---|---|---|---|
| SCN-01 | Detection capture | Front frame with valid object | Cropped object image stored in scan state |
| SCN-02 | Embedding reuse | Similar known item embedding | Existing title reused |
| SCN-03 | AI fallback | No close neighbor | Gemini title extraction used |
| SCN-04 | Expiry parse success | Model returns `02/06/2026` | Stored expiry timestamp normalized to date midnight |
| SCN-05 | Expiry parse failure | Invalid date or `null` | Defensive default timestamp applied |

[Table 4.9 Compare engine cases]

| Test ID | Module | Input | Expected output |
|---|---|---|---|
| CMP-01 | Request validation | Blank query | Structured invalid request error |
| CMP-02 | Cache | Repeated query/location within TTL | Response returned from cache |
| CMP-03 | Partial failure | Some adapters fail | Remaining site results returned + error map |
| CMP-04 | Location shaping | Saved city/area/pincode/max limit | Compare request uses sanitized location context |
| CMP-05 | Debug continuity | Live fetch failure in debug build | Deterministic demo results available |

[Table 4.10 Reminder and schedule cases]

| Test ID | Module | Input | Expected output |
|---|---|---|---|
| NTF-01 | Reminder worker | Item expires in 1 day, reminder window=2 | Notification emitted |
| NTF-02 | Reminder worker | Item expires in 5 days, reminder window=2 | No notification |
| NTF-03 | Reminder worker | Item expired 2 days earlier | Expired-item alert emitted |
| NTF-04 | Permission gate | Notification permission denied | Worker skips posting |
| NTF-05 | Scheduler | App startup event | Unique periodic work registered |

Representative E2E checkpoints:

- seeded inventory appears before cleanup,
- quantity increments/decrements are reflected in UI text,
- location save acknowledges success state,
- compare flow shows `Results for ...`,
- scan route opens and returns cleanly.

[[VISUAL_SLOT: FIG_4_7_E2E_CHECKPOINT_FLOW]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_4_7_e2e_checkpoint_flow.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/screenshot_home_current.png]]
[[VISUAL_CAPTION: Figure 4.7 E2E checkpoint flow for regression validation]]

## 4.6 Observed Challenges and Fixes

During implementation, several recurring issues were observed.

1. **Camera crop instability** under orientation/aspect changes.
   Fix: coordinate clamping + rotation-aware crop path + overlay transform updates.
2. **Inconsistent naming** across repeated scans of similar products.
   Fix: embedding-first recognition with Gemini fallback only when needed.
3. **Expiry ambiguity** from noisy labels and mixed manufacturing/expiry prints.
   Fix: dedicated second capture + strict output format + parse fallback.
4. **Site parser drift and anti-bot gaps** in compare adapters.
   Fix: generic multi-mode parsing plus site-specific overrides and transparent site errors.
5. **Offer noise** in aggregated results.
   Fix: relevance scoring threshold + quantity-aware penalties.
6. **Repeated-query latency**.
   Fix: fingerprint-based local cache with TTL.
7. **E2E flakiness** from non-deterministic test data.
   Fix: debug seed/clear controls used by Maestro flows.
8. **Permission-linked reminder drops**.
   Fix: runtime permission requests in UI plus worker-side permission checks.

[Table 4.11 Challenge-to-fix traceability]

| Challenge | Fix approach | Outcome |
|---|---|---|
| Camera geometry mismatch | Clamp/rotate/transform alignment | Stable crop behavior |
| Repeat-scan naming drift | Embedding reuse before AI fallback | Better title consistency |
| Expiry extraction ambiguity | Two-step capture + strict date prompt | Improved expiry reliability |
| Source variability in compare | Layered parsers + per-site errors | Graceful partial-failure behavior |
| Aggregated result noise | Relevance and quantity-aware filtering | Cleaner top results |
| Duplicate network effort | Fingerprint cache TTL | Faster repeated compares |
| Test instability | Deterministic seed and cleanup flows | Reproducible E2E runs |
| Notification policy constraints | Permission-aware worker | Safe runtime posting behavior |

## 4.7 Cybersecurity and Safety Considerations

Security and safety were integrated at module level.

- **Local-first data handling**: inventory, embeddings, location settings, and history are stored locally, reducing centralized exposure.
- **Permission-aware operations**: camera and notification actions are gated by runtime permission checks.
- **AI output validation**: model responses are treated as untrusted until format/parsing checks succeed.
- **Network boundary control**: compare adapters use constrained request profiles and explicit timeout behavior.
- **Transparent partial failures**: site-level errors are reported, avoiding false confidence from silent failures.
- **Safe reminder positioning**: reminders are advisory signals; users remain final decision-makers for consumption safety.
- **Dependency hygiene**: version-managed dependencies support controlled upgrades and security patch tracking.

[[VISUAL_SLOT: FIG_4_8_SECURITY_CONTROLS]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_4_8_security_safety_controls.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/legacy_reportfinal_img-004.jpg]]
[[VISUAL_CAPTION: Figure 4.8 Security and safety controls across scan, compare, and notify layers]]

## 4.8 Chapter Summary

This chapter showed how **GROCO – Smart Grocery Supply Management System** was built through staged integration and validated through layered testing. The implementation combined AI/ML modules (object detection, embedding similarity, and Gemini-based semantic extraction) with software modules for local persistence, adapter-based aggregation, navigation-driven UI, and periodic reminders.

The key architectural outcome is a balanced hybrid: deterministic local behavior for core inventory and recognition continuity, plus cloud-assisted inference and network comparison for richer intelligence. The key testing outcome is traceable verification across unit, integration, UI, and E2E levels, including representative input-output cases and deterministic device flows.

Overall, the implemented system is modular, resilient under partial failures, and maintainable for future upgrades in model quality, adapter robustness, and reminder precision.

[Table 4.12 Chapter 4 evidence index]
