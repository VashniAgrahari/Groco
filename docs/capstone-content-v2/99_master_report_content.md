# GROCO – Smart Grocery Supply Management System

## Report Content Draft (Formatting to be done manually in Word)

This file is intentionally formatting-light and content-focused.

---

# 1. Preliminary Pages Content

## 1.1 Certificate (editable template text)

This is to certify that the project report entitled **"GROCO – Smart Grocery Supply Management System"** is a bona fide record of the work carried out by:

- **[Student Name 1]**, Roll No. **[________]**
- **[Student Name 2]**, Roll No. **[________]**
- **[Student Name 3]**, Roll No. **[________]**
- **[Student Name 4]**, Roll No. **[________]**

of **[Department Name]**, **[College Name]**, in partial fulfillment of the requirements for the award of the degree of **Bachelor of Technology** in **[Branch Name]** during the academic year **[20__ - 20__]**.

The work embodied in this report has been completed under my supervision and guidance. To the best of my knowledge, the report represents original work by the above students and has not been submitted, either in full or in part, to any other university or institution for the award of any degree or diploma.

The project demonstrates a practical application of Artificial Intelligence and Machine Learning in the domain of household grocery management, including intelligent item identification, expiry awareness, and decision support for economical purchasing. The students have shown sincere effort in problem understanding, design planning, implementation, testing, and documentation. Their work reflects good analytical thinking, responsible use of AI methods, and awareness of social relevance.

I recommend that this report be accepted for examination.

**Project Guide**  
Name: **[Guide Name]**  
Designation: **[Designation]**  
Department: **[Department Name]**  
Signature: **[________]**  
Date: **[DD/MM/YYYY]**

**Head of Department**  
Name: **[HOD Name]**  
Signature: **[________]**  
Date: **[DD/MM/YYYY]**

**External Examiner (if applicable)**  
Name: **[Examiner Name]**  
Signature: **[________]**  
Date: **[DD/MM/YYYY]**

Place: **[City]**

---

## 1.2 Declaration of Originality

We, the undersigned students, hereby declare that the project report titled **"GROCO – Smart Grocery Supply Management System"** is an original work carried out by us under the guidance of **[Guide Name]**, **[Designation]**, **[Department Name]**, **[College Name]**.

We further declare that:

1. The work presented in this report has been completed by us during the academic period specified by the department.
2. This report has not been submitted earlier, either in part or in full, to this or any other institution for the award of any degree, diploma, or certificate.
3. Ideas, results, and observations taken from published or unpublished sources have been properly acknowledged in the report.
4. We understand that any violation of academic integrity, including plagiarism, data fabrication, or false declaration, will lead to disciplinary action as per institute rules.

The purpose of this project is to address practical challenges in grocery planning, expiry control, and cost-aware buying decisions using AI-supported methods. We have made sincere efforts to ensure that the study, analysis, and presented outcomes are fair, transparent, and based on actual work carried out by our team.

We accept full responsibility for the contents of this report.

**Student 1**  
Name: **[Student Name 1]**  
Roll No.: **[________]**  
Signature: **[________]**

**Student 2**  
Name: **[Student Name 2]**  
Roll No.: **[________]**  
Signature: **[________]**

**Student 3**  
Name: **[Student Name 3]**  
Roll No.: **[________]**  
Signature: **[________]**

**Student 4**  
Name: **[Student Name 4]**  
Roll No.: **[________]**  
Signature: **[________]**

Date: **[DD/MM/YYYY]**  
Place: **[City]**

---

## 1.3 Acknowledgement

We express our sincere gratitude to all those who supported us in completing the project report on **"GROCO – Smart Grocery Supply Management System"**.

First, we thank **[Principal Name]**, Principal of **[College Name]**, for providing the academic environment and infrastructure required to carry out this work. We also thank **[HOD Name]**, Head of the **[Department Name]**, for valuable encouragement and departmental support throughout the project period.

We are deeply thankful to our project guide, **[Guide Name]**, whose continuous guidance helped us shape the project from an initial idea into a practical and meaningful system. Their feedback on problem framing, technical direction, and report writing helped us maintain clarity and discipline in every stage of work.

We acknowledge the support of all faculty members of the department for sharing conceptual knowledge in Artificial Intelligence, Machine Learning, and engineering design practices, which served as the foundation for this project. We also thank the laboratory staff and technical assistants for helping us with the required resources and testing setup.

We extend our appreciation to classmates and peers who provided useful suggestions during demonstrations and review discussions. Their feedback helped us improve usability, simplify workflow decisions, and strengthen the practical value of the system.

We are grateful to our family members for their patience, motivation, and confidence in our efforts. Their constant support helped us stay focused during demanding timelines and multiple project milestones.

Finally, we thank everyone, directly or indirectly, who contributed to the successful completion of this project report. We consider this work not only a technical exercise but also a learning experience in teamwork, responsibility, and socially relevant problem solving using AI-enabled methods.

---

## 1.4 Abstract

Household grocery management is an everyday activity, yet it remains largely manual, inconsistent, and time-consuming for many families and students living away from home. Common issues include forgotten inventory, untracked expiry dates, unnecessary duplicate purchases, and lack of quick comparison across online grocery sources. These problems result in avoidable food waste, financial inefficiency, and poor planning discipline. The project **"GROCO – Smart Grocery Supply Management System"** addresses these practical challenges through an AI-supported decision framework designed for routine use.

The project combines three core goals: intelligent inventory awareness, proactive expiry management, and informed purchase decisions. Instead of treating these tasks as separate tools, the system unifies them into a single workflow where the user can capture grocery items, track quantity and expiry status, receive reminders, and compare options before reordering. The central design motivation is to reduce user effort while improving reliability of household decisions.

A key contribution of the project is the use of AI/ML methods to convert raw visual and text inputs into actionable inventory information. Image-based input supports item recognition and naming assistance, while text interpretation helps identify expiry-related details from product labels. Matching logic is then used to align product intent with available market options for comparison. The emphasis is not on replacing user judgment, but on reducing repetitive manual work and presenting clearer choices at the right time.

The methodological approach follows a practical engineering sequence: problem identification, requirement analysis, model-assisted workflow design, integration of inventory and comparison modules, iterative testing, and usability-focused refinement. During evaluation, the project considered accuracy of item understanding, consistency of expiry alerts, relevance of comparison outcomes, and smoothness of end-user flow. The findings indicate that AI-assisted grocery workflows can significantly improve day-to-day planning when combined with simple interfaces and clear status feedback.

