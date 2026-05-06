# GROCO – Smart Grocery Supply Management System

## Chapter 6: Conclusion and Future Scope

### 6.1 Summary of Achievements
The project set out to solve a practical household problem: grocery inventory gets scattered across memory, paper notes, and shopping apps, which leads to avoidable waste, missed expiry dates, and unplanned spending. The implemented system addresses this gap through a unified mobile workflow that combines item capture, inventory intelligence, expiry awareness, and cost-aware reorder support. The final output is not a concept note or isolated prototype feature; it is an integrated Android application flow where scanning, storing, tracking, comparing, and reminding operate as one user journey.

A major achievement is the transition from a recognition-only flow to a decision-support flow. Earlier approaches in similar student implementations typically stop at detecting products or extracting text. GROCO extends this into actionable inventory management: once an item is identified, the app preserves meaningful context (title, image/embedding representation, expiry, quantity state), then supports day-to-day decisions such as whether to consume first, reorder now, or wait. This shift from “AI demo” to “assistive system” is a strong outcome for an applied software project.

Another important achievement is architecture consolidation. The price-comparison logic that was originally developed as a separate proof-of-concept was integrated into the Android-first structure. This reduces context switching for users and improves maintainability for developers because data handling, UI actions, and reminders remain in one product boundary. The integrated structure also improves testability by allowing domain utilities, adapters, and flow behavior to be validated under a common project setup.

From a technical perspective, the solution demonstrates a hybrid intelligence strategy:
- on-device perception and retrieval for speed and baseline reliability,
- local persistence for continuity and low-friction reuse,
- selective AI inference for cases where deterministic extraction is weak,
- modular adapters for market comparison behavior.

This balance is significant because fully cloud-dependent designs often increase cost, latency, and privacy risk, while fully offline designs struggle with open-world product variation. GROCO chooses a practical midpoint: high-frequency interactions stay lightweight and local, while ambiguous cases can use AI fallback paths.

The implementation also demonstrates balanced feature expansion across user-facing and system-facing layers. User features include scan-based item capture, inventory visibility, quantity/expiry context, reminders, and compare/reorder actions. System features include similarity retrieval, structured repository behavior, scheduled background work, and recoverable UI states under partial failures.

Validation coverage is another concrete achievement. The work includes planned checks across unit logic, integration behavior, build/lint gates, and device-level flow verification scripts. Even where environment constraints delay complete execution, the verification design remains repeatable and extensible.

The project also aligns with core engineering outcomes: modular decomposition, measurable objectives, AI/ML integration on mobile constraints, responsible data handling, and maintainable implementation patterns.

Overall, the main achievements can be summarized as follows:

| Objective Area | Planned Intent | Achieved Outcome |
|---|---|---|
| Smart item onboarding | Capture grocery details with low manual effort | Scan-assisted capture with AI-supported title/expiry extraction |
| Inventory intelligence | Maintain actionable item state | Local catalog with quantity/expiry context and near-term usability |
| Waste prevention | Improve consumption timing awareness | Expiry-aware tracking with reminder-ready scheduling path |
| Cost optimization | Support informed reordering | Integrated multi-source price comparison and provider handoff |
| Mobile-first robustness | Keep common flows fast and reliable | Local-first persistence and selective fallback strategy |
| Engineering quality | Ensure repeatable validation | Structured test plan across unit, integration, and E2E flows |

In short, GROCO demonstrates that a student-built Android system can combine AI assistance, inventory tracking, and market comparison in a way that is practical for everyday use, technically coherent, and extensible for future releases.

### 6.2 Social, Environmental and Ethical Impact
The social relevance of this project is centered on daily decision support for households. Grocery management is often not treated as a technical domain, yet poor visibility over stock and expiry affects family budgeting, nutrition continuity, and purchase discipline. By bringing reminders, quantity awareness, and compare support into one interface, the system lowers cognitive load for non-technical users. This is especially useful for busy homes where multiple people buy items but no shared inventory memory exists.

