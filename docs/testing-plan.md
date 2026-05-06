# Groco Integration Testing Plan (Groco + Quick Compare)

## 1. Goals
- Verify that the integrated Android app is stable, usable, and accurate for:
  - grocery catalog management,
  - quantity and expiry tracking,
  - price comparison across providers,
  - reorder flow handoff,
  - expiry notifications.
- Keep verification practical for local Android development and CI.

## 2. Test Pyramid

### 2.1 Unit tests (highest volume)
- `domain/compare`:
  - text normalization,
  - quantity extraction and normalization,
  - offer similarity and cluster scoring,
  - query relevance filtering,
  - deterministic ranking / tie-break behavior.
- `domain/inventory`:
  - expiry status computation,
  - days-left calculations,
  - reminder window logic,
  - quantity threshold state (healthy / low / out).
- `data/adapters` parser helpers:
  - HTML/JSON extraction from representative fixtures,
  - per-site normalization (price, title, size, URL, stock).

### 2.2 Integration tests
- repository + persistence:
  - ObjectBox CRUD for catalog and history,
  - vector embeddings remain intact.
- comparison service:
  - combines parallel site adapters,
  - partial-site failures still return aggregate result,
  - cache hit/miss behavior,
  - stale cache invalidation.
- notifications:
  - WorkManager worker reads inventory,
  - creates notifications only for due items,
  - no duplicate spam for unchanged state.

### 2.3 UI tests (Compose + instrumentation)
- critical user journeys:
  - add item via scanner flow,
  - edit quantity and expiry,
  - open compare screen and sort/filter results,
  - open reorder deep link,
  - acknowledge reminder state.
- navigation and accessibility:
  - top-level tabs/screens reachable,
  - touch targets and labels present,
  - empty/error/loading states visible and recoverable.

### 2.4 Maestro E2E suite (device-level)
- Use Maestro for black-box mobile flow verification.
- Required baseline flows:
  - app launch + permissions,
  - deterministic inventory setup/cleanup,
  - scan route navigation smoke,
  - compare + reorder flows (to be added as compare feature lands).
- Current suite location:
  - `maestro/flows/full_e2e_suite.yaml`
  - `maestro/flows/inventory_seed_and_cleanup.yaml`
  - `maestro/flows/navigation_scan_smoke.yaml`
  - `maestro/flows/quantity_management_flow.yaml`
  - `maestro/flows/compare_prices_flow.yaml`
- Runner script:
  - `scripts/run-maestro-e2e.sh`

### 2.5 Manual exploratory tests
- camera edge cases:
  - blur, low-light, rotated product images.
- compare edge cases:
  - unmatched search text,
  - mixed quantity sizes (500 ml vs 1 L),
  - one or many site failures.
- reminder edge cases:
  - timezone changes,
  - past-dated expiry,
  - notification permission denied.

## 3. Test Data Strategy
- Add deterministic test fixtures for site payloads under `app/src/test/resources/compare-fixtures`.
- Maintain synthetic inventory fixture set:
  - perishable (short expiry),
  - long shelf-life,
  - no expiry parsed,
  - low stock / out-of-stock quantity.
- Include golden compare output snapshots for ranking stability.

## 4. Edge Case Matrix
- Quantity parsing:
  - `500ml`, `0.5 L`, `1 ltr`, `2 kg`, `12 pcs`, `1 pack (1 L)`.
- Price parsing:
  - `₹45`, `Rs. 45`, `INR 45.50`, malformed strings.
- Search relevance:
  - stop words only,
  - brand synonyms,
  - typo-tolerant query.
- Notification timing:
  - due today,
  - due in N days,
  - already expired.

## 5. Non-Functional Checks
- performance:
  - compare call target under acceptable latency with concurrent adapters,
  - memory usage bounded during parse + clustering.
- reliability:
  - adapter timeout handling,
  - retry/backoff for transient site failures.
- usability:
  - no dead-end screens,
  - minimal taps for frequent tasks.

## 6. Verification Commands
- Unit tests (Android/JVM):
  - `JAVA_HOME=<JDK17> ./gradlew testDebugUnitTest`
- Lint:
  - `JAVA_HOME=<JDK17> ./gradlew lintDebug`
- Build:
  - `JAVA_HOME=<JDK17> ./gradlew assembleDebug`
- Instrumented/UI tests (requires emulator/device + Android SDK):
  - `JAVA_HOME=<JDK17> ./gradlew connectedDebugAndroidTest`
- Maestro E2E (requires emulator/device + Android SDK platform tools):
  - `./scripts/run-maestro-e2e.sh`

## 7. Current Environment Constraints (Observed)
- Gradle build fails on default JDK 25 (`JavaVersion.parse(25)` from Kotlin tooling).
- JDK 17 is available and should be used for this project.
- Android SDK path is not configured in this environment (`local.properties` / `ANDROID_HOME` missing), so local build and instrumentation tests cannot complete until SDK is configured.

## 8. Exit Criteria
- All new/updated unit tests passing.
- Integration tests passing for compare and reminder flows.
- UI instrumentation suite passes on at least one target API level.
- Manual exploratory checklist executed with no P1/P2 issues open.
