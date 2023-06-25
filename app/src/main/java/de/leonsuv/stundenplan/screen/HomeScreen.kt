package de.leonsuv.stundenplan.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.leonsuv.stundenplan.component.EventRows
import de.leonsuv.stundenplan.wrapper.ApiWrapper

@OptIn(ExperimentalFoundationApi::class)
@Composable
// Startbildschirm-Funktion, die das HomeScreen-Layout erstellt
fun HomeScreen(padding: PaddingValues, api: ApiWrapper) {
    // Zustand für den HorizontalPager speichern
    val pageCount = 20
    val pagerState = rememberPagerState(
        initialPage = 7,
        initialPageOffsetFraction = 0f
    ) {
        pageCount
    }
    val height = 220.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        EventRows(api = api, pagerState = pagerState, height = height)
    }
}
