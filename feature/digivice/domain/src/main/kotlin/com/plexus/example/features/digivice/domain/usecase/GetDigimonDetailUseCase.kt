package com.plexus.example.features.digivice.domain.usecase

import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonDetailModel
import com.plexus.example.features.digivice.domain.repository.DigimonRepository
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.flow.Flow

class GetDigimonDetailUseCase(private val repository: DigimonRepository) {
    suspend operator fun invoke(id: String): Flow<Either<DigimonDetailModel, ErrorDomainModel>> =
        repository.getDigimonDetail(id)
}
