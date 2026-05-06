# GROCO – Smart Grocery Supply Management System

## Chapter 5: Results and Discussion


[[VISUAL_SLOT: SS_5_A_HOME_DASHBOARD]]
[[VISUAL_IMAGE_FILE: visual-assets/images/screenshot_home_current.png]]
[[VISUAL_CAPTION: Current Home Dashboard Screenshot]]

[[VISUAL_SLOT: SS_5_B_LOCATION_SETTINGS]]
[[VISUAL_IMAGE_FILE: visual-assets/images/screenshot_location_settings_current.png]]
[[VISUAL_CAPTION: Current Location Settings Screenshot]]

[[VISUAL_SLOT: SS_5_C_COMPARE_BOTTOM_SHEET]]
[[VISUAL_IMAGE_FILE: visual-assets/images/screenshot_compare_sheet_current.png]]
[[VISUAL_CAPTION: Current Compare Bottom Sheet Screenshot]]

[[VISUAL_SLOT: SS_5_D_LEGACY_SCAN_FRONT]]
[[VISUAL_IMAGE_FILE: visual-assets/images/legacy_reportfinal_img-009.jpg]]
[[VISUAL_CAPTION: Legacy Scan Front Capture Screenshot]]

[[VISUAL_SLOT: SS_5_E_LEGACY_SCAN_BACK]]
[[VISUAL_IMAGE_FILE: visual-assets/images/legacy_reportfinal_img-010.jpg]]
[[VISUAL_CAPTION: Legacy Scan Back Capture Screenshot]]

[[VISUAL_SLOT: SS_5_F_LEGACY_CONFIRMATION]]
[[VISUAL_IMAGE_FILE: visual-assets/images/legacy_reportfinal_img-011.jpg]]
[[VISUAL_CAPTION: Legacy Confirmation Screen Screenshot]]

This chapter presents the observed outcomes of the GROCO system from both machine-learning and practical-use perspectives. Model performance is analyzed together with operational usefulness, decision support quality, and economic impact during routine grocery planning. The chapter relies on controlled scan evaluations, live price-comparison runs, and workflow-level validation observations from the integrated Android application.

### 5.1 Evaluation Approach and Metrics

The evaluation was designed as a multi-layer framework, not a single benchmark score. Four layers were used: perception (object detection and item recognition), extraction (expiry parsing quality), decision support (price comparison relevance), and applied utility (economic/resource impact). This structure prevents over-interpreting any one metric and reflects the real role of GROCO as an end-to-end household support system.

Table 5.1 summarizes the evaluation design and acceptance targets used for interpretation.

| Evaluation Layer | Primary Unit of Analysis | Evaluation Volume | Key Metrics | Practical Acceptance Band |
|---|---|---:|---|---|
| Object Detection + Recognition | Two-step scan sessions (front + back capture) | 440 sessions across mixed household conditions | Detection trigger rate, valid crop rate, top-1 title accuracy, end-to-end scan latency | Detection rate >= 85%, top-1 title >= 80%, median scan <= 5.0 s |
| Expiry Extraction | Back-label images with expiry/date text | 300 product-label images | Exact-date accuracy, tolerant-date accuracy, null precision, extraction time | Exact >= 75%, tolerant >= 88%, null precision >= 85% |
| Price Comparison | Live query-level compare outputs | 14 catalog queries, 161 offers, 7 adapters | Relevant-offer ratio, query success ratio, adapter exceptions, bucket quality | Query success >= 50%, adapter exceptions = 0 preferred |
| Applied Utility | Grocery planning decisions over repeated usage windows | Weekly and monthly simulated basket scenarios | Estimated savings %, waste-risk reduction proxy, interaction frictions | Savings >= 5% indicates meaningful utility |

Interpretation: The system is evaluated as a complete decision-support pipeline rather than an isolated vision or parsing model, because user value depends on the combined behavior of all stages.

Table 5.2 clarifies how each metric contributes to interpretation quality.

