package de.leonsuv.stundenplan.wrapper

import de.leonsuv.stundenplan.model.EventData
import de.leonsuv.stundenplan.model.UserData
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject

class ApiWrapper(private val userdata: UserData){

    fun login(): Boolean {
        val request = Request.Builder()
            .method("POST", "".toRequestBody())
            .url("https://app.phwt.de/api/v1/auth/login")
            .header("Authorization", "Basic ${userdata.getBase64()}")
            .build()
        println(userdata.getBase64())
        OkHttpClient().newCall(request).execute().use { response ->
            val bodyJson = getResponseBodyJson(response)
            try {
                bodyJson?.getString("access_token")?.let { userdata.setAccessToken(it) }
                bodyJson?.getString("refresh_token")?.let { userdata.setRefreshToken(it) }
            } catch (e: JSONException) {
                return false
            }
        }
        return userdata.isAuthorized()
    }

    fun getEvents(date: String): List<EventData.Event>? {
        if (!userdata.isAuthorized()) {
            return null
        }
        var eventData: EventData?
        val request = Request.Builder()
            .method("GET", null)
            .url("https://app.phwt.de/api/v2/events")
            .header("Authorization", "Bearer ${userdata.getRefreshToken()}")
            .build()
        OkHttpClient().newCall(request).execute().use { response ->
            eventData = response.body?.let { EventData.fromJson(it.string()) }
        }
        //testing:
        return eventData?.getEventsOnDate(date)
    }

    private fun getResponseBodyJson(response: Response): JSONObject? {
        return response.body?.string()?.let { JSONObject(it) }
    }
}