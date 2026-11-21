package com.plexus.example.features.digivice.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plexus.example.features.digivice.ui.navigation.DigiviceInternalRoute
import com.plexus.example.features.digivice.ui.search.screen.DigiviceSearchScreen
import com.plexus.example.features.digivice.ui.search.viewmodel.DigiviceSearchViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun DigiviceListEntryPoint(
    navigateTo: (route: DigiviceInternalRoute) -> Unit,
    back: () -> Unit,
) {
    val searchViewModel: DigiviceSearchViewModel = koinViewModel()
    val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()

//    HandleUiEvents(
//        viewModel = searchViewModel,
//        navigateTo = navigateTo,
//    )

    DigiviceSearchScreen(
        uiState = uiState,
        onBack = back,
        onClickSearch = searchViewModel::searchDigimon,
        onClickItem = { transactionId ->
//            searchViewModel.onAction(
//                DigiviceSearchViewModel.UserAction.OnClickTransactionItem(transactionId)
//            )
        }
    )
}

//@Composable
//private fun HandleUiEvents(
//    viewModel: DigiviceSearchViewModel,
//    navigateTo: (route: TransactionInternalRoute) -> Unit,
//    navigateToSavings: () -> Unit,
//) {
//    ObserveUiEvent(viewModel = viewModel) { event ->
//        when (event) {
//            TransactionListUiEvent.NavigateToFilters -> navigateTo(TransactionFilterRoute)
//            TransactionListUiEvent.NavigateToSavings -> navigateToSavings()
//            is TransactionListUiEvent.NavigateToDetail -> navigateTo(
//                TransactionDetailRoute(transactionId = event.transactionId)
//            )
//        }
//    }
//}
