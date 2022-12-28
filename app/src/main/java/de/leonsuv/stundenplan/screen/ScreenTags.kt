package de.leonsuv.stundenplan.screen

sealed class Tags(
    val id: String,
    val title: String
) {
    object Home : Tags("home", "Home")
    object Contacts : Tags("contacts", "Kontakte")
    object Settings : Tags("settings", "Einstellungen")

    object Items {
        val list = listOf(Home, Contacts, Settings)
    }
}