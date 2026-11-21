package com.plexus.example.features.digivice.ui.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plexus.example.features.digivice.domain.model.DigimonOverviewDomainModel
import com.plexus.example.features.digivice.domain.usecase.GetDigimonListUseCase
import com.plexus.example.features.digivice.domain.usecase.SearchDigimonUseCase
import com.plexus.example.features.digivice.ui.search.mapper.toUi
import com.plexus.example.features.digivice.ui.search.state.DigiviceSearchUiState
import com.plexus.example.libs.kotlinutils.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class DigiviceSearchViewModel(
    private val getDigimonListUseCase: GetDigimonListUseCase,
    private val searchDigimonUseCase: SearchDigimonUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DigiviceSearchUiState(isLoading = true))
    val uiState: StateFlow<DigiviceSearchUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null


    init {
        println("DAVID = init DigiviceSearchViewModel")
        getDigimonList()
    }

    private fun getDigimonList() {
        searchJob?.cancel()
        _uiState.value = DigiviceSearchUiState(isLoading = true)

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(3000) // TODO: QUITAR
            getDigimonListUseCase().collect { result ->
                when (result) {
                    is Either.Success -> {
                        handleSuccessGetDigimonList(result.data)
                    }

                    is Either.Failure -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                errorMessage = "message"
                            )
                        }
                    }
                }
            }
        }
    }

    internal fun searchDigimon(name: String) {
        val query = name.trim()
        if (query.isEmpty()) {
            getDigimonList()
            return
        }

        searchJob?.cancel()
        _uiState.value = DigiviceSearchUiState(isLoading = true)

        searchJob = viewModelScope.launch(Dispatchers.IO) {
            searchDigimonUseCase(query).collect { result ->
                when (result) {
                    is Either.Success -> handleSuccessGetDigimonList(result.data.digimons)
                    is Either.Failure -> _uiState.update { currentState ->
                        currentState.copy(isLoading = false, errorMessage = result.fail)
                    }
                }
            }
        }
    }

    private fun handleSuccessGetDigimonList(digimons: List<DigimonOverviewDomainModel>) {
        println("DAVID = Success: $digimons")
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                items = digimons.map { it.toUi() }
            )
        }
    }

//    private fun resetData(selectedFilters: TransactionFilterUiModel? = null) {
//        currentFilters = selectedFilters
//        currentPagination = PaginationDomainModel(0, DEFAULT_PAGE_SIZE, 0)
//    }
//
//    internal fun getTransactions(currentTransactions: List<TransactionItemUiModel> = emptyList()) {
//        launchIO {
//            _uiState.update { currentState ->
//                currentState.copy(
//                    isLoading = true,
//                    errorMessage = null,
//                )
//            }
//
//            getTransactionsUseCase(
//                query = createQuery(currentFilters)
//            ).collect { result ->
//                when (result) {
//                    is Either.Success -> {
//                        handleSuccessGetTransactions(result.data, currentTransactions)
//                    }
//
//                    is Either.Failure -> {
//                        _uiState.update {
//                            it.copy(
//                                isLoading = false,
//                                errorMessage = result.fail.message,
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun createQuery(
//        filters: TransactionFilterUiModel?
//    ): TransactionsQueryModel {
//        val (transactionType, transactionSubType) = filters?.transactionType?.toDomainModel()
//            ?: Pair(null, null)
//
//        return with(DateConverter) {
//            TransactionsQueryModel(
//                type = transactionType,
//                subType = transactionSubType,
//                minTotalAmount = filters?.transactionAmountRange?.startAmount,
//                maxTotalAmount = filters?.transactionAmountRange?.endAmount,
//                startDateUtcMillis = filters?.transactionDateRange?.startLocalDate?.toUtcMillis(),
//                endDateUtcMillis = filters?.transactionDateRange?.endLocalDate?.toUtcMillis(),
//                pageNumber = currentPagination.pageNumber + 1,
//                pageSize = currentPagination.pageSize,
//            )
//        }
//    }
//
//    private fun handleSuccessGetTransactions(
//        newData: TransactionsDomainModel,
//        currentTransactions: List<TransactionItemUiModel>
//    ) {
//        currentPagination = newData.pagination
//
//        val newTransactions: List<TransactionItemUiModel> =
//            newData.transactions.map { it.toUiModel() }
//        val allTransactions = currentTransactions + newTransactions
//        val transactionsGroupedByDate = groupTransactionsByDate(allTransactions)
//
//        _uiState.update {
//            it.copy(
//                isLoading = false,
//                groupedTransactions = transactionsGroupedByDate,
//                hasMoreData = allTransactions.size < currentPagination.totalItems,
//                errorMessage = null,
//            )
//        }
//    }
//
//    private fun groupTransactionsByDate(transactions: List<TransactionItemUiModel>): List<TransactionGroupUiModel> {
//        return transactions.groupBy { it.dateUtcMillis.toLocalDate() }
//            .map { (date, items) ->
//                TransactionGroupUiModel(
//                    date = date,
//                    transactions = items,
//                )
//            }
//    }
//
//    private fun getGowBalance() {
//        launchIO {
//            getPreferenceUseCase(
//                key = PreferenceKey.GOW_BALANCE,
//                valueType = Double::class
//            ).collect { result ->
//                if (result is Either.Success) {
//                    _uiState.update { currentState ->
//                        currentState.copy(
//                            gowBalance = result.data
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    // region User Actions
//
//    internal fun onAction(action: UserAction) {
//        when (action) {
//            is UserAction.OnClickSavings -> {
//                emitEvent(DigiviceSearchUiEvent.NavigateToSavings)
//            }
//
//            is UserAction.OnClickInfoTransactions -> {
//                _uiState.update { it.copy(isInfoVisible = true) }
//            }
//
//            is UserAction.OnDismissInfoTransactions -> {
//                _uiState.update { it.copy(isInfoVisible = false) }
//            }
//
//            is UserAction.OnClickOpenFilters -> {
//                emitEvent(DigiviceSearchUiEvent.NavigateToFilters)
//            }
//
//            is UserAction.OnClickTransactionItem -> {
//                emitEvent(DigiviceSearchUiEvent.NavigateToDetail(action.transactionId))
//            }
//
//            is UserAction.OnEndScroll -> {
//                loadMoreTransactions()
//            }
//
//            is UserAction.FiltersUpdated -> {
//                if (action.shouldReload) {
//                    resetData(action.selectedFilters)
//                    getTransactions()
//                }
//            }
//        }
//    }
//
//    private fun loadMoreTransactions() {
//        val currentState = _uiState.value
//        getTransactions(currentState.groupedTransactions.flatMap { it.transactions })
//    }
//
//    // endregion
//
//    internal sealed class UserAction {
//        object OnClickSavings : UserAction()
//
//        object OnClickInfoTransactions : UserAction()
//        object OnDismissInfoTransactions : UserAction()
//
//        object OnClickOpenFilters : UserAction()
//
//        data class OnClickTransactionItem(val transactionId: String) : UserAction()
//
//        object OnEndScroll : UserAction()
//
//        data class FiltersUpdated(
//            val selectedFilters: TransactionFilterUiModel?,
//            val shouldReload: Boolean
//        ) : UserAction()
//    }
//
//    internal companion object {
//        private const val DEFAULT_PAGE_SIZE = 20
//    }
}