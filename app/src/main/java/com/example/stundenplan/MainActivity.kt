package com.example.stundenplan

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.stundenplan.ui.theme.StundenplanTheme
import com.example.stundenplan.wrapper.ApiWrapper
import com.example.stundenplan.wrapper.UserData


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder()
            .permitAll().build())

        val userData = UserData(resources.getString(R.string.username),
                                resources.getString(R.string.password))
        val api = ApiWrapper(userData)
        setContent {
            StundenplanTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column {
                        TopBar(
                            clickLogout = api::login,
                            clickFilter = { print("filter") },
                            clickRefresh = api::getEvents
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        NavigationBarSample()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    clickLogout: () -> Unit,
    clickFilter: () -> Unit,
    clickRefresh: () -> Unit
) {
    TopAppBar(
        title = {
            Text("Veranstaltungen",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        actions = {
            Box {
                Row {
                    IconButton(onClick = clickLogout) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout_light),
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = clickFilter) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = clickRefresh) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}

@Composable
fun NavigationBarSample() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Veranstaltungen", "Kontakte", "Einstellungen")
    val icons = listOf(Icons.Outlined.DateRange, Icons.Filled.AccountBox, Icons.Filled.Settings)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}