package com.blue.micro_ai.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
   val status : Int,
   val message : String,
   val data : T?,
   val error : String? = null
)