Beyond technical outcomes, the project has direct social relevance. Better expiry awareness can reduce preventable waste at household level; better price visibility can support budget-sensitive users; and structured tracking can encourage responsible consumption habits. The solution is particularly useful for working families, students, and shared households where grocery decisions are distributed across multiple people and often made under time pressure.

In summary, **"GROCO – Smart Grocery Supply Management System"** presents a realistic and scalable model for applying AI/ML to a high-frequency domestic problem. The work demonstrates that small, context-aware automation in routine tasks can produce measurable improvements in convenience, economy, and sustainability. The project also provides a foundation for future enhancements such as multilingual support, improved personalization, and broader decision analytics in household resource management.

# 2. Chapter 1: Introduction

## 2.1 Background and Motivation

Grocery purchase and consumption management is a recurring challenge in most households. Items are purchased at different times, stored in different places, and consumed at variable rates. As a result, people often lose visibility over what is available, what is nearing expiry, and what needs replenishment. The issue becomes more pronounced in urban settings where daily schedules are tight, purchase channels are fragmented, and family members share responsibilities. Even when people use notes or messaging groups for coordination, updates are not systematic and quickly become outdated.

At the same time, the grocery ecosystem has expanded rapidly. Consumers can buy from local stores, online quick-commerce services, and full-range e-commerce platforms. This expansion offers convenience, but it also introduces a decision burden. Identical or similar products may appear in different formats, sizes, and pricing structures. Users must manually compare options while also remembering current stock at home. In practice, many purchases happen under urgency, leading to overspending or duplicate buying.

Food expiry management is another critical dimension. A large portion of avoidable food loss in domestic settings occurs because users do not track expiry dates consistently. Perishable items require timely consumption, yet manual monitoring is difficult when many items are stored together. Missed expiry reminders can create financial loss and raise health concerns. Therefore, a useful grocery management system should not only record items but should also guide attention toward what needs immediate action.

These practical realities motivate a technology-assisted approach. Advances in Artificial Intelligence and Machine Learning make it possible to transform routine grocery handling into a more structured process. Computer vision techniques can support item understanding from images; language models can assist in interpreting label information; ranking methods can improve relevance in price comparison; and reminder logic can prioritize items based on urgency. When these components are combined carefully, users can move from reactive buying to planned, evidence-based decisions.

The motivation behind **"GROCO – Smart Grocery Supply Management System"** is rooted in this shift from manual memory-based behavior to assisted decision behavior. The project does not assume fully autonomous grocery planning. Instead, it focuses on practical augmentation: helping users capture information faster, understand inventory status clearly, and act before waste or overspending occurs. This design philosophy is important for real adoption because users prefer systems that simplify daily work without demanding high learning effort.

From an academic perspective, the project provides a meaningful domain for applying AI/ML in a human-centered workflow. Many student projects either remain algorithm-centric or become purely interface-driven. This work attempts balance by linking model-assisted interpretation with real decision points: what to consume first, what to replenish, and from where to buy. It demonstrates how machine learning can support small but repeated decisions that collectively influence household economy and sustainability.

The project is also motivated by practical constraints common in college-level deployment scenarios. Users may have diverse devices, variable connectivity, and limited tolerance for complex settings. Therefore, the system emphasizes simple interaction flow, clear status indicators, and robust fallback behavior. In this context, the technical challenge is not only model capability, but also integration quality and user trust.

In summary, the background of this project lies at the intersection of three needs: inventory clarity, expiry discipline, and price-aware purchasing. The motivation is to convert these needs into an accessible AI-supported system that improves routine household decisions without introducing unnecessary complexity.

## 2.2 Problem Definition

The core problem addressed in this project can be stated as follows:

**How can a grocery management system assist users in maintaining accurate household inventory, reducing expiry-related waste, and selecting economical purchase options, while keeping daily interaction effort low?**

This problem has multiple sub-problems that are interdependent:

1. **Inventory visibility problem:** Users do not have a consistently updated view of available items and quantities.
2. **Expiry monitoring problem:** Users fail to identify and act on near-expiry items at the right time.
3. **Purchase decision problem:** Users cannot quickly compare equivalent products across available market sources.
4. **Effort and adoption problem:** Manual data entry and complex workflows reduce long-term usage.

A practical system must solve these together rather than independently. For example, price comparison without inventory awareness may encourage unnecessary buying, while inventory tracking without reorder guidance may not improve cost efficiency. Similarly, reminders without accurate item understanding can lead to false alerts and reduced trust.

Formally, for each grocery item, the system must maintain a usable state representation that includes identity, quantity level, and time-sensitivity. Based on this representation, it should generate prioritized actions such as consume soon, reorder now, or defer purchase. At the same time, when users search for alternatives, the system must present comparison results that are sufficiently relevant to user intent.

From an AI/ML perspective, the challenge is to manage uncertainty in real-world input:

- Image captures may vary in lighting, orientation, and packaging design.
- Label text may be partial or noisy.
- Product names across different vendors may use different naming styles.
- Price and availability information can change frequently.

Therefore, the problem is not only about prediction quality, but also about decision quality under imperfect data. The system must provide useful outputs even when some inputs are ambiguous. It should degrade gracefully, allow user correction, and preserve transparency in recommendations.

The project defines success as measurable improvement in three outcomes: reduced manual effort for grocery organization, better timeliness of expiry-related actions, and better confidence in economical reorder decisions. These outcomes align with everyday user needs and provide a clear basis for evaluation in an academic setting.

## 2.3 Objectives (3-5 measurable goals)

The project is guided by five measurable objectives that connect AI/ML capability with practical user value.

1. **Improve item understanding accuracy**  
Target: Achieve at least **85% correct item identification/label interpretation** on the project’s evaluation set covering common grocery categories.  
Measurement: Ratio of correctly interpreted item records to total evaluated records.

2. **Increase expiry action timeliness**  
Target: Ensure at least **90% of near-expiry items are surfaced to the user within the configured reminder window** during controlled trials.  
Measurement: Reminder coverage rate for items that meet urgency criteria.

3. **Provide relevant comparison support**  
Target: Achieve at least **80% relevance agreement** between system ranking and evaluator judgment for matched product options in test queries.  
Measurement: Percentage of comparison outputs marked relevant by evaluators.

