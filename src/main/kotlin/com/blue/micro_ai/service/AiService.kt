package com.blue.micro_ai.service

import com.blue.micro_ai.model.ApiResponse
import com.blue.micro_ai.model.CvAnalysisResult
import org.springframework.stereotype.Service

@Service
class AiService (
    private val providers: Map<String, AiProvider>
){
    /**
     * Generic AI method that can return any type of parsed model response.
     *
     * @param prompt The message or input for the AI model.
     * @param responseType The target class type to deserialize the model response into.
     * @param providerName The AI provider name (default: "ollamaProvider").
     */
    fun <T> getAiData(
        prompt: String,
        responseType: Class<T>,
        providerName: String = "ollamaProvider"
    ): ApiResponse<T> {
        val provider = providers[providerName]
            ?: throw IllegalArgumentException("Provider '$providerName' not found")

        return provider.chat(prompt, responseType)
    }
}