# Chapter 3: System Design and Methodology

This chapter presents the system design and methodology of **GROCO – Smart Grocery Supply Management System**. The design objective was to combine practical grocery inventory management with an AI-assisted recognition and comparison pipeline that is usable on mid-range Android devices, economical to maintain, and extensible for future improvements. The methodology therefore balances model-centric decisions (detection, similarity, extraction, ranking) with implementation architecture decisions (module boundaries, data persistence, caching, and failure handling).

The chapter is organized from constraints to architecture, then to AI/ML logic, price comparison workflow, data modelling, tool selection, and risk-aware design decisions. Emphasis is placed on reproducible behavior, local-first operation, and graceful degradation under uncertain data conditions.

## 3.1 Design Considerations and Constraints (Technical, Economic, and Sustainability)

The design of GROCO was shaped by real constraints rather than ideal laboratory assumptions. The system needed to run in a mobile environment where camera input quality, network conditions, and e-commerce page structures are variable. At the same time, the product had to remain affordable to build and operate without introducing heavy backend infrastructure.

From a technical perspective, the first constraint was **mobile resource boundedness**. The recognition pipeline had to work with limited battery, memory, and thermal headroom. This motivated an architecture where frequently used states (inventory, reminders, location preferences, comparison history) are kept on-device in local storage. It also motivated bounded network timeouts and selective AI calls, so that occasional slow dependencies do not freeze the user workflow. Camera capture was designed as a guided two-step process (front image and expiry-side image) to reduce ambiguity and improve extraction quality without requiring multi-frame complex tracking.

A second technical constraint was **heterogeneous visual and textual product data**. Packaging layouts differ across brands, product categories, and printing styles. Expiry fields may appear as “EXP”, “Best Before”, or date-only stamps. This means deterministic parsing alone is brittle, while fully free-form extraction can be inconsistent. The adopted methodology therefore uses a structured extraction strategy with controlled fallback behavior: confidence-based similarity reuse when possible, explicit date format constraints, and fallback AI interpretation when deterministic extraction is uncertain.

A third technical constraint was **external marketplace instability** for price comparison. The compare subsystem depends on public-facing marketplace responses where HTML structure, embedded scripts, and anti-bot behavior can change. If not handled correctly, this can convert one source failure into complete feature failure. GROCO addresses this by using adapter isolation, per-site error capture, and partial result composition. Even when one or more providers return no parsable data, the user still receives ranked offers from available sources.

The primary economic constraint was **low operational cost**. A backend-heavy architecture with central databases, scheduled crawlers, and hosted vector services was intentionally avoided for this phase. A local-first design with ObjectBox persistence reduces recurring hosting and observability costs, and minimizes deployment complexity. AI inference is called only for high-value tasks (product title extraction and expiry interpretation), instead of routing all matching and ranking through cloud services. This selective usage pattern keeps inference expense predictable.

Another economic consideration was **maintenance burden**. Feature design prioritized modules with clear ownership boundaries: scanner orchestration, inventory persistence, compare adapters, offer matching, and reminders. This improves maintainability for small teams because updates can be made in one subsystem without refactoring the entire application.

Sustainability considerations influenced both the problem framing and system behavior. At the application level, GROCO targets reduction of household food waste by surfacing expiry proximity and stock levels, nudging users toward timely consumption. At the software level, sustainability also means compute efficiency: repeated network fan-out is reduced through short-term local comparison caching, and local inventory operations remain functional even when network access is unavailable.

Design choices were therefore evaluated on three axes simultaneously:

1. Technical robustness under noisy inputs and variable connectivity.
2. Economic feasibility under minimal infrastructure budget.
3. Sustainability impact through waste-aware user workflows and efficient computation.

[Table 3.1 Design constraints, response decisions, and trade-offs]

[Figure 3.1 Constraint-to-design mapping for GROCO modules]

These constraints also informed non-functional goals for the chapter methodology: bounded latency for core flows, failure transparency instead of silent breakdowns, and storage designs that support future analytics without violating local-first behavior.

## 3.2 Overall System Architecture (Scan Pipeline + Inventory + Compare + Reminders)

The overall architecture follows a **modular, pipeline-oriented Android design**. It can be understood as four connected subsystems: (i) scan and registration, (ii) local inventory intelligence, (iii) multi-source price comparison, and (iv) reminder automation.

