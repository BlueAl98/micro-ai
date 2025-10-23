package com.blue.micro_ai.model

import jakarta.validation.constraints.NotBlank

data class RequestAi (
    @field:NotBlank(message = "Prompt is required")
    val prompt: String?
)