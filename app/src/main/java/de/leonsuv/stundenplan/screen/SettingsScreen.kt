package de.leonsuv.stundenplan.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun PrevSettingsScreen() {
    SettingsScreen(padding = PaddingValues(0.dp, 20.dp, 0.dp, 20.dp))
}

@Composable
fun SettingsScreen(padding: PaddingValues) {
    val availableOptions = listOf(
        "Verbesserungsvorschlag",
        "Fehler melden",
        "About"
    )
    val icons = listOf(
        Icons.Outlined.Notifications,
        Icons.Outlined.Warning,
        Icons.Outlined.Info
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            availableOptions.let { options ->
                LazyColumn {
                    items(options) { option ->
                        Divider()
                        ListItem(
                            headlineContent = {
                                Button(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    contentPadding = PaddingValues(start = 0.dp)
                                ) {
                                    Text(
                                        text = option,
                                        color = Color.Black,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            },
                            leadingContent = {
                                Icon(
                                    icons[availableOptions.indexOf(option)],
                                    contentDescription = option
                                )
                            })
                        Divider()
                    }
                    items(1) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider()
                        ListItem(
                            headlineContent = {
                                Button(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    contentPadding = PaddingValues(start = 0.dp)
                                ) {
                                    Text(
                                        color = Color.Red,
                                        text = "Abmelden",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Outlined.AccountBox,
                                    contentDescription = "Logout"
                                )
                            },
                        )
                        Divider()
                    }
                }
            }
        }
    }
}