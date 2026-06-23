package com.kodemukti.namaibayi.data.remote.api

import com.kodemukti.namaibayi.data.remote.dto.GenerateRequestDto
import com.kodemukti.namaibayi.data.remote.dto.GenerateResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NamAIApi {
    @POST("api/v1/generate")
    suspend fun generateNames(@Body request: GenerateRequestDto): Response<GenerateResponseDto>
}