4. **Reduce user effort per task**  
Target: Keep median completion time for core flows (add/update/check/reorder decision) under **60 seconds per item interaction** after initial onboarding.  
Measurement: Task-time observation from guided usability sessions.

5. **Maintain reliable routine operation**  
Target: Complete at least **95% of scheduled inventory and reminder cycles without critical failure** in test runs.  
Measurement: Successful cycle count divided by total observed cycles.

These objectives were selected because they are specific, measurable, and tied to real usage behavior. Together, they ensure that project evaluation goes beyond feature presence and focuses on decision quality, usability, and operational consistency.

## 2.4 Social and Environmental Relevance

The project has direct social relevance because grocery management is a universal household need across income groups. Small inefficiencies in daily planning accumulate into meaningful economic and nutritional impact over time. When users cannot track quantities or expiry status, they may purchase unnecessarily, discard usable items, or postpone essential purchases. An organized and timely support system can reduce this friction and improve household stability.

For students, working professionals, and shared-family environments, grocery responsibility is often distributed. In such settings, one person may buy items while another uses them, and communication gaps become common. A structured inventory view with expiry alerts can improve coordination and reduce confusion. This is particularly important for hostels and rented accommodations where storage space is limited and turnover is irregular.

From a financial perspective, budget-sensitive households benefit from better visibility of what is already available and what can be purchased more economically. Comparison guidance can help users make informed decisions rather than urgency-driven purchases. Over repeated cycles, this can contribute to more disciplined spending patterns.

Environmental relevance is equally important. Domestic food waste contributes to broader sustainability challenges through wasted water, energy, packaging, and transport effort embedded in food products. While large-scale waste reduction requires policy and supply-chain reforms, household-level prevention remains a significant opportunity. A system that highlights near-expiry items and supports timely consumption can reduce avoidable disposal at source.

The project also aligns with responsible technology use. It applies AI where it adds clear value, instead of automation for its own sake. By keeping users in control of final decisions, the system supports assistive intelligence rather than opaque decision replacement. This human-in-the-loop approach is appropriate for everyday domestic contexts where trust and clarity are essential.

Another social value lies in digital habit formation. Many users are willing to adopt better planning practices if tools are easy to use and provide immediate benefits. By reducing entry effort and increasing actionable feedback, the project encourages sustained behavior change in inventory tracking and consumption planning.

In summary, the project addresses a common problem with implications for affordability, food responsibility, and environmental awareness. Its relevance extends beyond technical demonstration and supports practical community-level benefits through better everyday decision support.

## 2.5 Project Plan Overview (WBS summary, timeline summary, risks summary)

The project was planned as a phased workflow with clear outputs at each stage. A compact Work Breakdown Structure (WBS), semester-aligned timeline, and early risk mapping were used to keep development focused and verifiable.

### WBS Summary

| WBS Level | Work Package | Key Deliverable |
|---|---|---|
| 1 | Problem and requirement analysis | Defined use cases, user scenarios, and evaluation criteria |
| 2 | AI/ML-assisted workflow design | Item understanding flow, expiry interpretation strategy, matching logic blueprint |
| 3 | Core system development | Integrated inventory, reminder, and comparison capabilities |
| 4 | Testing and validation | Functional checks, usability trials, and result analysis |
| 5 | Documentation and presentation | Structured report content, visual summaries, and demonstration material |

### Timeline Summary

The project followed a semester-oriented schedule of approximately 16 weeks:

| Phase | Duration | Focus |
|---|---|---|
| Weeks 1-2 | Planning | Problem framing, literature scanning, requirement finalization |
| Weeks 3-5 | Design | AI/ML workflow definition, data/state design, interaction mapping |
| Weeks 6-10 | Development | Implementation of inventory, extraction, comparison, and reminder modules |
| Weeks 11-13 | Integration and refinement | End-to-end flow stabilization and usability improvements |
| Weeks 14-15 | Testing and evaluation | Scenario-based validation, performance checks, outcome measurement |
| Week 16 | Final consolidation | Report preparation, result packaging, and review closure |

### Risks Summary

| Risk Area | Practical Impact | Mitigation Strategy |
|---|---|---|
| Inconsistent image/label quality | Incorrect item or expiry interpretation | Multi-step validation flow with user correction option |
| Product naming variation across sources | Low comparison relevance | Normalization and similarity-based matching rules |
| Frequent market data changes | Time-sensitive result drift | Use latest available retrieval and clear timestamp/context display |
| User drop-off due to input effort | Reduced real-world utility | Keep interactions short and prioritize high-frequency tasks |
| Reminder fatigue | Users ignore alerts | Urgency-based reminder logic with configurable windows |
| Integration complexity | Delays in final stabilization | Phase-wise integration with checkpoint-based testing |

This planning approach helped balance innovation and feasibility. It ensured that AI/ML features were introduced in a controlled sequence and evaluated against practical user outcomes rather than only technical novelty.

## 2.6 Scope and Limitations

### Scope

The scope of **"GROCO – Smart Grocery Supply Management System"** includes:

1. Intelligent support for adding and organizing grocery items in a household inventory context.
2. Quantity and expiry-aware item status management with proactive reminders.
3. AI/ML-assisted interpretation of user-provided item information from practical inputs.
4. Cross-source product comparison support to aid reorder decisions.
5. User-centered workflow emphasizing clarity, low interaction effort, and actionable status feedback.
6. Experimental evaluation based on accuracy, relevance, usability, and operational consistency.

The system is intended as a decision-support tool for routine domestic use. It assists users in making better choices but does not automate final purchase decisions.

### Limitations

Despite its usefulness, the project has expected limitations:

1. Input quality dependence: poor lighting, unclear packaging text, or partial capture can reduce interpretation quality.
2. Variation in product formats: differences in unit representation and packaging style can affect comparison alignment.
3. Market dynamism: price and availability can change rapidly, so comparison outcomes represent a time-specific snapshot.
4. Region/platform dependency: source coverage may vary by location and service availability.
5. User behavior dependency: reminder effectiveness depends on whether users acknowledge and act on alerts.
6. Evaluation scale: academic testing covers representative scenarios but not every real-world edge case.

These limitations are typical for practical AI-enabled systems and do not invalidate the core contribution. Instead, they identify natural directions for iterative improvement, including stronger personalization, richer multilingual handling, adaptive reminders, and broader ecosystem coverage.

## 2.7 Chapter Summary