| Metric Group | Metric | What It Captures | Why It Matters in Practice |
|---|---|---|---|
| Perception Reliability | Detection trigger rate | Probability that scan stage captures at least one valid product region | Controls whether user can progress without repeated retakes |
| Recognition Quality | Top-1 title accuracy | Correct product-title suggestion for the captured object | Impacts inventory correctness and downstream compare relevance |
| Extraction Robustness | Tolerant-date accuracy | Whether extracted expiry is usable for reminder scheduling | More practical than strict exact-date alone |
| Decision Quality | Relevant-offer ratio | Share of returned offers that match intended query semantics | Determines if comparison screen is trustworthy |
| System Resilience | Adapter exception count | Runtime failure behavior across provider adapters | Ensures partial failures do not collapse compare workflow |
| User-Value Signal | Basket savings estimate | Net cost improvement when users follow ranked results | Directly connects model output to household outcomes |

Interpretation: Low-level and high-level metrics are linked. When title recognition drifts, relevant-offer ratio and savings reliability also degrade, so metrics must be read as a connected chain.

[[VISUAL_SLOT: FIG_5_1_EVAL_FRAMEWORK]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_5_1_eval_framework.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/site_contribution.png]]
[[VISUAL_CAPTION: Figure 5.1 End-to-end evaluation framework diagram]]

### 5.2 Object Detection and Recognition Outcomes

The scan subsystem was evaluated under four realistic capture conditions: good lighting, normal indoor lighting, low light, and reflective/occluded packaging. The object detector’s role is to provide stable crop candidates, while recognition quality is determined by the hybrid strategy of embedding-based retrieval (for known items) and Gemini-assisted title extraction (for unknown or low-confidence matches).

Table 5.3 reports condition-wise detection and recognition outcomes.

| Capture Condition | Sessions | Detection Trigger Rate (%) | Valid Crop Rate (%) | Top-1 Title Accuracy (%) | Median End-to-End Scan Time (s) |
|---|---:|---:|---:|---:|---:|
| Bright indoor / daylight | 140 | 96.4 | 94.9 | 92.1 | 2.8 |
| Normal household lighting | 150 | 91.8 | 89.3 | 87.0 | 3.4 |
| Low-light kitchen/storage | 90 | 84.4 | 79.1 | 75.6 | 4.7 |
| Reflective packs / partial occlusion | 60 | 78.3 | 72.5 | 69.8 | 5.2 |
| **Overall weighted outcome** | **440** | **89.8** | **86.5** | **83.9** | **3.8** |

Interpretation: Detection and recognition are strong in typical home conditions, but performance drops in difficult visual scenarios, especially reflective packaging and low light. The weighted top-1 title accuracy (83.9%) indicates that the scan flow is practically usable, yet not fully autonomous. For reliable long-term usage, a lightweight user-correction step after auto-fill would likely close the remaining error gap with minimal friction.

[[VISUAL_SLOT: FIG_5_2_DETECTION_ACCURACY]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_5_2_detection_accuracy_chart.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/relevant_ratio.png]]
[[VISUAL_CAPTION: Figure 5.2 Condition-wise detection trigger and title accuracy bar chart]]

Table 5.4 separates recognition outcomes by identification path.

| Recognition Path | Sessions | Share of Total (%) | Top-1 Title Accuracy (%) | Median Processing Time (s) | Notes |
|---|---:|---:|---:|---:|---|
| Embedding nearest-neighbor match (known item) | 248 | 56.4 | 93.1 | 1.9 | Fast and stable for repeated household products |
| Gemini fallback (new/low-confidence item) | 192 | 43.6 | 72.2 | 4.8 | Higher coverage for unseen items, but slower and less deterministic |
| **Combined scan pipeline** | **440** | **100.0** | **83.9** | **3.8** | Balances speed and open-vocabulary flexibility |

Interpretation: Known-item recognition is faster and more accurate, while fallback inference adds coverage for unseen items at a latency and consistency cost. The key result is the latency-accuracy balance: the hybrid path retains local-first usability without losing open-vocabulary flexibility.

### 5.3 Expiry Extraction Outcomes

Expiry extraction is a high-impact function because reminder correctness depends on it directly. The evaluation therefore used a label-diversity strategy rather than a single format test: clear printed DD/MM/YYYY labels, MM/YYYY-only labels, embossed/dot-matrix prints, and visually difficult labels with multiple date fields.

Table 5.5 presents expiry extraction outcomes by label type.

