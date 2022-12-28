package de.leonsuv.stundenplan

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.stundenplan.R
import de.leonsuv.stundenplan.model.EventData
import de.leonsuv.stundenplan.wrapper.ApiWrapper
import de.leonsuv.stundenplan.model.UserData
import de.leonsuv.stundenplan.screen.ContactsScreen
import de.leonsuv.stundenplan.screen.HomeScreen
import de.leonsuv.stundenplan.screen.SettingsScreen
import de.leonsuv.stundenplan.screen.Tags
import de.leonsuv.stundenplan.ui.theme.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .permitAll().build()
        )

        val encodedCredentials =
            UserData(base64 = intent.getStringExtra("base64") ?: "")
        val api = ApiWrapper(encodedCredentials)
        //this needs to be in login:
        api.login()
        val events = api.getEvents("2022-11-25")
        setContent {
            StundenplanTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(api, events ?: throw java.lang.NullPointerException())
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(api: ApiWrapper, events: List<EventData.Event>) {
    val activeScreen = mutableStateOf<Tags>(Tags.Home)
    val navController = rememberNavController()
    var title by remember { mutableStateOf("Home") }
    Column {
        Scaffold(topBar = {
            TopBar(
                clickLogout = api::login,
                clickFilter = { print("filter") },
                clickRefresh = { api.getEvents("2022-11-25") },
                title
            )
        },
            bottomBar = {
                NavigationBarSample { id: Int ->
                    navController.navigate(Tags.Items.list[id].id)
                    title = Tags.Items.list[id].title
                }
            },
            content = {
                val padding = it
                NavHost(navController = navController, startDestination = activeScreen.value.id) {
                    composable(Tags.Home.id) { HomeScreen(padding = padding, events) }
                    composable(Tags.Contacts.id) { ContactsScreen(padding = padding) }
                    composable(Tags.Settings.id) { SettingsScreen(padding = padding) }
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    clickLogout: () -> Unit,
    clickFilter: () -> Unit,
    clickRefresh: () -> Unit,
    title: String
) {
    val icons = listOf(
        R.drawable.ic_logout_light,
        R.drawable.ic_filter_light,
        R.drawable.ic_refresh_light
    )
    val methods = listOf(clickLogout, clickFilter, clickRefresh)
    TopAppBar(
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            Box {
                Row {
                    icons.forEachIndexed { index, item ->
                        IconButton(onClick = { methods[index] }) {
                            Icon(
                                painter = painterResource(id = item),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}

@Composable
fun NavigationBarSample(onClick: (Int) -> Unit) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        "Home",
        "Kontakte",
        "Einstellungen"
    )
    val icons = listOf(
        Icons.Outlined.Home,
        Icons.Outlined.AccountBox,
        Icons.Outlined.Settings
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    onClick(index)
                }
            )
        }
    }
}