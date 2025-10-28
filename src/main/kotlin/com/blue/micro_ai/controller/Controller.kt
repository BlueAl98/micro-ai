package com.blue.micro_ai.controller

import com.blue.micro_ai.model.*
import com.blue.micro_ai.service.*
import com.blue.micro_ai.utils.getPromtCvAnlisis
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/ai")
class Controller (
    private val aiService: AiService
){

    @PostMapping
    fun generalAi(
        @Valid  @RequestBody request: RequestAi
    ): ApiResponse<ResponseAi> {
        return aiService.getAiData(
            prompt = request.prompt,
            responseType = ResponseAi::class.java)
    }


    @PostMapping("/cvAnalisis")
    fun generateCvAnalisis(
        @Valid @RequestBody request: RequestAiCv
    ): ApiResponse<CvAnalysisResult> {

        val prompt = """
        ${getPromtCvAnlisis(request.targetProfile)}
        Candidate CV:
        ${request.cvText}
        """.trimIndent()

        return aiService.getAiData(
            prompt = prompt,
            responseType = CvAnalysisResult::class.java)
    }


}