A second social impact is behavioral regularization. When users repeatedly see “what should be consumed soon” and “what is low in stock,” they tend to shift from reactive purchasing to planned replenishment. This can reduce rushed orders, duplicate purchases, and silent stockouts.

The environmental impact is tied to preventable food waste. Food waste is not only an economic issue but also a resource and emissions issue because wasted items embed water, energy, transport, and packaging costs. Any system that improves expiry visibility and consumption sequencing has potential to reduce this avoidable loss. GROCO contributes at the point where many losses happen in practice: inside homes, after purchase, due to weak tracking habits.

Environmental gains can be expected through three practical mechanisms:
- fewer items forgotten beyond expiry,
- better stock rotation (consume older items first),
- more deliberate reordering instead of panic buying.

In addition, compare-led purchasing can indirectly reduce unnecessary logistics churn when users make fewer repetitive emergency orders. The effect size depends on user behavior, but the direction is meaningful and aligns with sustainable consumption goals.

Ethically, the system takes a responsible direction by keeping core household inventory data local-first. This reduces default exposure of sensitive behavioral data such as what users buy, how often they replenish, and what consumption patterns may reveal about routines. Selective inference usage is restricted to specific tasks where value justifies it, rather than streaming all user data by default.

The project also recognizes ethical risks and addresses them explicitly:
- AI extraction may be uncertain for poor-quality images.
- Market comparison sources may be incomplete at runtime.
- Notifications can become noisy if poorly tuned.

Instead of hiding these realities, the design can expose recoverable states (for example, user correction when extraction is weak, partial-source transparency when comparison is incomplete, reminder configuration controls). This is better than presenting false certainty.

From an inclusiveness perspective, the app relies on familiar smartphone interactions and does not require specialized hardware. For wider accessibility, future improvements should include multilingual labels, readability controls, and lightweight onboarding guides.

The project’s ethical stance can be summarized through four operating principles:
- utility first: solve concrete household pain points;
- privacy by default: keep frequent data local;
- transparency over perfection: show uncertainty states;
- human override: allow correction instead of forcing automation.

These principles make the project suitable for responsible deployment as a practical assistant rather than an opaque automation engine.

### 6.3 Limitations
Although the implemented system is functionally complete for its current scope, several limitations remain and should be acknowledged clearly.

1. Product capture quality is image-dependent. Low light, reflective packaging, unusual fonts, or motion blur can reduce extraction confidence.
2. Comparison coverage is runtime-dependent. Some provider sources may intermittently fail due to anti-bot controls, location restrictions, or layout changes.
3. Inventory is currently device-centric. Without optional sync, multi-user households cannot maintain a shared real-time list across phones.
4. Reminder usefulness depends on user configuration and engagement. If permissions are denied or reminder windows are not tuned, outcome quality drops.
5. Comparison does not guarantee final checkout parity. Market prices and availability can change between fetch time and provider checkout.
6. Explainability is still basic. Users see outcomes but not always the deeper rationale behind similarity scores or confidence transitions.
7. Dataset and usage diversity are still limited for academic timeline constraints. More field usage across varied households is needed for stronger generalization claims.
8. End-to-end verification remains environment-sensitive. Emulator/device setup quality can affect repeatability of full automation runs.

These limitations are acceptable for the current stage, but they define the boundary between a strong academic build and a production-grade consumer platform.

### 6.4 Future Scope
The future roadmap should preserve the current strengths (local-first design, modular architecture, practical UX) while improving robustness, intelligence quality, and collaboration support.

In the near term, the most valuable enhancement is guided correction and confidence-aware UX. Instead of silently accepting weak extraction, the app can prompt lightweight confirmation for uncertain fields. This keeps user effort low while improving stored data quality.

The second high-impact direction is adaptive reminders. Current reminder logic can evolve from fixed thresholds to behavior-informed scheduling using consumption velocity and item category patterns. For example, perishables can receive denser reminder cadence than long shelf-life goods, and low-stock alerts can consider recent usage trends.