At the application shell level, navigation routes connect Home, Scan, Compare, and Location Settings screens. The Home route is the operational center where stored items, stock condition, and compare actions are visible. The Scan route feeds inventory creation. The Compare route is invoked directly from inventory items for contextual shopping decisions. The Settings route stores location preferences and result limits used by comparison requests.

The **scan pipeline** starts with camera initialization and streaming object detection. A guided two-step capture process improves context quality:

1. Front image capture for product identity cues.
2. Label-side capture for expiry-related text cues.

For each captured candidate, a lightweight embedding vector is generated (normalized grayscale representation), then used for nearest-neighbor lookup in the local vector index. If a close local match exists, existing title knowledge can be reused; otherwise title extraction is requested through AI inference. The expiry-side image is processed for date extraction. After resolution, the item is persisted with title, embedding, image path, expiry timestamp, quantity/unit defaults, and reminder thresholds.

The **inventory subsystem** persists and exposes grocery items in real time. Item state includes both semantic information (title, expiry date) and operational information (quantity, unit, low-stock threshold, remind-before window). Quantity controls in the UI allow rapid adjustments without opening secondary forms. This keeps daily usage lightweight and enables automatic low-stock cues.

The **price comparison subsystem** is triggered from inventory context. The selected item title becomes the query seed; stored location preferences (city, area, pincode, optional coordinates, per-site cap) are injected automatically. The repository then executes adapter fan-out in parallel across configured providers. Each adapter returns normalized offers or site-level failure signals. Results from all sites are merged, filtered for relevance, clustered by quantity-aware similarity, and ranked. The UI presents clustered alternatives with confidence, price ordering, and direct provider links for reorder handoff.

The **reminder subsystem** is implemented using periodic background work. A scheduled worker scans inventory once per interval, computes days remaining for each item, and dispatches notifications when `daysRemaining <= remindBeforeDays`. This turns static data into proactive action. The reminder engine is independent of compare and scan network state, so it remains functional offline.

Architecturally, GROCO uses dependency injection to keep module coupling low. Storage boxes, network client, adapters, and matcher components are provided centrally and consumed by feature modules. This allows controlled substitution of adapters, matcher thresholds, or extraction strategies in future releases.

A key architectural property is **graceful partial operation**. If network providers fail, inventory still works. If AI extraction fails for a scan, fallback paths preserve item creation with minimal defaults. If reminders cannot run due permission denial, inventory continuity is unaffected. This containment is essential for a consumer utility application where users expect continuity over perfection.

[Figure 3.2 End-to-end architecture: camera-to-inventory-to-compare-to-reminders]

[Figure 3.3 Scan registration data flow with dual-step capture]

[Figure 3.4 Multi-site compare and reorder handoff workflow]

[Table 3.2 Component responsibility matrix (UI, domain, data, background tasks)]

The architecture therefore combines AI-enabled decision support with a dependable local control plane. AI enriches user context, while core lifecycle state remains device-local and immediately accessible.

## 3.3 AI/ML Methodology

The AI/ML methodology in GROCO is not based on a single monolithic model. Instead, it combines complementary mechanisms: object-level detection for region relevance, embedding-based similarity for continuity and reuse, and expiry extraction with deterministic interpretation plus LLM fallback logic. This layered approach was chosen to balance speed, robustness, and practical deployment constraints on Android.

### 3.3.1 Object Detection Logic

Object detection is used as a **region-of-interest discovery step** rather than final classification authority. During camera stream analysis, detected bounding regions are overlaid for user guidance and captured on demand. The objective is to isolate product regions from noisy backgrounds and improve downstream extraction quality.

This design has three advantages:

1. It avoids requiring category-specific model retraining for every grocery brand.
2. It improves the signal-to-noise ratio for similarity and extraction modules.
3. It supports a user-in-the-loop workflow where capture timing is controlled by the user.

Detection confidence from the stream informs whether to proceed with capture, but final item identity is resolved through similarity reuse and AI title extraction logic. This separation avoids over-trusting detector labels, which are often generic for retail packaging.

### 3.3.2 Embedding Generation and Similarity Search

After region capture, GROCO generates a compact visual embedding from resized grayscale pixel structure. The embedding is persisted in a vector-enabled local store and indexed for approximate nearest-neighbor retrieval. This enables reuse of known item identities across repeated scans.

