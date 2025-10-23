package com.blue.micro_ai.model

import jakarta.validation.constraints.NotBlank

data class RequestAiCv(
    @field:NotBlank(message = "CV text is required")
    val cvText: String?,
    @field:NotBlank(message = "Target profile is required")
    val targetProfile : String?
)
