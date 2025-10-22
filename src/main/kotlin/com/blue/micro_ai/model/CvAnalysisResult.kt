package com.blue.micro_ai.model

data class CvAnalysisResult(
    val candidate: Candidate,
    val evaluation: Evaluation,
    val key_points: KeyPoints,
    val improvement_tips: List<String>
)

data class Candidate(
    val name: String,
    val years_experience: Int,
    val summary: String
)

data class Evaluation(
    val technical_match: Double,
    val experience_match: Double,
    val overall_score: Double,
    val verdict: String
)

data class KeyPoints(
    val strengths: List<String>,
    val weaknesses: List<String>,
    val recommended_questions: List<String>
)