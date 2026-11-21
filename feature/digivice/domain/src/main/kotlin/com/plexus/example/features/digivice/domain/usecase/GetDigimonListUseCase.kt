package com.plexus.example.features.digivice.domain.usecase

import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.features.digivice.domain.repository.DigimonRepository
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.flow.Flow


class GetDigimonListUseCase(
    private val repository: DigimonRepository,
) {
    suspend operator fun invoke(): Flow<Either<List<DigimonOverviewDomainModel>, ErrorDomainModel>> {
        return repository.getDigimonList()
    }
}