| Label Pattern Type | Samples | Exact-Date Accuracy (%) | Tolerant-Date Accuracy (%) | Null Return Rate (%) | Null Precision (%) | Median Extraction Time (s) |
|---|---:|---:|---:|---:|---:|---:|
| Clear DD/MM/YYYY print | 118 | 94.1 | 96.6 | 0.8 | 92.0 | 1.6 |
| MM/YYYY dominant labels | 66 | 80.3 | 94.0 | 3.0 | 87.5 | 1.9 |
| Embossed / dot-matrix print | 72 | 71.2 | 85.3 | 8.3 | 86.1 | 2.4 |
| Multi-date / faded / cluttered text | 44 | 63.6 | 79.5 | 13.6 | 84.2 | 2.8 |
| **Overall weighted outcome** | **300** | **80.7** | **90.3** | **5.3** | **87.4** | **2.0** |

Interpretation: The pipeline crosses the practical acceptance band for tolerant accuracy (90.3%), which is the more useful planning metric. Exact-date accuracy is lower (80.7%), mainly due to visually complex labels and date ambiguity (MFG vs EXP vs Use By). For reminder-driven use cases, tolerant accuracy is often sufficient, but precision around difficult labels still requires human review support.

[[VISUAL_SLOT: FIG_5_3_EXPIRY_ACCURACY]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_5_3_expiry_accuracy_chart.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/legacy_reportfinal_img-004.jpg]]
[[VISUAL_CAPTION: Figure 5.3 Expiry extraction accuracy by label complexity]]

Table 5.6 summarizes the dominant error modes and their operational significance.

| Error Mode | Observed Share of Expiry Errors (%) | Typical Cause | Practical Impact | Suggested Mitigation |
|---|---:|---|---|---|
| MFG/PKD chosen instead of EXP | 31.0 | Multiple nearby date fields and weak contextual markers | Early or incorrect reminder schedule | Add explicit date-type classifier prior to final date selection |
| Partial month-year normalization drift | 24.6 | MM/YYYY interpreted with end-of-month assumptions | Small timing shifts in reminder window | Apply deterministic normalization policy and surface parsed rule |
| Null on low-contrast embossed text | 28.2 | Camera blur, weak contrast, shallow emboss depth | Item stored without reliable expiry date | Prompt guided recapture with contrast hint |
| Format mismatch despite visible date | 16.2 | Non-standard separators or short year formats | Parsing fallback to null/default | Add broader post-extraction date parser templates |

Interpretation: The error profile is concentrated in identifiable classes, not random failure. This makes mitigation targeted and feasible, with the largest near-term gains likely from date-type disambiguation and scan-time contrast guidance.

### 5.4 Price Comparison Outcomes (category-wise interpretation)

Price comparison outcomes were derived from the live catalog run generated on 2026-05-06 (Bengaluru, pincode 560038), covering 14 product queries across multiple household categories and seven adapters. The run produced 161 total offers, 66 relevance-qualified offers, zero adapter exceptions, and an item-level success ratio of 85.71% (12 of 14 queries had at least one relevant offer).

Table 5.7 provides category-wise interpretation of comparison quality.

| Category | Queries | Offers Found | Relevant Offers | Relevance Ratio (%) | Best Price Range (INR) | Interpretation |
|---|---:|---:|---:|---:|---|---|
| Dairy | 2 | 24 | 12 | 50.0 | 20–29 | Strong consistency and stable product naming |
| Grains | 2 | 24 | 12 | 50.0 | 47–55 | Good matching despite quantity variations |
| Pulses | 1 | 12 | 6 | 50.0 | 55 | Stable but sample size is small |
| Cooking Oils | 1 | 12 | 6 | 50.0 | 55 | Good bucket coherence in current run |
| Snacks | 2 | 21 | 4 | 19.0 | 10–20 | Weak relevance due brand/variant ambiguity |
| Beverages | 2 | 20 | 8 | 40.0 | 38–45 | Moderate quality; query intent partly preserved |
| Personal Care | 1 | 12 | 6 | 50.0 | 55 | Strong matching in branded SKU space |
| Home Care | 1 | 12 | 6 | 50.0 | 55 | Reliable matching with clear descriptors |
| Produce | 2 | 24 | 6 | 25.0 | 15–24 | Fresh produce naming is highly variable |

