package com.blue.micro_ai.service

import com.blue.micro_ai.model.ApiResponse

interface AiProvider {
    fun <T> chat(message: String?, responseType: Class<T>): ApiResponse<T>
}