A practical acceptance threshold is applied to nearest-neighbor results. If similarity is strong, the stored title can be reused with high confidence; otherwise the pipeline invokes title extraction inference. In the current implementation profile, this thresholding behavior maps to nearest-neighbor score filtering, where only sufficiently close matches are accepted for identity reuse.

This methodology offers two benefits:

1. **Continuity**: repeat scans of familiar products avoid redundant AI calls.
2. **Stability**: item naming becomes more consistent over time as local memory grows.

Because this similarity memory is local, it also improves privacy and offline usability.

### 3.3.3 Expiry Extraction: OCR-Led Interpretation with LLM Fallback

Expiry extraction is treated as a high-impact step because it directly drives reminder timing and potential waste reduction. The methodology uses a two-stage interpretation strategy:

1. Structured date extraction from packaging text cues (OCR-style deterministic parsing and regex normalization).
2. LLM fallback for ambiguous, low-contrast, or irregular print patterns.

In design terms, the first stage prioritizes precision and cost-efficiency by trying deterministic interpretation of candidate date strings (e.g., `DD/MM/YYYY`, `MM/YYYY`, and related patterns). The second stage is invoked when deterministic confidence is low, conflicting dates are detected (manufacture vs expiry), or text legibility is poor.

The fallback prompt is constrained to return only a normalized date token (or null when unavailable), reducing verbose responses and simplifying parser integration. Additional rule guidance asks the model to prefer later date interpretation when both manufacturing and expiry-like dates are present, which aligns with practical packaging conventions.

The acceptance logic is confidence-gated: deterministic extraction is preferred when confidence is sufficient, while fallback interpretation is accepted only when it passes its own confidence threshold.

In current runtime behavior, cloud inference is actively used for expiry extraction due strong variability in real packaging prints. The OCR-first stage remains part of the design methodology for future hardening, with expected benefits of reduced network dependency and lower inference cost.

### 3.3.4 Why a Layered AI Methodology Was Chosen

A single-model approach could appear simpler, but it would reduce control over failure modes and complicate debugging. GROCO instead uses a layered logic where each stage has a clear role:

1. Detection isolates relevant regions.
2. Embeddings provide memory-based reuse.
3. Structured extraction provides deterministic consistency.
4. LLM fallback handles ambiguity.

This is more aligned with production reliability goals than purely end-to-end prediction, especially in consumer apps where image quality and textual patterns are highly variable.

[Figure 3.5 AI pipeline: detection, embedding memory, extraction, fallback]

[Table 3.3 AI/ML stages, inputs, outputs, confidence signals, fallback rules]

The resulting methodology is both practical and extensible: stronger OCR modules, improved embeddings, or category-specific detectors can be upgraded independently without rewriting the entire pipeline.

## 3.4 Price Comparison Methodology

The price comparison methodology transforms marketplace data from multiple heterogeneous sources into ranked and actionable offer clusters. The process includes source collection, normalization, relevance filtering, quantity-aware clustering, and final ranking.

### 3.4.1 Multi-source Offer Collection

The compare subsystem uses provider-specific adapters under a common search contract. Each adapter constructs query URLs, applies headers suitable for mobile browsing context, and attempts to parse offers from structured and semi-structured content (such as linked JSON, embedded JSON blocks, and product-card HTML).

This adapter-based design provides two benefits:

1. Site-specific changes are isolated to one adapter rather than breaking the entire compare module.
2. Parallel execution across adapters reduces aggregate response latency relative to serial requests.

Each provider yields a bounded number of offers per request, and per-site failures are recorded explicitly. The user thus receives both usable results and transparency about unavailable providers.

### 3.4.2 Offer Normalization

Raw offers are normalized into a shared schema containing site, title, price, currency, brand, size text, stock flag, URL, and image URL when available. Normalization includes:

1. Text cleaning (lowercase, punctuation cleanup, stopword removal).
2. Unit extraction and standardization (`kg/g`, `l/ml`, `pcs`).
3. Price parsing into numeric form.
4. Duplicate suppression by `(normalized title, rounded price)` keys.

Normalization is mandatory because raw marketplaces use inconsistent naming and display formats. Without normalization, cross-site matching quality degrades and ranking becomes misleading.

### 3.4.3 Quantity-aware Matching and Clustering

