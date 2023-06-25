package de.leonsuv.stundenplan.model

import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KProperty

class EventData {
    var data: List<DateEventData> = emptyList()

    // Datenklasse, die ein Datum und die zugehörigen Ereignisse repräsentiert
    data class DateEventData(
        val date: String,
        val events: List<Event>
    )

    // Datenklasse, die ein Ereignis repräsentiert
    data class Event(
        val title: String,
        val kind: String,
        val teacher: String,
        var startTime: Time,
        var endTime: Time,
        val rooms: List<String>
    ) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Time {
            return when (property.name) {
                "startTime" -> startTime
                "endTime" -> endTime
                else -> throw IllegalArgumentException("Unknown property name: ${property.name}")
            }
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Time) {
            when (property.name) {
                "startTime" -> startTime = value
                "endTime" -> endTime = value
                else -> throw IllegalArgumentException("Unknown property name: ${property.name}")
            }
        }
    }

    // Datenklasse, die eine Zeit repräsentiert
    data class Time(
        val hour: Int,
        val minute: Int
    )


    // Gibt eine Liste von Ereignissen für ein bestimmtes Datum zurück
    fun getEventsOnDate(date: String): List<Event> {
        return data.firstOrNull { it.date == date }?.events.orEmpty()
    }


    companion object {
        // Erstellt ein EventData-Objekt aus einem JSON-String
        fun fromJson(json: String): EventData {
            val eventData = EventData()

            val data = JSONArray(json)
            eventData.data = List(data.length()) { idx -> createDateEventData(data.getJSONObject(idx)) }

            return eventData
        }

        // Erstellt ein DateEventData-Objekt aus einem JSON-Objekt
        private fun createDateEventData(json: JSONObject): DateEventData {
            return DateEventData(json.getString("Date"), createEvents(json))
        }

        // Erstellt eine Liste von Event-Objekten aus einem JSON-Objekt
        private fun createEvents(json: JSONObject): List<Event> {
            val jsonEvents = json.getJSONArray("Event")
            return List(jsonEvents.length()) {idx -> createSingleEvent(idx, jsonEvents) }
        }

        // Erstellt ein einzelnes Event-Objekt aus einem JSON-Array
        private fun createSingleEvent(idx: Int, json: JSONArray): Event {
            val event = json.getJSONObject(idx)
            return Event(
                event.getString("Title"),
                event.getString("Kind"),
                event.getString("Teacher"),
                getStartTime(event.getJSONObject("Starttime")),
                getEndTime(event.getJSONObject("Endtime")),
                getRooms(event)
            )
        }

        // Gibt eine Liste von Räumen aus einem JSON-Objekt zurück
        private fun getRooms(event: JSONObject): List<String> {
            val rooms = event.getJSONArray("Rooms")
            return List(rooms.length()) {idx -> rooms.getString(idx)}
        }

        // Erstellt ein Time-Objekt aus einem JSON-Objekt für die Startzeit
        private fun getStartTime(json: JSONObject): Time {
            return Time(json.getInt("Hour"), json.getInt("Minute"))
        }

        // Erstellt ein Time-Objekt aus einem JSON-Objekt für die Endzeit
        private fun getEndTime(json: JSONObject): Time {
            return Time(json.getInt("Hour"), json.getInt("Minute"))
        }
    }
}
