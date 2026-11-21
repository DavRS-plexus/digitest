package com.plexus.example.features.digivice.domain.usecase

import com.plexus.example.features.digivice.domain.model.DigimonListDomainModel
import com.plexus.example.features.digivice.domain.repository.DigiviceRepository
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.flow.Flow

// TODO: ERRORdomainModel
class SearchDigimonUseCase(
    private val repository: DigiviceRepository,
) {
    suspend operator fun invoke(name: String): Flow<Either<DigimonListDomainModel, String>> {
        return repository.searchDigimon(name)
    }
}

