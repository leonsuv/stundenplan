package de.leonsuv.stundenplan

import android.annotation.*
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.stundenplan.R
import de.leonsuv.stundenplan.wrapper.ApiWrapper
import de.leonsuv.stundenplan.model.UserData
import de.leonsuv.stundenplan.screen.ContactsScreen
import de.leonsuv.stundenplan.screen.HomeScreen
import de.leonsuv.stundenplan.screen.SettingsScreen
import de.leonsuv.stundenplan.screen.Tags
import de.leonsuv.stundenplan.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ermöglicht den Netzwerkzugriff im Haupt-Thread
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder().permitAll().build()
        )

        // Base64-kodierte Anmeldeinformationen aus Intent-Extras extrahieren
        val encodedCredentials =
            UserData(base64 = intent.getStringExtra("base64") ?: "")
        val api = ApiWrapper(encodedCredentials)

        // Anmeldung beim API durchführen
        api.login()

        setContent {
            StundenplanTheme {
                // Ein Container (Surface) mit Hintergrundfarbe des Themes
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Hauptbildschirm anzeigen
                    MainScreen(api)
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(api: ApiWrapper) {
    // Aktuell ausgewählter Bildschirm
    val activeScreen = mutableStateOf<Tags>(Tags.Home)
    // Navigation Controller initialisieren
    val navController = rememberNavController()
    // Aktueller Titel
    var title by remember { mutableStateOf("Home") }

    val date = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    Column {
        // Scaffold-Komponente mit TopBar, BottomBar und Inhalt
        Scaffold(
            topBar = {
                // Obere Navigationsleiste (TopBar) anzeigen
                TopBar(
                    clickLogout = api::login,
                    clickFilter = { print("filter") },
                    clickRefresh = {CoroutineScope(Dispatchers.Main).launch {api.getEvents(date)}},
                    title
                )
            },
            bottomBar = {
                // Untere Navigationsleiste (NavigationBarSample) anzeigen
                NavBar(navController = navController) { id: Int ->
                    navController.navigate(Tags.Items.list[id].id)
                    title = Tags.Items.list[id].title
                }
            },
            content = {
                val padding = it
                // NavHost für die Navigation zwischen Bildschirmen
                NavHost(navController = navController, startDestination = activeScreen.value.id) {
                    composable(Tags.Home.id) { HomeScreen(padding = padding, api = api) }
                    composable(Tags.Contacts.id) { ContactsScreen(padding = padding, api = api) }
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
    // Symbole für Logout, Filter und Aktualisierung
    val icons = listOf(
        R.drawable.ic_logout_light,
        R.drawable.ic_filter_light,
        R.drawable.ic_refresh_light
    )
    // Klickfunktionen für die Symbole
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
                    // Symbole als Aktionsschaltflächen anzeigen
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
fun NavBar(navController: NavController, onClick: (Int) -> Unit) {


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

    val currentRoute = navController.currentBackStackEntryAsState()

    var selectedItem by rememberSaveable {
        mutableIntStateOf(
            items.indexOfFirst { it == currentRoute.value?.destination?.route }
        )
    }

    BackHandler {
        navController.popBackStack()
    }

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