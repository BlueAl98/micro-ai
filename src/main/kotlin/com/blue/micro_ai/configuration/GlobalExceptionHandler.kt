package com.blue.micro_ai.configuration

import com.blue.micro_ai.model.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class GlobalExceptionHandler {
    // 🧩 Validation errors (e.g. missing fields, bad body)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // 422
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Nothing>> {
        val errorMsg = ex.bindingResult.fieldErrors.joinToString(", ") {
            "${it.field}: ${it.defaultMessage}"
        }
        val response = ApiResponse<Nothing>(
            status = 422,
            message = "Validation error",
            data = null,
            error = errorMsg
        )
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response)
    }

    // 🧠 Known bad requests (e.g., IllegalArgumentException)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse<Nothing>(
            status = 400,
            message = "Bad request",
            data = null,
            error = ex.message
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    // ⚠️ Missing resources or nulls
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse<Nothing>(
            status = 404,
            message = "Resource not found",
            data = null,
            error = ex.message
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }

    // ⚡ Any ResponseStatusException (custom HTTP codes)
    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatus(ex: ResponseStatusException): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse<Nothing>(
            status = ex.statusCode.value(),
            message = ex.reason ?: "Error",
            data = null,
            error = ex.message
        )
        return ResponseEntity.status(ex.statusCode).body(response)
    }

    // 💥 Generic fallback (unexpected errors)
    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        val response = ApiResponse<Nothing>(
            status = 500,
            message = "Internal Server Error",
            data = null,
            error = ex.localizedMessage ?: "Unknown error"
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }
}