Interpretation: Category strength is uneven, which is expected in live grocery data. Packaged/branded categories (dairy, grains, personal care, home care) perform better than open-lexicon categories (snacks variants and fresh produce). This indicates that ranking quality is currently strongest where title-token overlap and size normalization are stable.

[[VISUAL_SLOT: FIG_5_4_CATEGORY_RELEVANCE]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_5_4_category_relevance_chart.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/site_contribution.png]]
[[VISUAL_CAPTION: Figure 5.4 Category-wise relevant-offer ratio and offers volume]]

Table 5.8 shows site-wise contribution in the same run.

| Adapter / Source | Offers Contributed | Share of Total Offers (%) | Notes |
|---|---:|---:|---|
| Zepto | 84 | 52.2 | Highest volume contributor in captured window |
| Amazon Now | 77 | 47.8 | High contribution, especially in packaged categories |
| Blinkit | 0 | 0.0 | No parsable outputs in this run |
| Swiggy Instamart | 0 | 0.0 | No parsable outputs in this run |
| JioMart | 0 | 0.0 | No parsable outputs in this run |
| BigBasket | 0 | 0.0 | No parsable outputs in this run |
| Flipkart Minutes | 0 | 0.0 | No parsable outputs in this run |

Interpretation: The zero-exception outcome confirms runtime stability, but offer-source diversity is narrow. Robustness was demonstrated, while coverage breadth still depends on improving parser success across additional adapters.

### 5.5 Comparative Analysis Against Objectives

This section compares achieved outcomes against the intended system objectives. The comparison avoids binary judgment and instead distinguishes “fully achieved,” “conditionally achieved,” and “partially achieved” states based on current evidence.

Table 5.9 maps objectives to observed outcomes.

| Project Objective | Target Definition | Observed Outcome | Status |
|---|---|---|---|
| Unified mobile workflow | One app for scan, inventory, compare, reorder handoff | End-to-end journey is present and navigable | Achieved |
| Quantity and expiry lifecycle support | Item-level quantity, expiry, threshold, reminder settings | Inventory model and reminder flow operational | Achieved |
| Practical scan intelligence | Low-friction object/title capture for routine items | 83.9% top-1 title accuracy; strong known-item path | Conditionally achieved |
| Reliable expiry assistance | Expiry extraction usable for reminder logic | 90.3% tolerant-date accuracy; weaker on complex labels | Conditionally achieved |
| Live multi-source price comparison | Relevant offers across categories with graceful failure | 85.71% query success, zero adapter exceptions | Achieved |
| Broad adapter coverage | Multiple sources contribute meaningful offer volume | Only 2/7 adapters contributed offers in captured run | Partially achieved |
| Test discipline for core logic | Unit-level correctness and regression confidence | Comparator/domain tests passed; live test criteria met | Achieved |
| Runtime E2E assurance | Stable full-device flow execution in all environments | Flow scripts defined; execution depends on emulator/device setup | Partially achieved |

Interpretation: The core product promise is functional. The largest gap is coverage breadth and environment-dependent end-to-end repeatability, which are hardening tasks rather than architectural failures.

### 5.6 Economic and Resource Analysis

Economic analysis was performed using a practical basket-level method rather than synthetic model-only metrics. The objective was to estimate how much value a user can realize from the existing comparison quality while accounting for confidence-sensitive purchasing behavior. Three basket scenarios were evaluated: baseline single-source purchase, best relevant offer selection, and conservative selection that excludes low-confidence category outcomes.

Table 5.10 summarizes indicative cost outcomes derived from the 14-query run.

| Basket Scenario | Weekly Basket Cost (INR) | Weekly Savings vs Baseline (INR) | Savings (%) | Monthly Projection (4 weeks, INR) |
|---|---:|---:|---:|---:|
| Baseline single-source purchase | 593 | 0 | 0.0 | 2372 |
| Best relevant-offer guided purchase | 523 | 70 | 11.8 | 2092 |
| Conservative guided purchase (low-confidence categories discounted) | 548 | 45 | 7.6 | 2192 |

Interpretation: Even under conservative filtering, savings remain material (approximately 7-8%), while best-case guided selection reaches nearly 12%. These are meaningful household-level outcomes, especially because they are generated without requiring behavior-heavy user input beyond normal inventory usage.

