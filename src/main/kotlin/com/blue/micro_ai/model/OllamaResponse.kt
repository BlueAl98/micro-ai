package com.blue.micro_ai.model

import com.fasterxml.jackson.annotation.JsonProperty

data class OllamaResponse(
    val id: String,
    @JsonProperty("object")
    val objectType: String?, // nullable because JSON might be different
    val created: Long?,
    val model: String?,
    val system_fingerprint: String?,
    val choices: List<Choice> = emptyList()
)

data class Choice(
    val index: Int?,
    val message: Message?,
    val finish_reason: String?
)

data class Message(
    val role: String?,
    val content: String?
)