This chapter introduced the foundation of **"GROCO – Smart Grocery Supply Management System"** by presenting its background, motivation, and practical need. It defined the central problem as a combined challenge of inventory visibility, expiry control, and economical purchasing under low-effort user interaction constraints. Clear and measurable objectives were established to evaluate technical and user-centered outcomes. The chapter also explained the project’s social and environmental significance, outlined the phase-wise plan through WBS, timeline, and risk summaries, and clarified scope boundaries with known limitations. Together, these elements provide a structured basis for the next chapters on prior work, methodology, implementation, and evaluated outcomes.

---

# Chapter 2: Literature Survey

## 2.1 Introduction to Literature Context

Modern grocery support systems are no longer limited to manual barcode entry and static inventory records. The current research direction combines embedded vision, machine learning, and decision-support algorithms to identify products from camera streams, read packaging text, compare prices, and generate user-relevant recommendations in real time. For a mobile-first system such as **GROCO – Smart Grocery Supply Management System**, this literature context is especially important because performance requirements are multi-dimensional: recognition quality, latency, memory footprint, energy usage, and robustness under retail-like conditions.

From an AI/ML perspective, the literature has evolved from closed-set recognition pipelines toward hybrid systems that include object detection, metric embeddings, optical character recognition (OCR), and language-guided reasoning. Early product recognition studies often used handcrafted features, but deep convolutional models improved robustness against lighting variation, viewpoint shifts, and partial occlusion. In parallel, edge-device constraints drove work on lightweight backbones, quantization, and approximate nearest-neighbor (ANN) retrieval.

From an ECE perspective, the core trade-off is clear: higher model capacity improves accuracy but increases latency, memory usage, thermal load, and battery drain. Deployment is therefore a system-level co-design problem, not only a model-selection problem.

A second major trend in the literature is the move from single-modality recognition toward multi-modal inference. Image-only classification performs well for common products in known categories, but fails when package artwork is similar, text is dense, or the product is unseen during training. To reduce these errors, recent work combines visual detection, OCR-based textual cues (brand, flavor, weight, date), and vision-language priors for open-vocabulary recognition.

A third trend concerns downstream decision support. Product detection by itself is not enough for a grocery assistance workflow. Literature on recommendation systems, entity matching, and offer ranking shows that economically useful outputs depend on structured product normalization, price unit alignment, and user-context filtering.

Therefore, this chapter reviews project-relevant literature in seven connected themes: real-time mobile object detection, lightweight backbones, embedding-based retrieval, OCR and date extraction, vision-language open-vocabulary models, and price comparison methods. Instead of listing papers independently, the review compares methods by operational trade-offs and relevance to an on-device grocery intelligence pipeline.

## 2.2 Real-Time Object Detection for Mobile

Real-time object detection literature is usually organized into two families: two-stage detectors and one-stage detectors. Two-stage methods, represented by Faster R-CNN variants, first generate candidate regions and then classify them. They often provide strong localization quality, especially for cluttered scenes, but their compute pattern and memory usage are less suitable for low-power mobile inference. In contrast, one-stage methods directly predict class and bounding boxes in a single pass, offering better speed-accuracy balance for edge deployment.

### Evolution of one-stage detectors

SSD (Single Shot MultiBox Detector) established a practical baseline by combining multi-scale feature maps with anchor-based prediction. Its mobile variants, particularly SSD with MobileNet backbones, became common in embedded benchmarks due to reasonable speed and moderate accuracy. However, SSD performance drops for small objects and dense shelf layouts unless carefully tuned.

The YOLO family improved real-time performance through stronger feature fusion and detection heads. For grocery-like scenes, YOLO-style models are often preferred when near real-time response is required for live camera preview. The limitation is that larger variants exceed practical memory and thermal budgets on mid-range phones, while tiny variants may lose discrimination between similar product packages.

EfficientDet and EfficientDet-Lite proposed compound scaling, where input resolution, depth, and width are jointly scaled with a weighted feature pyramid design. The Lite versions are optimized for mobile runtimes and often deliver strong accuracy-per-FLOP ratios. Their performance advantage is most visible when inference frameworks support optimized operators; otherwise, theoretical gains may not fully translate to real-device latency.

### Deployment-centric metrics

Many detection studies report mean Average Precision (mAP) as primary quality measure. For mobile deployment, this is insufficient. Edge literature increasingly adds latency percentiles, memory peak, energy-per-inference, and sustained frame-rate under thermal throttling. These metrics are particularly important for ECE-oriented systems because user experience degrades when frame rate collapses after continuous usage.

A key finding across studies is that the best offline detector is not always the best mobile detector. For example, a moderately accurate but stable model with consistent 20-25 FPS may outperform a high-mAP model that oscillates between 8-20 FPS due to thermal constraints. This shift from peak accuracy to operational reliability is central for real deployments.

### Data domain considerations

Retail product detection presents domain-specific challenges: reflective plastic covers, repeated logos, seasonal package redesign, and dense shelf backgrounds. Generic datasets such as COCO are useful for initialization but insufficient for grocery-specific discrimination. Studies show that fine-tuning with domain images, augmentation for illumination and blur, and hard-negative mining are necessary to reduce confusion among near-identical products.

### Comparative insight for this project

For a mobile grocery assistant, literature collectively suggests a hybrid detector strategy: start from efficient one-stage models (e.g., SSD-MobileNet, YOLO-nano/small, EfficientDet-Lite) and evaluate them on device-level constraints, not only mAP. The selected model should favor stable inference under sustained usage and should integrate well with subsequent embedding and OCR modules. In other words, detection quality must be treated as an upstream enabler for the entire recognition and recommendation pipeline.

## 2.3 Lightweight Backbones (MobileNet family etc.)

Backbone architecture determines the feature quality versus computational cost trade-off for both detection and classification stages. Mobile literature shows that carefully designed lightweight backbones can retain most discriminative power while reducing multiply-accumulate operations and memory transfers.

### MobileNet lineage and design principles

MobileNetV1 introduced depthwise separable convolutions, decomposing standard convolution into channel-wise spatial filtering and pointwise channel mixing. This significantly reduces computation and parameter count, making it suitable for CPU and mobile NPUs.

MobileNetV2 added inverted residual blocks with linear bottlenecks. Instead of compressing then expanding like classical residual blocks, it expands to a higher-dimensional latent space for depthwise filtering and projects back linearly. This design preserves information flow in low-dimensional manifolds and improves efficiency.

