package com.plexus.example.features.digivice.domain.usecase

import com.plexus.example.core.error.domain.model.ErrorDomainModel
import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.repository.DigimonRepository
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.flow.Flow


class SearchDigimonUseCase(
    private val repository: DigimonRepository,
) {
    suspend operator fun invoke(name: String): Flow<Either<DigimonListDomainModel, ErrorDomainModel>> {
        return repository.searchDigimon(name)
    }
}