A major source of false comparisons in grocery domains is pack-size mismatch (for example, comparing 500 ml vs 1 L directly). GROCO addresses this through quantity extraction and compatibility scoring. Offers are first bucketed by normalized quantity, then clustered by semantic similarity within compatible groups.

The core pairwise offer similarity score is:

\[
S_{offer} = 0.55\,S_{text} + 0.30\,(100\,S_{qty}) + 0.15\,S_{brand} - P_{qty}
\]

where:

1. \(S_{text}\): token-based title similarity.
2. \(S_{qty}\): quantity compatibility in \([0,1]\).
3. \(S_{brand}\): brand similarity.
4. \(P_{qty}\): penalty applied when quantity compatibility is below threshold.

This weighting scheme is intentionally text-dominant but quantity-sensitive, reflecting that product wording and pack equivalence are both critical for fair comparison.

### 3.4.4 Relevance Filtering and Ranking

Before final clustering, each candidate offer is scored against the user query using blended token-set and partial-match similarity. The practical relevance rule is:

\[
S_{rel} = 0.7\,R_{token} + 0.3\,R_{partial} + B_{contain}, \quad \text{accept if } S_{rel} \ge 44
\]

where \(B_{contain}\) is a small bonus when normalized query appears directly in title text. This threshold reduces noisy, loosely related products.

After relevance filtering, matched clusters are ranked by query affinity and presented with confidence and ascending prices. The user-facing ranking principle can be represented as:

\[
RankScore_k = \lambda \cdot Conf_k + (1-\lambda) \cdot (1 - PriceNorm_k)
\]

where \(Conf_k\) captures semantic confidence of cluster \(k\) and \(PriceNorm_k\) captures relative cost among matched clusters. This expresses the practical design intent: the best recommendation is not only cheapest, but also semantically reliable.

### 3.4.5 Local Caching and Compare Reuse

Repeated comparison queries in short time windows are common in household decision cycles. GROCO therefore computes a deterministic request fingerprint from normalized query and location context, and stores serialized compare results with a time-to-live. If the same request is repeated within the freshness window, local cache is returned immediately.

Benefits include:

1. Lower repeated network load.
2. Faster perceived response time.
3. Reduced cost and energy from unnecessary fan-out.

This cache strategy is intentionally short-lived to balance freshness and efficiency.

[Figure 3.6 Multi-source collection, normalization, matching, and ranking flow]

[Table 3.4 Example normalization and quantity-bucket transformation]

[Table 3.5 Ranking factors and threshold settings]

Overall, the comparison methodology is designed to deliver actionable choices rather than raw scrape dumps. It favors relevance, quantity fairness, and transparent degradation under partial source availability.

## 3.5 Data Modelling and Local Storage Strategy

Data modelling in GROCO is centered on a local-first persistence layer that supports low-latency interaction, offline continuity for inventory functions, and efficient retrieval for both reminders and comparison reuse.

Three primary entities define the storage strategy:

1. **GroceryItem**: item identity and lifecycle state.
2. **ComparisonHistory**: query-context result cache.
3. **LocationSettings**: reusable user location and compare limits.

`GroceryItem` includes title, image path, expiry timestamp, quantity, unit, low-stock threshold, and remind-before days. It also stores embeddings for visual memory reuse and nearest-neighbor retrieval. This entity bridges AI outputs and user operations: scan-generated attributes become inventory intelligence signals over time.

`ComparisonHistory` captures a compact persistence of compare responses keyed by request fingerprint. Stored fields include query text, location context snapshot, response payload, and creation timestamp. This enables deterministic cache reuse while preserving request transparency.

`LocationSettings` stores city, area, pincode, optional coordinates, and per-site result caps. Persisting this configuration eliminates repetitive data entry and improves user experience in repeated compare flows.

The local persistence engine was chosen for both conventional object storage and vector lookup capability. The vector index supports approximate nearest-neighbor matching directly on device. This design avoids separate vector infrastructure and keeps latency bounded for the recognition reuse path.

From a modelling perspective, data can be viewed in two categories:

1. **Operational state**: current inventory quantities, reminder thresholds, and settings.
2. **Decision support state**: embeddings and compare history used to improve inference reuse and response speed.

The strategy intentionally avoids over-normalized multi-table relational complexity for this phase. Instead, a focused schema keeps read/write operations simple and predictable on mobile runtime.

