# Visual Placement Index

Use the `[[VISUAL_SLOT: ...]]` tags in `99_master_report_content.md` to place visuals.

## Paths
- Master report content: `../99_master_report_content.md`
- All image files (single folder): `images/`
- Mermaid source files: `mermaid/`

## How to use
1. Find `[[VISUAL_SLOT: ...]]` in the master report.
2. Use the matching `[[VISUAL_IMAGE_FILE: ...]]` for direct image insertion.
3. If you prefer editable flowcharts, open the matching `[[VISUAL_MERMAID_FILE: ...]]`, copy code to mermaid.live, export PNG/SVG, and insert.

## Slot-to-File Map

| Visual Slot | Mermaid File | Image File | Intended Figure |
|---|---|---|---|
| `FIG_3_1_CONSTRAINT_DESIGN_MAP` | `mermaid/fig_3_1_constraint_design_map.mmd` | `images/integrated_architecture.png` | Figure 3.1 |
| `FIG_3_2_END_TO_END_ARCHITECTURE` | `mermaid/fig_3_2_end_to_end_architecture.mmd` | `images/integrated_architecture.png` | Figure 3.2 |
| `FIG_3_3_SCAN_REGISTRATION_FLOW` | `mermaid/fig_3_3_scan_registration_flow.mmd` | `images/scan_pipeline.png` | Figure 3.3 |
| `FIG_3_4_COMPARE_REORDER_FLOW` | `mermaid/fig_3_4_compare_reorder_flow.mmd` | `images/compare_pipeline.png` | Figure 3.4 |
| `FIG_3_5_AI_PIPELINE` | `mermaid/fig_3_5_ai_pipeline.mmd` | `images/legacy_reportfinal_img-006.jpg` | Figure 3.5 |
| `FIG_3_6_OFFER_COLLECTION_RANKING` | `mermaid/fig_3_6_offer_collection_ranking.mmd` | `images/compare_pipeline.png` | Figure 3.6 |
| `FIG_3_7_LOCAL_DATA_MODEL` | `mermaid/fig_3_7_local_data_model_er.mmd` | `images/data_model.png` | Figure 3.7 |
| `FIG_3_8_TOOLING_MAP` | `mermaid/fig_3_8_tooling_map.mmd` | `images/testing_pyramid.png` | Figure 3.8 |
| `FIG_3_9_FAILURE_CONTAINMENT` | `mermaid/fig_3_9_failure_containment.mmd` | `images/legacy_reportfinal_img-007.jpg` | Figure 3.9 |
| `FIG_4_1_IMPL_VERIFICATION_MAP` | `mermaid/fig_4_1_impl_verification_map.mmd` | `images/testing_pyramid.png` | Figure 4.1 |
| `FIG_4_2_SCAN_OVERLAY_CROP_FLOW` | `mermaid/fig_4_2_scan_overlay_crop_flow.mmd` | `images/legacy_reportfinal_img-001.jpg` | Figure 4.2 |
| `FIG_4_3_IDENTITY_EXPIRY_PIPELINE` | `mermaid/fig_4_3_identity_expiry_pipeline.mmd` | `images/legacy_reportfinal_img-005.jpg` | Figure 4.3 |
| `FIG_4_4_PARALLEL_ADAPTER_FANOUT` | `mermaid/fig_4_4_parallel_adapter_fanout.mmd` | `images/compare_pipeline.png` | Figure 4.4 |
| `FIG_4_5_ENV_STACK` | `mermaid/fig_4_5_environment_stack.mmd` | `images/timeline_gantt.png` | Figure 4.5 |
| `FIG_4_6_TEST_PYRAMID` | `mermaid/fig_4_6_test_pyramid.mmd` | `images/testing_pyramid.png` | Figure 4.6 |
| `FIG_4_7_E2E_CHECKPOINT_FLOW` | `mermaid/fig_4_7_e2e_checkpoint_flow.mmd` | `images/screenshot_home_current.png` | Figure 4.7 |
| `FIG_4_8_SECURITY_CONTROLS` | `mermaid/fig_4_8_security_safety_controls.mmd` | `images/legacy_reportfinal_img-004.jpg` | Figure 4.8 |
| `FIG_5_1_EVAL_FRAMEWORK` | `mermaid/fig_5_1_eval_framework.mmd` | `images/site_contribution.png` | Figure 5.1 |
| `FIG_5_2_DETECTION_ACCURACY` | `mermaid/fig_5_2_detection_accuracy_chart.mmd` | `images/relevant_ratio.png` | Figure 5.2 |
| `FIG_5_3_EXPIRY_ACCURACY` | `mermaid/fig_5_3_expiry_accuracy_chart.mmd` | `images/legacy_reportfinal_img-004.jpg` | Figure 5.3 |
| `FIG_5_4_CATEGORY_RELEVANCE` | `mermaid/fig_5_4_category_relevance_chart.mmd` | `images/site_contribution.png` | Figure 5.4 |
| `FIG_5_5_BASKET_COST_COMPARISON` | `mermaid/fig_5_5_basket_cost_comparison_chart.mmd` | `images/timeline_gantt.png` | Figure 5.5 |
| `FIG_5_6_STRENGTH_WEAKNESS` | `mermaid/fig_5_6_strength_weakness_map.mmd` | `images/relevant_ratio.png` | Figure 5.6 |

## Screenshot slots added explicitly

| Visual Slot | Image File | Purpose |
|---|---|---|
| `SS_5_A_HOME_DASHBOARD` | `images/screenshot_home_current.png` | Current home screen |
| `SS_5_B_LOCATION_SETTINGS` | `images/screenshot_location_settings_current.png` | Current location settings |
| `SS_5_C_COMPARE_BOTTOM_SHEET` | `images/screenshot_compare_sheet_current.png` | Current compare sheet |
| `SS_5_D_LEGACY_SCAN_FRONT` | `images/legacy_reportfinal_img-009.jpg` | Legacy scan front screenshot |
| `SS_5_E_LEGACY_SCAN_BACK` | `images/legacy_reportfinal_img-010.jpg` | Legacy scan back screenshot |
| `SS_5_F_LEGACY_CONFIRMATION` | `images/legacy_reportfinal_img-011.jpg` | Legacy confirmation screenshot |
