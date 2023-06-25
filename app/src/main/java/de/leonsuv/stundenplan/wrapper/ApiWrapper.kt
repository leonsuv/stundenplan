package de.leonsuv.stundenplan.wrapper

import de.leonsuv.stundenplan.model.EventData
import de.leonsuv.stundenplan.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ApiWrapper(private val userdata: UserData) {
    // Methode zum Einloggen
    fun login(): Boolean {
        // Erstelle eine Anfrage zum Login
        val request = Request.Builder()
            .method("POST", "".toRequestBody())
            .url("https://app.phwt.de/api/v1/auth/login")
            .header("Authorization", "Basic ${userdata.getBase64()}")
            .build()
        println(userdata.getBase64())
        // Sende die Anfrage ab und verarbeite die Antwort
        OkHttpClient().newCall(request).execute().use { response ->
            val bodyJson = getResponseBodyJson(response)
            try {
                // Extrahiere den Zugriffstoken aus der Antwort und speichere ihn
                bodyJson?.getString("access_token")?.let { userdata.setAccessToken(it) }
                // Extrahiere den Aktualisierungstoken aus der Antwort und speichere ihn
                bodyJson?.getString("refresh_token")?.let { userdata.setRefreshToken(it) }
            } catch (e: JSONException) {
                return false
            }
        }
        // Gib zurück, ob der Benutzer autorisiert ist
        return userdata.isAuthorized()
    }

    // Methode zum Abrufen von Ereignissen
    suspend fun getEvents(date: String): List<EventData.Event>? = withContext(Dispatchers.IO) {
        // Überprüfe, ob der Benutzer autorisiert ist
        if (!userdata.isAuthorized()) {
            return@withContext null
        }
        val eventData: EventData?
        // Erstelle eine Anfrage zum Abrufen von Ereignissen
        val request = Request.Builder()
            .method("GET", null)
            .url("https://app.phwt.de/api/v2/events")
            .header("Authorization", "Bearer ${userdata.getRefreshToken()}")
            .build()

        eventData = withContext(Dispatchers.IO) {
            val response = OkHttpClient().newCall(request).execute()
            response.body?.let { EventData.fromJson(it.string()) }
        }

        // Gib Ereignisse für das angegebene Datum zurück (Testzwecke)
        return@withContext eventData?.getEventsOnDate(date)
    }


    // Hilfsmethode zum Konvertieren des Antwortkörpers in ein JSON-Objekt
    private fun getResponseBodyJson(response: Response): JSONObject? {
        return response.body?.string()?.let { JSONObject(it) }
    }

    fun getContacts(): JSONArray {
        return JSONArray("[\n" +
                "{\n" +
                "\"CardTitle\": \"Präsident\",\n" +
                "\"information\": {\n" +
                "\"Name:\": \"Prof. Dr. Dennis De\",\n" +
                "\"Email:\": \"dennis.de@phwt.de\",\n" +
                "\"Organisation:\": \"Private Hochschule für Wirtschaft und Technik gGmbH\",\n" +
                "\"Telefonzentrale:\": \"04441/915-0\",\n" +
                "\"Ort:\": \"Rombergstraße 40, 49377 Vechta\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"CardTitle\": \"Präsidiumssekretariat/Gleichstellungsbeauftragte\",\n" +
                "\"information\": {\n" +
                "\"Name:\": \"Marion Lammers\",\n" +
                "\"Email:\": \"info@phwt.de\",\n" +
                "\"Organisation:\": \"Private Hochschule für Wirtschaft und Technik gGmbH\",\n" +
                "\"Telefonzentrale:\": \"04441/915-0\",\n" +
                "\"Ort:\": \"Rombergstraße 40, 49377 Vechta\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"CardTitle\": \"Studienbereichsleitung\",\n" +
                "\"information\": {\n" +
                "\"Name:\": \"Prof. Dr. Gabriele Schreieck\",\n" +
                "\"Email:\": \"schreieck@phwt.de\",\n" +
                "\"Telefon:\": \"05441/992-118\",\n" +
                "\"Organisation:\": \"Studienbereich Ingenieurwesen Dr. Jürgen Ulderup\",\n" +
                "\"Telefonzentrale:\": \"05441/992-0\",\n" +
                "\"Ort:\": \"Schlesierstr. 13 a, 49356 Diepholz\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"CardTitle\": \"Studienbereichsleitung\",\n" +
                "\"information\": {\n" +
                "\"Name:\": \"Prof. Dr. Elmar Reucher\",\n" +
                "\"Email:\": \"reucher@phwt.de\",\n" +
                "\"Telefon:\": \"04441/915-202\",\n" +
                "\"Organisation:\": \"Studienbereich Betriebswirtschaft\",\n" +
                "\"Ort:\": \"Rombergstraße 40, 49377 Vechta\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"CardTitle\": \"Studienbereichssekretariat Elektrotechnik und Mechatronik\",\n" +
                "\"information\": {\n" +
                "\"Name:\": \"Kathrin Sanders\",\n" +
                "\"Email:\": \"sanders@phwt.de\",\n" +
                "\"Telefon:\": \"05441/992-250\",\n" +
                "\"Fax:\": \"05441/992-259\",\n" +
                "\"Organisation:\": \"Studienbereich Ingenieurwesen Dr. Jürgen Ulderup\",\n" +
                "\"Telefonzentrale:\": \"05441/992-0\",\n" +
                "\"Ort:\": \"Schlesierstr. 13 a, 49356 Diepholz\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"CardTitle\": \"Studienbereichssekretariat Elektrotechnik und Mechatronik\",\n" +
                "\"information\": {\n" +
                "\"Name:\": \"Tatjana Voropaev\",\n" +
                "\"Email:\": \"voropaev@phwt.de\",\n" +
                "\"Telefon:\": \"05441/992-251\",\n" +
                "\"Fax:\": \"05441/992-259\",\n" +
                "\"Raum:\": \"Thüringer Straße 3a, 49356 Diepholz\",\n" +
                "\"Organisation:\": \"Studienbereich Ingenieurwesen Dr. Jürgen Ulderup\",\n" +
                "\"Telefonzentrale:\": \"05441/992-0\",\n" +
                "\"Ort:\": \"Schlesierstr. 13 a, 49356 Diepholz\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"CardTitle\": \"Veranstaltungsplanung Standort Diepholz\",\n" +
                "\"information\": {\n" +
                "\"Name:\": \"Kathrin Sanders\",\n" +
                "\"Email:\": \"studienplanung-ing@phwt.de\",\n" +
                "\"Telefon:\": \"05441/992-250\",\n" +
                "\"Raum:\": \"Sekretariat im ZME, Thüringerstraße 3a\",\n" +
                "\"Organisation:\": \"Studienbereich Ingenieurwesen Dr. Jürgen Ulderup\",\n" +
                "\"Telefonzentrale:\": \"05441/992-0\",\n" +
                "\"Ort:\": \"Schlesierstr. 13 a, 49356 Diepholz\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"CardTitle\": \"Studienbereichssekretariat Maschinenbau und Wirtschaftsingenieurwesen\",\n" +
                "\"information\": {\n" +
                "\"Name:\": \"Claudia Reddehase\",\n" +
                "\"Email:\": \"reddehase@phwt.de\",\n" +
                "\"Telefon:\": \"05441/992-100\",\n" +
                "\"Fax:\": \"05441/992-109\",\n" +
                "\"Organisation:\": \"Studienbereich Ingenieurwesen Dr. Jürgen Ulderup\",\n" +
                "\"Telefonzentrale:\": \"05441/992-0\",\n" +
                "\"Ort:\": \"Schlesierstr. 13 a, 49356 Diepholz\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"CardTitle\": \"Studienbereichssekretariat Maschinenbau und Wirtschaftsingenieurwesen\",\n" +
                "\"information\": {\n" +
                "\"Name:\": \"Janice Mathea\",\n" +
                "\"Email:\": \"mathea@phwt.de\",\n" +
                "\"Telefon:\": \"05441/992-101\",\n" +
                "\"Fax:\": \"05441/992-109\",\n" +
                "\"Organisation:\": \"Studienbereich Ingenieurwesen Dr. Jürgen Ulderup\",\n" +
                "\"Telefonzentrale:\": \"05441/992-0\",\n" +
                "\"Ort:\": \"Schlesierstr. 13 a, 49356 Diepholz\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"CardTitle\": \"Studienbereichssekretariat Betriebswirtschaft\",\n" +
                "\"information\": {\n" +
                "\"Name:\": \"Petra Lübberding\",\n" +
                "\"Email:\": \"luebberding@phwt.de\",\n" +
                "\"Telefon:\": \"04441/915-121\",\n" +
                "\"Fax:\": \"04441/915-209\",\n" +
                "\"Organisation:\": \"Studienbereich Betriebswirtschaft\",\n" +
                "\"Ort:\": \"Rombergstraße 40, 49377 Vechta\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"CardTitle\": \"Institutsleiter\",\n" +
                "\"information\": {\n" +
                "\"Name:\": \"Prof. Dr.-Ing. Carsten Bye\",\n" +
                "\"Email:\": \"bye@phwt.de\",\n" +
                "\"Telefon:\": \"05441/992-151\",\n" +
                "\"Organisation:\": \"PHWT-Institut\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"CardTitle\": \"Assistenz\",\n" +
                "\"information\": {\n" +
                "\"Name:\": \"Ann-Christin Bajohr\",\n" +
                "\"Email:\": \"bajohr@phwt.de\",\n" +
                "\"Telefon:\": \"05441/992-156\",\n" +
                "\"Fax:\": \"05441/992-159\",\n" +
                "\"Organisation:\": \"PHWT-Institut\"\n" +
                "}\n" +
                "}\n" +
                "]")
    }
}
