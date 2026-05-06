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
