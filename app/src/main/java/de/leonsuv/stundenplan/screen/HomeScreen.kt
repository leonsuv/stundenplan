package de.leonsuv.stundenplan.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.leonsuv.stundenplan.model.EventData
import java.time.Duration
import java.time.LocalTime

@Composable
fun HomeScreen(padding: PaddingValues, events: List<EventData.Event>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Column {
            EventRows(events = events)
        }
    }
}

@Composable
fun EventRows(events: List<EventData.Event>) {
    Column(
        modifier = Modifier
            .height(220.dp)
            .verticalScroll(rememberScrollState())
    ) {
        events.forEach {
            EventRow(it)
            if (events.indexOf(it) != events.size) {
                Divider()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventRow(event: EventData.Event) {
    ListItem(
        headlineText = { Text(event.title) },
        supportingText = { Text(event.kind + ": " + event.teacher) },
        trailingContent = { Text(event.rooms.joinToString(separator = "\n")) },
        leadingContent = {
            TimeLeading(from = event.startTime, to = event.endTime)
        }
    )
}

@Composable
fun TimeLeading(from: EventData.Time, to: EventData.Time) {
    Column {
        Text(
            LocalTime.MIN.plus(Duration.ofMinutes(((from.hour * 60) + from.minute).toLong()))
                .toString()
        )
        Text(
            LocalTime.MIN.plus(Duration.ofMinutes(((to.hour * 60) + to.minute).toLong())).toString()
        )
    }
}