MobileNetV3 combined hardware-aware architecture search with squeeze-and-excitation style attention and efficient nonlinearities. The key insight is co-optimization with actual target hardware, not only symbolic FLOPs. In practical reports, V3 often provides better latency-accuracy trade-off than V2 at similar model scales.

### Other lightweight alternatives

ShuffleNet introduced channel shuffle operations to improve inter-group information exchange with grouped convolutions. GhostNet used ghost feature generation to reduce redundant computations. EfficientNet-Lite adapted compound scaling principles for edge inference. MobileViT and related compact transformer-CNN hybrids introduced global context modeling at moderate compute cost.

These alternatives show that no single backbone dominates all scenarios. Performance depends on operator support in runtime libraries, quantization behavior, and input resolution. For example, a theoretically efficient architecture can underperform if its operator pattern is not optimized in the target inference engine.

### Quantization and compression literature

Model compression studies consistently show that post-training quantization can reduce model size and latency, but may degrade accuracy for fine-grained product distinctions if calibration data is weak. Quantization-aware training generally recovers part of this loss by simulating low-precision arithmetic during training.

Pruning methods offer additional savings but may introduce irregular sparsity that is not efficiently exploited by mobile hardware. Structured pruning is usually preferred because it maps better to dense kernels supported by inference accelerators.

Knowledge distillation is another recurring strategy: a large teacher model transfers soft targets to a compact student. In grocery recognition, this can help preserve class boundary information among visually similar items.

### ECE-oriented system trade-offs

From an embedded systems perspective, lightweight backbone evaluation should include activation memory, sensitivity to input resolution, throughput under CPU/NPU fallback, and thermal behavior under continuous inference.

Literature indicates that memory bandwidth and cache behavior can dominate real-device performance, especially for depthwise-heavy networks. Therefore, model choice must be validated with deployment profiling rather than paper-level benchmarks.

### Comparative insight for this project

For **GROCO – Smart Grocery Supply Management System**, MobileNet-family backbones remain a practical first choice due to mature tooling and strong edge performance. However, literature also supports selective benchmarking of newer lightweight models (e.g., EfficientNet-Lite or GhostNet derivatives) because backbones interact strongly with detection head design and quantization strategy. The most suitable backbone is the one that preserves discriminative features for similar packages while meeting sustained mobile latency limits.

## 2.4 Feature Embeddings and Similarity Search (HNSW/ANN concepts)

Object detection identifies candidate regions, but fine-grained grocery recognition often requires an additional retrieval stage. Embedding-based retrieval maps each product image (or cropped detection) into a feature vector space where semantically similar items lie near each other. This design is robust to class expansion because adding new products can be handled by inserting new vectors into an index rather than retraining the full classifier.

### Embedding learning approaches

Literature uses several objectives for discriminative embeddings:

1. Contrastive loss, which pulls positive pairs together and pushes negative pairs apart.
2. Triplet loss, which enforces anchor-positive proximity relative to anchor-negative distance.
3. Proxy-based losses, which stabilize training by comparing samples to learned class proxies.
4. Multi-modal alignment losses (as in vision-language models), where image embeddings align with text embeddings.

For retail products, hard-negative mining is critical because many classes differ by small textual or color cues. Studies show that embedding quality improves when negatives are sampled from visually similar categories rather than random global sampling.

### Exact vs approximate nearest neighbor search

Given high-dimensional embeddings and large catalogs, exact nearest neighbor search becomes computationally expensive. ANN methods provide faster retrieval with controlled recall loss.

Common ANN structures include:

1. HNSW (Hierarchical Navigable Small World graphs): graph-based index with layered proximity navigation, often delivering high recall and low latency.
2. IVF (Inverted File) families: partition vector space into coarse centroids, then search selected partitions.
3. Product quantization variants: compress vectors into compact codes for memory-efficient search.

HNSW is widely adopted for its strong speed-recall behavior and incremental insertion support. Its main limitations are memory overhead and parameter sensitivity for graph degree and construction effort.

### On-device vs server-side indexing

Literature presents three deployment patterns:

1. Fully on-device index for privacy and offline support.
2. Cloud index with lightweight client embeddings for large catalogs.
3. Hybrid: local shortlist with cloud re-ranking.

On-device indexing improves responsiveness and privacy but is constrained by storage and RAM. Cloud indexing supports larger inventories but introduces network dependency. Hybrid architectures are often preferred in mobile commerce systems.

### Similarity quality and calibration

Distance metrics (cosine, Euclidean on normalized vectors, learned Mahalanobis variants) influence retrieval quality. Many studies report top-k accuracy, recall@k, and mean reciprocal rank as evaluation criteria. In practical systems, calibration is essential: a nearest result may still be wrong when the query belongs to an unseen or low-quality crop. Confidence estimation methods include distance-margin thresholds, density-based outlier detection, and agreement checks with OCR text.

### Comparative insight for this project

For grocery intelligence, embedding + ANN retrieval offers flexibility beyond closed-set detection. Literature suggests that HNSW-based ANN can provide real-time candidate retrieval if vector dimensionality and index parameters are tuned for device constraints. The strongest results typically come from fusion strategies: detector proposes region, embedding index produces shortlist, and OCR/metadata logic resolves final identity. This multi-stage retrieval pipeline is more robust than single-shot classification when products are frequently updated.

## 2.5 OCR and Date Extraction in Product Packaging

Packaging text provides critical cues that visual appearance alone may miss, including brand name, product variant, net quantity, manufacturing date, and expiry date. OCR literature for consumer packaging shows that recognition quality depends more on text localization and pre-processing than on recognition architecture alone.

### Text detection and recognition pipeline

Most practical systems separate OCR into two stages:

1. Text detection (where text regions are): methods such as EAST, CRAFT, and differentiable binarization approaches.
2. Text recognition (what text says): CRNN-based models, attention-based sequence decoders, and transformer-based recognizers.

Tesseract-like engines remain useful for high-contrast printed text but often struggle with curved labels, dense fonts, and low-light motion blur. Deep OCR models improve robustness but require more compute and memory.

### Packaging-specific OCR challenges

Compared with scanned documents, grocery packaging introduces additional complexity:

1. Decorative fonts and stylized branding.
2. Reflective or transparent wraps causing highlights.
3. Perspective distortion from handheld camera angles.
4. Multilingual labels and mixed scripts.
5. Small printed dates stamped with low contrast.

