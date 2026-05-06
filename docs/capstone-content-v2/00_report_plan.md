# Groco Capstone Report Plan (Content-First, Formatting by Student)

## Objective
Produce a simple, submission-ready academic report with stronger AI/ML and ECE orientation, reduced software-engineering implementation verbosity, and an overall target length of ~70 pages.

## Target Page Budget (~70 pages)
- Preliminary pages: 8 pages
  - Cover, Certificate, Declaration, Acknowledgement, Abstract, TOC, List of Figures, List of Tables, List of Abbreviations
- Chapter 1: Introduction: 8 pages
- Chapter 2: Literature Survey: 14 pages
- Chapter 3: System Design and Methodology: 14 pages
- Chapter 4: Implementation and Testing: 12 pages
- Chapter 5: Results and Discussion: 9 pages
- Chapter 6: Conclusion and Future Scope: 4 pages
- References: 2 pages
- Appendices/Annexure essentials: 3 pages

Total: ~74 pages including figures/tables; trim during formatting to ~70 pages.

## Writing Style Rules
- Keep academic tone, simple and direct.
- Focus on AI/ML pipeline understanding, methodology, and analysis.
- Minimize references to specific source files, function names, and engineering jargon.
- Include equations, model reasoning, and evaluation logic where relevant.
- Use first-person singular minimally; prefer objective narrative.
- Keep “funding pitch/product strategy” language out.

## Section Ownership (Subagents)
- Agent A: Preliminaries + Chapter 1
- Agent B: Chapter 2 Literature Survey (deep, comparative, gap-focused)
- Agent C: Chapter 3 System Design & Methodology (AI/ML centered)
- Agent D: Chapter 4 Implementation & Testing (experiment-centric, less code-centric)
- Agent E: Chapter 5 Results & Discussion (tables/metrics interpretation)
- Agent F: Chapter 6 + References + Appendices essentials

## Output Files
- sections/01_prelim_and_ch1.md
- sections/02_literature_survey.md
- sections/03_system_design_methodology.md
- sections/04_implementation_testing.md
- sections/05_results_discussion.md
- sections/06_conclusion_refs_appendix.md
- 99_master_report_content.md (merged)
