package com.example.stundenplan.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

//Under big construction
class EventData {
    //Section: model
    //Storing all Dates and events
    var data: List<DateEventData> = emptyList()
    private lateinit var jsonData: JSONObject

    //Events grouped by date
    data class DateEventData(
        @SerializedName("Date") val date: String,
        @SerializedName("Event") val events: List<Event>
    )
    //Single event template
    data class Event(
        @SerializedName("Title") val title: String,
        @SerializedName("Kind") val kind: String,
        @SerializedName("Teacher") val teacher: String,
        @SerializedName("Starttime") val startTime: Time,
        @SerializedName("Endtime") val endTime: Time,
        @SerializedName("Rooms") val rooms: List<String>
    )
    //Start and end time of event
    data class Time(
        @SerializedName("Hour") val hour: Int,
        @SerializedName("Minute") val minute: Int
    )
    //End-section: model

    //Section: Parsing Json
    fun getEventsOnDate(date: String): DateEventData {


        //return Gson().toJson(data.find { it.date == date }?.events ?: return "No events")
        return DateEventData(date, emptyList())
    }

    fun saveAllEvents() {
        //Go through all dates in json
        //and save getEventsOnDate(date) in data
    }

    data class eventsJson(
        val data: List<DateEventData>
    )

    companion object {
        fun fromJson(json: String): EventData {
            //TODO: Parse JSON to OOP-Classes
            val eventData = EventData()

            val data: eventsJson =
                Gson().fromJson(json, eventsJson::class.java)

            println(data.data.size)
            return eventData
        }
    }
    //End-Section: Parsing Json
}
