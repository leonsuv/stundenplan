package de.leonsuv.stundenplan.screen

// Versiegelte Klasse Tags, die verschiedene Bildschirme repräsentiert
sealed class Tags(
    val id: String, // Eindeutige ID des Bildschirms
    val title: String // Titel des Bildschirms
) {
    // Unterklassen, die konkrete Bildschirme repräsentieren
    object Home : Tags("home", "Home") // Startseite
    object Contacts : Tags("contacts", "Kontakte") // Kontakte-Seite
    object Settings : Tags("settings", "Einstellungen") // Einstellungen-Seite

    object Items {
        // Eine Liste aller Bildschirme
        val list = listOf(Home, Contacts, Settings)
    }
}
