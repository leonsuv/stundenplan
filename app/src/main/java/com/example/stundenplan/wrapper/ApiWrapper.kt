package com.example.stundenplan.wrapper

import com.example.stundenplan.model.EventData
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ApiWrapper(private val userdata: UserData){

    fun login(): Boolean {
        /* TODO:
         * try login, return true/false (CHECKED)
         * save access and refresh token in userdata (CHECKED)
         */
        val request = Request.Builder()
            .method("POST", "".toRequestBody())
            .url("https://app.phwt.de/api/v1/auth/login")
            .header("Authorization", "Basic ${userdata.getBase64()}")
            .build()
        OkHttpClient().newCall(request).execute().use { response ->
            val bodyJson = getResponseBodyJson(response)
            bodyJson?.getString("access_token")?.let { userdata.setAccessToken(it) }
            bodyJson?.getString("refresh_token")?.let { userdata.setRefreshToken(it) }
        }
        return userdata.isAuthorized()
    }

    fun getEvents() {
        if (!userdata.isAuthorized()) {
            return
        }
        var eventData: EventData?
        val request = Request.Builder()
            .method("GET", null)
            .url("https://app.phwt.de/api/v2/events")
            .header("Authorization", "Basic ${userdata.getRefreshToken()}")
            .build()
        OkHttpClient().newCall(request).execute().use { response ->
//            val bodyJson = getResponseBodyJson(response)
            eventData = response.body?.let { EventData.fromJson(it.string()) }
/*            try {
                println(bodyJson?.toJSONArray(bodyJson.names())?.length())
            } catch (e: JSONException) {
                e.printStackTrace()
            }*/
        }
        println(eventData?.getEventsOnDate("2022-10-04"))
    }

    private fun getResponseBodyJson(response: Response): JSONObject? {
        return response.body?.string()?.let { JSONObject(it) }
    }
}