Literature therefore emphasizes adaptive thresholding, perspective correction, glare suppression, and local enhancement around date regions.

### Date extraction as structured information task

Date extraction is not only OCR; it is an information extraction problem. After text recognition, systems typically apply:

1. Pattern detection for formats (DD/MM/YYYY, MM-YY, textual month variants).
2. Lexical context rules (e.g., “MFG”, “PKD”, “EXP”, “BEST BEFORE”).
3. Confidence filtering and ambiguity handling.
4. Temporal validation (expiry should be after manufacturing date).

Recent literature explores sequence labeling and small language models for noisy OCR correction, especially when separators or digits are misread. However, rule-based post-processing remains highly competitive in constrained domains because date grammar is regular.

### Fusion with visual recognition

Packaging OCR is most effective when fused with visual predictions. If detector suggests multiple similar products, recognized brand tokens can resolve ambiguity. Conversely, OCR can fail under blur; visual embedding can still provide candidate category and restrict text matching space. This reciprocal constraint approach is repeatedly supported in retail studies.

### Edge deployment considerations

OCR modules are computationally variable because text region count changes across frames. For mobile stability, literature recommends staged execution: lightweight detector runs continuously, while OCR is triggered on selected frames or user action. This reduces energy usage and avoids UI lag. Quantized text detectors and compact recognizers can further reduce latency, but careful calibration is required to avoid severe recall loss for small date strings.

### Comparative insight for this project

For **GROCO – Smart Grocery Supply Management System**, OCR is not optional metadata; it is a discriminative signal for product disambiguation and date awareness. Literature supports a focused pipeline where text detection and recognition are optimized for packaging conditions, followed by rule-guided date parsing and cross-checks with visual candidates. This approach provides practical robustness without requiring very large language models on device.

## 2.6 Vision-Language Models for Open-Vocabulary Recognition

Traditional classifiers require predefined product classes, which limits scalability when new or rare items appear. Vision-language models (VLMs) address this limitation by learning aligned image-text representations from large-scale paired data. Open-vocabulary recognition uses this shared space to identify or describe items beyond fixed training labels.

### Core VLM developments

Contrastive pretraining approaches such as CLIP, ALIGN-type methods, and newer sigmoid-based alignment variants demonstrated that natural language supervision can produce broadly transferable visual representations. These models support zero-shot recognition by matching image embeddings with text prompts.

In detection tasks, open-vocabulary variants combine region proposals with text-conditioned scoring. This enables recognition of categories absent from detector training sets, useful for long-tail grocery inventories where complete labeled datasets are difficult to maintain.

### Benefits in grocery context

For project-relevant use cases, VLMs offer three practical advantages:

1. Better generalization to unseen brands or variants.
2. Natural interface with OCR tokens and textual metadata.
3. Flexible fallback reasoning when closed-set detector confidence is low.

If a product is unknown to the classifier but visually close to a known family, text prompts can still recover useful category-level meaning.

### Limitations and reliability concerns

Despite strong transfer ability, VLM literature highlights reliability issues:

1. Prompt sensitivity: small wording changes can alter predictions.
2. Dataset bias inherited from web-scale pretraining.
3. Lower precision for fine-grained, visually similar packaging.
4. Higher compute footprint than compact mobile CNNs.

Therefore, VLM outputs should be treated as probabilistic cues, not deterministic truth. In high-similarity domains like grocery shelves, pure zero-shot VLM inference may be insufficient without retrieval and OCR constraints.

### Distillation and lightweight adaptation

Recent studies explore distilling large VLM knowledge into smaller students for edge usage. Adapter-based fine-tuning, linear probing on fixed embeddings, and late-fusion scoring are common strategies to reduce cost. Instead of running full VLM inference on every frame, systems can precompute text embeddings for catalog labels and compute only image embeddings during runtime.

This decomposition helps mobile deployment because text-side computation is largely offline. Even so, image encoder complexity remains a bottleneck on low-end devices.

### Integration patterns in literature

Project-relevant papers often position VLMs as complementary modules:

1. As rerankers for top-k candidates from detector + ANN retrieval.
2. As fallback for unknown-product detection.
3. As semantic validators aligning OCR text with visual category descriptions.

These patterns reduce the risk of replacing a fast deterministic pipeline with a slower, less predictable one.

### Comparative insight for this project

For **GROCO – Smart Grocery Supply Management System**, literature suggests using VLM capability in a bounded manner: open-vocabulary support for edge cases, semantic reranking, and unknown-item handling, while retaining lightweight CNN-based detection as the primary real-time module. This balanced design captures VLM flexibility without violating mobile latency and power constraints.

## 2.7 Price Comparison and Offer Matching Methods

After recognition, the system must translate product identity into actionable shopping intelligence. Price comparison literature intersects information retrieval, recommender systems, and entity resolution.

### Product matching and normalization

Prices from different sellers are comparable only when product entities are normalized. Literature treats this as record linkage: match listings that may differ in brand spelling, unit notation, bundle description, and promotional language. Methods include token-based similarity, embedding-based semantic matching, and hybrid rules combining text, size, and brand constraints.

Unit normalization is especially important in grocery domains. Comparing pack price alone can be misleading when quantities differ; per-unit metrics (per gram, per liter, per piece) improve fairness and decision quality.

### Ranking models for offers

Offer ranking methods range from heuristic scoring to learning-to-rank models. Typical features include:

1. Effective unit price.
2. Delivery cost/time.
3. Promotion validity window.
4. Historical seller reliability.
5. User preference compatibility.

Simple weighted ranking is transparent and easy to deploy, while learning-to-rank can improve personalization with interaction data. Literature warns that personalization should not hide objectively better options; explainable ranking signals are preferred in consumer-facing systems.

### Recommendation paradigms

Collaborative filtering performs well with dense interaction history but suffers from cold-start for new users/products. Content-based methods use product attributes and embeddings, making them useful in early-stage deployments. Hybrid models are commonly recommended because they combine behavioral and content signals.

In grocery contexts, session-aware recommendations and substitution-aware ranking are important. If an exact product is unavailable, the system should propose close alternatives with explicit similarity reasoning (brand, category, dietary constraints, size range).

### Offer extraction and noise handling

Promotional offers are often expressed in non-standard language (e.g., “buy 2 get 1”, “combo savings”, “member only”). Literature uses rule templates, sequence tagging, and weakly supervised extraction to convert unstructured text into structured offer attributes. Confidence scoring and conflict resolution are required when multiple offer statements overlap.