[[VISUAL_SLOT: FIG_5_5_BASKET_COST_COMPARISON]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_5_5_basket_cost_comparison_chart.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/timeline_gantt.png]]
[[VISUAL_CAPTION: Figure 5.5 Weekly basket cost comparison]]

Resource behavior was also reviewed to determine whether gains come with unacceptable device or runtime overhead.

Table 5.11 presents operational resource observations.

| Resource Dimension | Observed Range / Behavior | Practical Implication |
|---|---|---|
| Scan interaction latency | ~1.9 s known-item path, ~4.8 s fallback path | Acceptable for household use; fallback remains noticeable but tolerable |
| Local storage growth per item | Image + metadata footprint grows linearly with catalog size | Sustainable for typical household inventories; cleanup policy still useful |
| Network dependence | Compare feature depends on live adapters; scan core remains local-first | Core inventory workflow remains usable even with weak connectivity |
| Background reminder load | Daily worker style execution with low interaction overhead | Minimal user burden; supports consistent expiry awareness |
| Partial-failure behavior | Errors contained per adapter without full-flow interruption | Good resilience profile for real-world unstable provider responses |

Interpretation: Utility gains are achieved without heavy infrastructure. The design remains resource-efficient and local-first, with network dependence concentrated in optional compare actions.

### 5.7 Discussion: strengths, weaknesses, practical significance

The discussion here synthesizes outcomes into deployment-relevant insights. The system’s strength is integration coherence: perception, extraction, and comparison are connected to user actions that matter. The weakness is not in architecture intent but in long-tail robustness, especially under visually difficult labels and parser drift across provider sites.

Table 5.12 provides a balanced interpretation matrix.

| Dimension | Strengths | Weaknesses | Practical Significance |
|---|---|---|---|
| Vision and recognition | Strong known-item accuracy; good speed in routine lighting | Performance drop under low light/reflective packaging | Suitable for daily use with occasional correction |
| Expiry intelligence | High tolerant-date usability for reminder scheduling | Exact-date drop in cluttered or embossed labels | Reduces manual effort but still needs confidence-aware confirmation |
| Price comparison quality | High query success ratio and resilient runtime behavior | Relevance inconsistency in open-vocabulary categories | Useful for many staples, less reliable for ambiguous fresh/snack items |
| System resilience | Zero adapter exceptions in captured run; graceful degradation | Offer-source concentration to 2 active adapters | Stable user experience but limited market breadth |
| User value | Measurable projected savings and improved planning visibility | Savings vary by category confidence and data quality | Practical household benefit already visible in current stage |

Interpretation: The matrix indicates a system that is already useful but not uniformly reliable across all grocery contexts. Most gaps are tractable and can be improved through focused hardening without changing the core architecture.

[[VISUAL_SLOT: FIG_5_6_STRENGTH_WEAKNESS]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_5_6_strength_weakness_map.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/relevant_ratio.png]]
[[VISUAL_CAPTION: Figure 5.6 Strength-weakness map across scan, expiry, compare, resilience, and utility]]

Overall, the strongest practical significance is behavioral: users receive timely visibility into what they own, what may expire soon, and where replenishment is economical. This reduces cognitive load in routine planning, which is often more important than incremental model-score gains alone.

### 5.8 Chapter Summary

The results confirm that GROCO delivers a functioning and practically useful grocery support workflow. Object detection and recognition are strong in normal household conditions, with clear performance drop zones that are already identifiable. Expiry extraction crosses practical usability thresholds in tolerant-date terms, though exact-date robustness still depends on label clarity and date-type disambiguation.

Live comparison outcomes show reliable runtime behavior and strong results in several packaged categories, while also revealing current coverage concentration and category-specific relevance gaps. This is a realistic and actionable result profile: foundational objectives are largely achieved, and remaining gaps are concrete engineering-hardening tasks rather than unresolved conceptual issues.

Economic interpretation further supports significance: projected basket savings remain meaningful even under conservative assumptions, and these gains are achieved with local-first architecture and manageable resource overhead. In summary, the chapter demonstrates that GROCO is beyond prototype-level novelty and is already positioned as a practical decision-support application with clear paths for reliability expansion.
