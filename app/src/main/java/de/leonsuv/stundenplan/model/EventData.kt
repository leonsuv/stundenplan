package de.leonsuv.stundenplan.model

import org.json.JSONArray
import org.json.JSONObject

//Under big construction
class EventData {
    //Section: model
    //Storing all Dates and events
    var data: List<DateEventData> = emptyList()

    //Events grouped by date
    data class DateEventData(
        val date: String,
        val events: List<Event>
    )
    //Single event template
    data class Event(
        val title: String,
        val kind: String,
        val teacher: String,
        val startTime: Time,
        val endTime: Time,
        val rooms: List<String>
    )
    //Start and end time of event
    data class Time(
        val hour: Int,
        val minute: Int
    )
    //End-section: model

    fun getEventsOnDate(date: String): List<Event> {
        return (data.filter {it.date == date}).single().events
    }

    //Section: Parsing Json
    companion object {
        fun fromJson(json: String): EventData {
            //TODO: Parse JSON to OOP-Classes
            val eventData = EventData()

            val data = JSONArray(json)
            eventData.data = List(data.length()) { idx -> createDateEventData(data.getJSONObject(idx)) }

            return eventData
        }

        private fun createDateEventData(json: JSONObject): DateEventData {
            return DateEventData(json.getString("Date"), createEvents(json))
        }

        private fun createEvents(json: JSONObject): List<Event> {
            val jsonEvents = json.getJSONArray("Event")
            return List(jsonEvents.length()) {idx -> createSingleEvent(idx, jsonEvents) }
        }

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

        private fun getRooms(event: JSONObject): List<String> {
            val rooms = event.getJSONArray("Rooms")
            return List(rooms.length()) {idx -> rooms.getString(idx)}
        }

        private fun getStartTime(json: JSONObject): Time {
            return Time(json.getInt("Hour"), json.getInt("Minute"))
        }

        private fun getEndTime(json: JSONObject): Time {
            return Time(json.getInt("Hour"), json.getInt("Minute"))
        }
    }
    //End-Section: Parsing Json
}