### 3.5.1 Storage Lifecycle and Data Flow

The storage lifecycle follows this sequence:

1. Scan pipeline produces item features and extracted attributes.
2. Item record is inserted into local inventory store.
3. Home inventory view subscribes to live changes and renders updated state.
4. Compare query uses item title + location settings.
5. Repository checks comparison cache before live adapter fan-out.
6. Fresh compare result is stored for short-term reuse.
7. Reminder worker reads inventory periodically and triggers due notifications.

This lifecycle provides a closed loop where each subsystem both reads and enriches local state.

### 3.5.2 Data Integrity and Practical Controls

Data integrity is handled through practical controls rather than heavyweight distributed mechanisms:

1. Numeric bounds for quantity and per-site limits.
2. Standardized date parsing to prevent malformed expiry values.
3. Threshold-based relevance filtering to reduce compare noise.
4. Time-bounded cache validity to prevent stale recommendation dominance.

Because all primary data remains local, privacy exposure is minimized. Only selected inference calls require network transmission of captured imagery for AI interpretation.

### 3.5.3 Why Local-first Was Preferred

Local-first modelling was selected for four reasons:

1. Fast UI updates without backend round trips.
2. Lower operational cost and deployment complexity.
3. Better resilience in unstable network environments.
4. Strong alignment with personal inventory privacy expectations.

The trade-off is absence of automatic cross-device sync in the current phase. However, the schema and module boundaries are suitable for later addition of optional cloud sync without disrupting current user flows.

[Figure 3.7 Local entity model and subsystem read/write links]

[Table 3.6 Entity definitions, key fields, and ownership modules]

The data strategy is therefore intentionally conservative: minimal schema, high practical utility, and direct support for AI-assisted workflows.

## 3.6 Tool and Technique Selection with Justification

Tool selection in GROCO was guided by compatibility with Android-first deployment, maintainability for small teams, and support for both AI-enabled and deterministic workflows.

### 3.6.1 Application and UI Stack

Jetpack Compose was selected for the UI layer due declarative state-driven rendering and rapid iteration. Since inventory and compare states change frequently, Compose reduces boilerplate in propagating state updates from ViewModel flows to the interface.

Navigation components were used for clear route boundaries across Home, Scan, Compare, and Settings. This structure aligns with modular feature growth.

### 3.6.2 Camera and Detection Stack

CameraX was selected for lifecycle-aware camera control and stable integration on Android devices. It simplifies preview, analysis binding, and resource management.

ML Kit object detection was selected for on-device region identification because it offers robust Android integration and low setup overhead compared to building and maintaining a custom detector training pipeline in this phase.

### 3.6.3 AI Inference and Extraction Stack

Firebase AI with Gemini model access was selected for constrained natural-language extraction tasks where packaging variability makes rigid rules fragile. This is especially relevant for title normalization and expiry interpretation.

The methodological safeguard is constrained prompting and explicit output formatting requirements, which reduce downstream parse ambiguity.

### 3.6.4 Storage and Data Access Stack

ObjectBox was selected because it supports both high-performance local object persistence and vector indexing in one stack. This dual role is particularly valuable for mobile AI workflows where embedding reuse is required.

Dependency injection through Hilt was selected to keep adapters, repositories, and storage access testable and loosely coupled.

### 3.6.5 Compare Networking and Parsing Stack

Ktor (OkHttp engine) was selected for coroutine-friendly concurrent requests and timeout controls. Jsoup and JSON parsing utilities were selected to handle heterogeneous marketplace structures including HTML cards and embedded JSON.

The adapter abstraction was chosen as a technique rather than a library feature. It converts a volatile scraping domain into manageable site-specific modules.

### 3.6.6 Background Work and Verification Stack

WorkManager was selected for periodic reminder scheduling because it provides OS-compatible reliability for deferred work across device states.

For verification, a layered approach was selected:

1. Unit tests for matching and quantity logic.
2. Live compare tests for multi-category source behavior.
3. Device-level flow automation with Maestro for critical journeys.

This combination gives better confidence than relying on one testing level.

[Table 3.7 Tool/technique selection, alternatives considered, and reasons]

[Figure 3.8 Tooling map across scan, compare, storage, and reminder subsystems]