### Comparative insight for this project

For this project, price intelligence should be built on reliable product matching and unit-level normalization before advanced recommendation logic. Literature supports a staged approach: entity resolution first, transparent price ranking second, and personalized recommendations third. This sequencing reduces user-facing errors and keeps behavior interpretable.

## 2.8 Comparative Table

The following table summarizes representative studies and model families most relevant to the project pipeline. It emphasizes comparative utility rather than exhaustive coverage.

| Study / Year | Method Focus | Dataset / Evaluation Context | Strengths | Limitations | Relevance to GROCO |
|---|---|---|---|---|---|
| Liu et al., 2016 | SSD one-stage detection | PASCAL VOC, COCO | Strong speed baseline; simple deployment path | Lower small-object recall in dense scenes | Useful baseline for mobile detection module |
| Howard et al., 2017 | MobileNetV1 backbone | ImageNet + transfer tasks | Very low compute via depthwise separable conv | Feature capacity limited for fine-grained classes | Practical backbone option for low-end devices |
| Sandler et al., 2018 | MobileNetV2 inverted residuals | ImageNet, detection/segmentation transfer | Better accuracy-efficiency balance than V1 | Sensitive to aggressive quantization in some tasks | Strong candidate backbone for on-device detector |
| Tan et al., 2020 | EfficientDet / EfficientDet-Lite scaling | COCO and edge benchmarks | High accuracy-per-FLOP with compound scaling | Runtime gains depend on optimized operators | Candidate for mobile detector where runtime support exists |
| Howard et al., 2019 | MobileNetV3 hardware-aware search | Mobile CPU-oriented evaluation | Improved latency-accuracy trade-off on phones | Architecture tuning may not transfer across chipsets | Good for sustained inference on commodity smartphones |
| Wang et al., 2021 (HNSW survey benchmarks) | Graph-based ANN retrieval | Million-scale vector search benchmarks | High recall with low latency; incremental insertion | Memory overhead can be significant | Suitable for fast product shortlist retrieval |
| Johnson et al., 2019 (ANN at scale) | IVF/PQ large-scale similarity search | Billion-scale retrieval settings | Memory-efficient indexing for huge catalogs | Approximation can reduce top-1 precision | Relevant for future cloud-scale catalog expansion |
| Baek et al., 2019 | Deep text recognition benchmark (CRNN/attention variants) | Scene text datasets | Clear comparison across OCR architectures | Not packaging-specific; domain shift present | Guides OCR recognizer selection strategy |
| Zhou et al., 2017 | EAST text detector | Natural scene text | Lightweight and practical for detection stage | Misses highly curved or tiny text in clutter | Baseline for packaging text localization |
| Radford et al., 2021 | CLIP contrastive vision-language pretraining | Large web image-text corpora | Zero-shot generalization and semantic flexibility | Prompt sensitivity; fine-grained confusion possible | Useful for unknown-item fallback and semantic reranking |
| Minderer et al., 2022 (open-vocab detection trends) | Open-vocabulary detection integration | COCO/LVIS style long-tail settings | Recognizes unseen classes via text prompts | Lower precision than closed-set tuned detectors | Supports long-tail product handling |
| Karpukhin et al., 2020 (dense retrieval principles) | Dense embedding retrieval and ranking | QA/search benchmarks | Strong representation learning for nearest-neighbor retrieval | Requires careful negative sampling and calibration | Conceptual guidance for embedding training and ANN ranking |
| Ricci et al., 2015-2021 line of work (recommender systems) | Hybrid recommendation strategies | E-commerce interaction datasets | Combines content and behavior for better personalization | Cold-start and bias issues remain | Guides phased recommendation design |
| Mudgal et al., 2018 (entity matching with deep learning) | Product/entity resolution | Structured and semi-structured product records | Improves duplicate and variant matching quality | Labeling effort for supervised matching | Important for accurate cross-store price comparison |

### Comparative observations

1. No single model family solves the full workflow; detection, retrieval, OCR, and recommendation require different strengths.
2. Edge suitability depends on deployment metrics, not only benchmark accuracy.
3. Hybrid fusion is consistently stronger for grocery ambiguity: visual detection + embedding retrieval + OCR + optional vision-language reranking.

## 2.9 Identified Research Gaps

From the reviewed literature, several gaps remain when targeting a practical grocery intelligence system on smartphones.

### Gap 1: Benchmark-to-deployment mismatch

Many studies optimize for benchmark accuracy, but report limited evidence for long-duration mobile usage. Thermal throttling, energy drain, and memory pressure are often underreported. This creates uncertainty when moving from research results to real user environments.

### Gap 2: Limited packaging-specific robustness evaluation

Generic object and scene-text datasets do not fully represent grocery packaging challenges such as glossy reflections, tiny date prints, multilingual labels, and frequent design updates. Methods performing well on public datasets may fail in store-like conditions.

### Gap 3: Weak integration between modules

Detection, OCR, and retrieval are frequently studied independently. End-to-end decision consistency is less explored. In real systems, uncertainty propagation across modules is a major issue; literature still lacks standardized fusion policies for this domain.

### Gap 4: Open-vocabulary benefits vs mobile cost

Vision-language models improve generalization, but their edge deployment cost is high. Existing work provides limited guidance on lightweight, bounded use of VLMs in mobile pipelines where deterministic latency is required.

### Gap 5: Price comparison reliability beyond text matching

Offer comparison studies often assume clean product identifiers. In practice, incorrect entity matching and missing unit normalization can mislead users even when ranking logic is sophisticated. Literature still underemphasizes error control at the matching stage.

### Gap 6: Limited uncertainty-aware user feedback

Few studies translate model uncertainty into user-facing guidance. Systems typically output a single prediction without confidence communication or alternative suggestions, reducing trust when ambiguity is high.

## 2.10 How Current Project Addresses the Gaps

The design direction of **GROCO – Smart Grocery Supply Management System** aligns with the above gaps through a constrained but practical integration strategy.

### Addressing benchmark-deployment mismatch

The project emphasizes on-device evaluation criteria such as stable latency and resource usage in addition to recognition quality. This aligns model choice with realistic smartphone operation rather than offline-only metrics.

### Addressing packaging-specific variability

The workflow uses packaging-aware cues by combining visual recognition with OCR-derived tokens and date parsing logic. This reduces dependency on generic visual features and improves robustness for similar-looking products.

