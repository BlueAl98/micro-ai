package com.blue.micro_ai.service

import com.blue.micro_ai.model.ApiResponse
import com.blue.micro_ai.model.CvAnalysisResult
import com.blue.micro_ai.model.OllamaResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service("ollamaProvider")
class OllamaService(private val webClient: WebClient,  private val objectMapper: ObjectMapper): AiProvider {


    fun getCvAnalysis(prompt: String): ApiResponse<CvAnalysisResult> {
        return chat(prompt, CvAnalysisResult::class.java)
    }

    override fun <T> chat(message: String, responseType: Class<T>): ApiResponse<T> {
        val request = mapOf(
            "model" to "qwen2.5:3b",
            "messages" to listOf(mapOf("role" to "user", "content" to message))
        )

        // 1️⃣ Send request to Ollama
        val ollamaResponse = webClient.post()
            .uri("/v1/chat/completions")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OllamaResponse::class.java)
            .block() ?: throw RuntimeException("No response from Ollama API")

        // 2️⃣ Extract model message
        val content = ollamaResponse.choices.firstOrNull()?.message?.content
            ?: throw RuntimeException("No message content in Ollama response")

        return try {
            // 3️⃣ Try parsing the model’s JSON message to your desired class
            val parsedData = objectMapper.readValue(content, responseType)
            ApiResponse(status = 200, message = "Success", data = parsedData, error = null)
        } catch (e: Exception) {
            // 4️⃣ Handle parsing errors
            ApiResponse(data = null, message = "Parsing error: ${e.message}", status = 400, error = e.message)

        }
    }
}