The third priority is resilient comparison infrastructure. Adapter hardening should include stronger parser fixtures, configurable fallbacks, and structured source health reporting. This would improve reliability under platform changes and provide clearer user trust cues when some sources are degraded.

A fourth direction is collaborative inventory support. Optional household sharing (through secure sync and role controls) would allow families to maintain one common inventory state across members. This can directly reduce duplicate purchases and improve planning for shared kitchens.

A fifth direction is analytics and forecasting. With privacy-respecting aggregation, the app can provide demand forecasting, monthly spend trends, and replacement window suggestions. These features should remain opt-in and interpretable.

A practical medium-term roadmap is shown below:

| Current Limitation | Proposed Enhancement | Expected Benefit |
|---|---|---|
| Uncertain scan outcomes in hard images | Confidence-aware review step before save | Higher title/expiry correctness |
| Static reminder thresholds | Adaptive reminder logic based on usage and category | Better timing, fewer missed or noisy alerts |
| Intermittent provider retrieval gaps | Multi-strategy adapter fallback + health indicators | More stable comparison experience |
| Single-device inventory | Optional secure household sync | Shared planning across family members |
| Limited explainability | Show concise reason tags for compare/ranking outcomes | Higher user trust and controllability |
| No predictive planning | Trend analysis + reorder window suggestions | Proactive budgeting and reduced stockouts |

For long-term extension, the project can explore multimodal packaging-text models, region-aware catalog ontologies, and privacy-preserving personalization after baseline reliability is stabilized.

In summary, future work should not replace the existing foundation; it should strengthen it through reliability engineering, user-centered explainability, and optional collaborative intelligence.

## References (IEEE-Style Draft)
[1] W. Liu, D. Anguelov, D. Erhan, C. Szegedy, S. Reed, C.-Y. Fu and A. C. Berg, “SSD: Single Shot MultiBox Detector,” in *Proc. ECCV*, 2016. [Online]. Available: https://arxiv.org/abs/1512.02325

[2] A. Howard *et al*., “Searching for MobileNetV3,” in *Proc. ICCV*, 2019. [Online]. Available: https://openaccess.thecvf.com/content_ICCV_2019/html/Howard_Searching_for_MobileNetV3_ICCV_2019_paper.html

[3] Y. A. Malkov and D. A. Yashunin, “Efficient and Robust Approximate Nearest Neighbor Search Using Hierarchical Navigable Small World Graphs,” *IEEE TPAMI*, vol. 42, no. 4, pp. 824-836, 2020. [Online]. Available: https://ieeexplore.ieee.org/document/8594636

[4] Google for Developers, “ML Kit: Object detection and tracking,” 2026. [Online]. Available: https://developers.google.com/ml-kit/vision/object-detection

[5] Google for Developers, “ML Kit: Text Recognition v2,” 2026. [Online]. Available: https://developers.google.com/ml-kit/vision/text-recognition/v2

[6] Android Developers, “CameraX architecture,” 2026. [Online]. Available: https://developer.android.com/media/camera/camerax/architecture

[7] Android Developers, “WorkManager API reference and guides,” 2026. [Online]. Available: https://developer.android.com/topic/libraries/architecture/workmanager

[8] Android Developers, “Guide to app architecture,” 2026. [Online]. Available: https://developer.android.com/topic/architecture

[9] Android Developers, “Jetpack Compose documentation,” 2026. [Online]. Available: https://developer.android.com/compose

[10] ObjectBox, “ObjectBox database for Android and vector search,” 2025. [Online]. Available: https://objectbox.io

[11] JetBrains, “Kotlin coroutines guide,” 2025. [Online]. Available: https://kotlinlang.org/docs/coroutines-overview.html

[12] Google AI for Developers, “Gemini API documentation,” 2026. [Online]. Available: https://ai.google.dev/gemini-api/docs

[13] Google Firebase, “Firebase AI Logic,” 2026. [Online]. Available: https://firebase.google.com/docs/ai-logic