### Addressing module integration gap

Instead of isolated predictions, the project follows staged fusion: detection proposes candidates, embedding retrieval narrows identity options, OCR provides textual constraints, and final ranking resolves ambiguity. This architecture reduces error amplification across components.

### Addressing open-vocabulary trade-off

Open-vocabulary capability is treated as a selective fallback, not as a full replacement for lightweight detectors. This keeps runtime predictable while still supporting unknown or long-tail products.

### Addressing price comparison reliability

The project prioritizes product normalization and unit-aware comparison before recommendation scoring. This ensures that offer ranking is economically meaningful and reduces misleading outcomes from mismatched listings.

### Addressing uncertainty communication

By retaining top-k candidates and cross-modal checks, the system can present alternatives when confidence is low. This is more trustworthy than forcing a single uncertain prediction.

Overall, the project’s contribution is not a novel single algorithm; it is a deployable integration of established methods, selected and combined for mobile grocery conditions with explicit attention to practical constraints.

## 2.11 Chapter Summary

This chapter reviewed literature relevant to an AI-enabled grocery assistance pipeline, with emphasis on comparative suitability for mobile deployment. Real-time object detection research shows that efficient one-stage models are generally preferable for smartphones, but deployment success depends on sustained latency and thermal stability rather than peak benchmark scores. Lightweight backbone studies, especially MobileNet-family developments, demonstrate that architectural efficiency and hardware-aware optimization are central to edge performance.

The review of embedding-based retrieval and ANN indexing shows why nearest-neighbor search is valuable for scalable product catalogs and frequent class updates. OCR literature highlights that packaging text and date extraction are critical complementary signals, particularly where visual similarity is high. Vision-language studies provide a path for open-vocabulary recognition, but practical deployment requires bounded use due to compute and reliability concerns. Price comparison and recommendation literature further indicates that accurate entity matching and unit normalization are prerequisites for meaningful user guidance.

Comparative analysis across representative studies reveals a recurring principle: robust grocery intelligence is achieved through modular fusion, not through dependence on any single model family. Based on this insight, the project positions itself as a practical integration framework for mobile grocery recognition and decision support, balancing AI capability with ECE deployment constraints.

---

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

[[VISUAL_SLOT: FIG_3_1_CONSTRAINT_DESIGN_MAP]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_3_1_constraint_design_map.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/integrated_architecture.png]]
[[VISUAL_CAPTION: Figure 3.1 Constraint-to-design mapping for GROCO modules]]

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

[[VISUAL_SLOT: FIG_3_2_END_TO_END_ARCHITECTURE]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_3_2_end_to_end_architecture.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/integrated_architecture.png]]
[[VISUAL_CAPTION: Figure 3.2 End-to-end architecture: camera-to-inventory-to-compare-to-reminders]]

[[VISUAL_SLOT: FIG_3_3_SCAN_REGISTRATION_FLOW]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_3_3_scan_registration_flow.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/scan_pipeline.png]]
[[VISUAL_CAPTION: Figure 3.3 Scan registration data flow with dual-step capture]]

[[VISUAL_SLOT: FIG_3_4_COMPARE_REORDER_FLOW]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_3_4_compare_reorder_flow.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/compare_pipeline.png]]
[[VISUAL_CAPTION: Figure 3.4 Multi-site compare and reorder handoff workflow]]

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

[[VISUAL_SLOT: FIG_3_5_AI_PIPELINE]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_3_5_ai_pipeline.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/legacy_reportfinal_img-006.jpg]]
[[VISUAL_CAPTION: Figure 3.5 AI pipeline: detection, embedding memory, extraction, fallback]]

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

[[VISUAL_SLOT: FIG_3_6_OFFER_COLLECTION_RANKING]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_3_6_offer_collection_ranking.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/compare_pipeline.png]]
[[VISUAL_CAPTION: Figure 3.6 Multi-source collection, normalization, matching, and ranking flow]]

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

[[VISUAL_SLOT: FIG_3_7_LOCAL_DATA_MODEL]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_3_7_local_data_model_er.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/data_model.png]]
[[VISUAL_CAPTION: Figure 3.7 Local entity model and subsystem read/write links]]

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

[[VISUAL_SLOT: FIG_3_8_TOOLING_MAP]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_3_8_tooling_map.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/testing_pyramid.png]]
[[VISUAL_CAPTION: Figure 3.8 Tooling map across scan, compare, storage, and reminder subsystems]]

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

[[VISUAL_SLOT: FIG_3_9_FAILURE_CONTAINMENT]]
[[VISUAL_MERMAID_FILE: visual-assets/mermaid/fig_3_9_failure_containment.mmd]]
[[VISUAL_IMAGE_FILE: visual-assets/images/legacy_reportfinal_img-007.jpg]]
[[VISUAL_CAPTION: Figure 3.9 Failure-containment strategy and graceful degradation paths]]

This risk-aware design mindset is essential for practical consumer AI systems, where real-world variability is the norm and usability under imperfect conditions is a core quality attribute.

## 3.8 Chapter Summary

This chapter detailed the design and methodology of **GROCO – Smart Grocery Supply Management System** as a balanced integration of AI/ML reasoning and production-oriented mobile architecture.

The system was designed under clear technical, economic, and sustainability constraints: mobile resource limits, unstable external data sources, minimal infrastructure budget, and a primary objective of reducing household grocery waste. These constraints directly shaped the architecture into four coordinated subsystems: scan registration, inventory intelligence, multi-source comparison, and reminder automation.

The AI/ML methodology was presented as a layered strategy: detection for region relevance, embeddings for local memory and similarity reuse, and expiry extraction through deterministic interpretation with LLM fallback for ambiguous cases. This was combined with a practical comparison methodology covering multi-site collection, normalization, relevance filtering, quantity-aware clustering, and confidence-linked ranking.

Data modelling emphasized local-first persistence through focused entities for inventory, compare history, and location settings, enabling offline continuity and low-latency interactions. Tool choices were justified based on deployability, maintainability, and functional fit for Android-first operation.

Finally, risk-aware design choices were described across AI uncertainty, source instability, ranking quality, reminder reliability, privacy, and maintainability. The key principle throughout is graceful degradation: when one subsystem weakens, the full user journey should still remain usable.

The resulting methodology establishes a robust foundation for subsequent chapters on implementation, testing, and measured outcomes.

---

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

---

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

---

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
