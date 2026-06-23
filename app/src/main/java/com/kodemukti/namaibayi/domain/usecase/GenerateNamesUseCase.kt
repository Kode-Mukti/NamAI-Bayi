package com.kodemukti.namaibayi.domain.usecase

import com.kodemukti.namaibayi.domain.model.GenerateRequest
import com.kodemukti.namaibayi.domain.model.AIResponse
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
import javax.inject.Inject

class GenerateNamesUseCase @Inject constructor(
    private val repository: GenerateRepository,
) {
    suspend operator fun invoke(request: GenerateRequest): Result<AIResponse> {
        return repository.generateNames(request)
    }
}
