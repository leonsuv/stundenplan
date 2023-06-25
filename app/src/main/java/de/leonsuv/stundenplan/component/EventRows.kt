package de.leonsuv.stundenplan.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import de.leonsuv.stundenplan.model.EventData
import de.leonsuv.stundenplan.wrapper.ApiWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalFoundationApi::class)
@Composable
// Funktion zum Anzeigen der Ereignisreihen
fun EventRows(api: ApiWrapper, pagerState: PagerState, height: Dp) {
    HorizontalPager(state = pagerState) { page ->
        val date = remember(page) {
            LocalDate.now().plusDays(page.toLong())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        var events by remember(date) {
            mutableStateOf<List<EventData.Event>?>(null)
        }

        LaunchedEffect(date) {
            if (events == null) {
                val fetchedEvents = withContext(Dispatchers.IO) {
                    api.getEvents(date)
                }
                if (fetchedEvents != null) {
                    events = fetchedEvents
                }
            }
        }

        Column(modifier = Modifier.height(height)) {
            events?.let { eventList ->
                LazyColumn {
                    items(eventList) { event ->
                        EventRow(event)
                        Divider()
                    }
                }
            }
        }
    }
}


@Composable
// Funktion zum Anzeigen einer einzelnen Ereignisreihe
fun EventRow(event: EventData.Event) {
    ListItem(headlineContent = { Text(event.title) },
        supportingContent = { Text(event.kind + ": " + event.teacher) },
        trailingContent = { Text(event.rooms.joinToString(separator = "\n")) },
        leadingContent = {
            // Anzeigen der Zeit in der führenden Position
            TimeLeading(from = event.startTime, to = event.endTime)
        }
    )
}

@Composable
// Funktion zum Anzeigen der Zeit in der führenden Position
fun TimeLeading(from: EventData.Time, to: EventData.Time) {
    Column {
        Text(
            // Startzeit anzeigen
            LocalTime.MIN.plus(Duration.ofMinutes(((from.hour * 60) + from.minute).toLong()))
                .toString()
        )
        Text(
            // Endzeit anzeigen
            LocalTime.MIN.plus(Duration.ofMinutes(((to.hour * 60) + to.minute).toLong())).toString()
        )
    }
}