The selected stack is therefore not only technically capable but also aligned with long-term maintainability and constrained project economics.

## 3.7 Risk-aware Design Choices

Risk-aware design in GROCO focuses on early containment of known uncertainty sources: variable image quality, unstable external sources, ambiguous expiry cues, and network fluctuations. Instead of attempting to eliminate all uncertainty, the system isolates failures and preserves continuity.

### 3.7.1 AI and Extraction Risks

The first risk cluster concerns incorrect item identity or expiry extraction. A wrong title can reduce compare relevance; a wrong expiry can produce mistimed reminders. Mitigation includes:

1. Using similarity reuse for known items before invoking fresh inference.
2. Constraining extraction outputs to strict formats.
3. Applying null-safe fallback handling instead of forcing uncertain values.
4. Preserving user-visible inventory states so corrections can be applied in future iterations.

This design favors transparent imperfection over hidden overconfidence.

### 3.7.2 Marketplace and Adapter Risks

The second risk cluster concerns source instability. E-commerce pages can change structure or block requests. A naive design would fail completely when one parser breaks. GROCO mitigates this with:

1. Adapter isolation by provider.
2. Parallel fan-out with bounded timeouts.
3. Per-site error recording in response payload.
4. Ranking over available offers instead of hard failing.

As a result, users still receive partial yet useful comparisons.

### 3.7.3 Relevance and Ranking Risks

A third risk is false equivalence between mismatched pack sizes. This can mislead purchase decisions. Mitigation is quantity-aware grouping and penalty-based similarity scoring. Relevance thresholds suppress low-quality candidates, while confidence surfaced in UI indicates semantic reliability.

This combination reduces noisy results and increases trust in top recommendations.

### 3.7.4 Background and Notification Risks

A fourth risk is reminder fatigue or unreliable background execution. Over-frequent alerts reduce user trust. Mitigation includes item-specific reminder windows and notification gating based on computed days remaining.

Permission-aware notification dispatch prevents runtime crashes when notification permissions are denied.

### 3.7.5 Data and Privacy Risks

Because grocery inventory can reveal personal consumption behavior, data exposure risk is non-trivial. Local-first persistence limits central data aggregation. Only selective scan imagery for inference leaves device context.

Comparison cache is time-bounded to reduce stale accumulation and unnecessary retention.

### 3.7.6 Operational and Maintainability Risks

As feature scope grows, module coupling can become a risk. The architecture mitigates this via clear boundaries (scan orchestration, compare repository, adapters, reminders, settings). This allows targeted maintenance and incremental upgrades.

Risk handling in GROCO can be summarized as **degrade gracefully, expose uncertainty, and keep critical user loops operational**.

[Table 3.8 Risk matrix: probability, impact, mitigation, residual risk]

[Figure 3.9 Failure-containment strategy and graceful degradation paths]

This risk-aware design mindset is essential for practical consumer AI systems, where real-world variability is the norm and usability under imperfect conditions is a core quality attribute.

## 3.8 Chapter Summary

This chapter detailed the design and methodology of **GROCO – Smart Grocery Supply Management System** as a balanced integration of AI/ML reasoning and production-oriented mobile architecture.

The system was designed under clear technical, economic, and sustainability constraints: mobile resource limits, unstable external data sources, minimal infrastructure budget, and a primary objective of reducing household grocery waste. These constraints directly shaped the architecture into four coordinated subsystems: scan registration, inventory intelligence, multi-source comparison, and reminder automation.

The AI/ML methodology was presented as a layered strategy: detection for region relevance, embeddings for local memory and similarity reuse, and expiry extraction through deterministic interpretation with LLM fallback for ambiguous cases. This was combined with a practical comparison methodology covering multi-site collection, normalization, relevance filtering, quantity-aware clustering, and confidence-linked ranking.

Data modelling emphasized local-first persistence through focused entities for inventory, compare history, and location settings, enabling offline continuity and low-latency interactions. Tool choices were justified based on deployability, maintainability, and functional fit for Android-first operation.

Finally, risk-aware design choices were described across AI uncertainty, source instability, ranking quality, reminder reliability, privacy, and maintainability. The key principle throughout is graceful degradation: when one subsystem weakens, the full user journey should still remain usable.

The resulting methodology establishes a robust foundation for subsequent chapters on implementation, testing, and measured outcomes.