[14] NIST, “Artificial Intelligence Risk Management Framework (AI RMF 1.0),” Jan. 2023. [Online]. Available: https://www.nist.gov/publications/artificial-intelligence-risk-management-framework-ai-rmf-10

[15] NIST, “AI Risk Management Framework: Generative AI Profile (NIST-AI-600-1),” Jul. 2024. [Online]. Available: https://www.nist.gov/itl/ai-risk-management-framework

[16] United Nations Environment Programme, *Food Waste Index Report 2024*, 2024. [Online]. Available: https://www.unep.org/resources/publication/food-waste-index-report-2024

[17] Food and Agriculture Organization, *The State of Food and Agriculture 2019: Moving Forward on Food Loss and Waste Reduction*, 2019. [Online]. Available: https://www.fao.org/agrifood-economics/publications/detail/en/c/1238574/

[18] IPCC, *Climate Change 2023: Synthesis Report*, 2023. [Online]. Available: https://www.ipcc.ch/report/ar6/syr/

[19] ISO, “ISO 22005:2007 Traceability in the feed and food chain — General principles and basic requirements for system design and implementation,” 2007. [Online]. Available: https://www.iso.org/standard/36297.html

[20] GS1, “GS1 Global Traceability Standard,” current edition. [Online]. Available: https://www.gs1.org/standards/gs1-global-traceability-standard/current-standard

[21] ISO/IEC, “ISO/IEC 27001:2022 Information security, cybersecurity and privacy protection — Information security management systems — Requirements,” 2022.

[22] Android Developers, “Testing in Android: unit, integration, and UI testing,” 2026. [Online]. Available: https://developer.android.com/training/testing

[23] Maestro Mobile, “Maestro: mobile UI testing framework,” 2026. [Online]. Available: https://maestro.mobile.dev/

[24] IEEE, “Ethically Aligned Design: A Vision for Prioritizing Human Well-being with Autonomous and Intelligent Systems,” 1st ed., 2019. [Online]. Available: https://standards.ieee.org/industry-connections/ec/autonomous-systems.html

## Appendix / Annexure Essentials (Draft)

### A. PO/PSO Mapping Summary (Sample Table)

| Report Component | PO Mapping (Indicative) | PSO Mapping (Indicative) | Evidence in Project |
|---|---|---|---|
| Problem formulation and objectives | PO1, PO2 | PSO1 | Requirement decomposition and measurable goals |
| System architecture and module design | PO3, PO5 | PSO1 | Layered Android design, modular comparison integration |
| AI/ML-assisted scan and retrieval pipeline | PO4 | PSO2 | Detection + extraction + similarity workflow |
| Testing and verification strategy | PO4, PO10 | PSO2 | Unit/integration/E2E validation planning |
| Societal and sustainability analysis | PO6, PO7 | PSO3 | Waste reduction and budget-aware usage narrative |
| Ethical and privacy considerations | PO8 | PSO3 | Local-first data handling and uncertainty disclosure |
| Documentation and future roadmap | PO11, PO12 | PSO1 | Structured limitations and enhancement mapping |

Note: Replace PO/PSO codes with institution-approved mappings before submission.

### B. Team Contribution Summary (Sample Table)

| Team Member | Primary Responsibility | Key Deliverables | Contribution Share |
|---|---|---|---|
| Member 1 (Lead) | Core architecture and integration | End-to-end flow integration, module coordination | 30% |
| Member 2 | AI/ML pipeline | Scan workflow, extraction logic, similarity handling | 25% |
| Member 3 | Inventory and reminder lifecycle | Quantity/expiry flows, notification behavior | 20% |
| Member 4 | Comparison and adapter reliability | Multi-source compare module, result formatting | 15% |
| Member 5 | Testing and documentation | Validation suites, report consolidation support | 10% |

Note: Replace placeholders with actual names, enrollment numbers, and approved percentages.

### C. Plagiarism Report Placeholder Note
A signed plagiarism/similarity report should be attached as an annexure in the department-mandated format. Replace this placeholder with the final similarity sheet and required supervisor authentication.
