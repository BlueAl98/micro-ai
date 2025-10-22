package com.blue.micro_ai.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig(
    @Value("\${base.url.ai}")
    private val ollamaServerUrl: String
) {

    @Bean
    fun ollamaWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl(ollamaServerUrl) // your Ollama server URL
            .build()
    }

}