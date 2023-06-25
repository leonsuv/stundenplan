package de.leonsuv.stundenplan.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.leonsuv.stundenplan.model.UserData
import de.leonsuv.stundenplan.wrapper.ApiWrapper

@Preview
@Composable
fun PrevContactsScreen() {
    ContactsScreen(padding = PaddingValues(0.dp, 20.dp, 0.dp, 20.dp), ApiWrapper(UserData()))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(padding: PaddingValues, api: ApiWrapper) {
    //val showDialog = remember { mutableStateOf(false) }
    //val cardSelected = remember { mutableIntStateOf(0) }
    val jsonArray = api.getContacts()

    val cardTitles = mutableListOf<String>()
    val names = mutableListOf<String>()
    val emails = mutableListOf<String>()
    val organisations = mutableListOf<String>()
    val telefonzentrales = mutableListOf<String>()
    val orts = mutableListOf<String>()

    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        val cardTitle: String? = jsonObject.optString("CardTitle")
        val informationObject = jsonObject.optJSONObject("information")

        val name: String? = informationObject?.optString("Name:")
        val email: String? = informationObject?.optString("Email:")
        val organisation: String? = informationObject?.optString("Organisation:")
        var telefonzentrale: String? = informationObject?.optString("Telefonzentrale:")
        if (telefonzentrale.isNullOrEmpty()) {
            telefonzentrale = informationObject?.optString("Telefon:")
        }
        val ort: String? = informationObject?.optString("Ort:")

        cardTitles.add(cardTitle ?: "")
        names.add(name ?: "")
        emails.add(email ?: "")
        organisations.add(organisation ?: "")
        telefonzentrales.add(telefonzentrale ?: "")
        orts.add(ort ?: "")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(names.size) { index ->
                    Card(
                        onClick = { /* Do something */ },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = cardTitles[index],
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Name: ${names[index]}")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Email: ${emails[index]}")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Organisation: ${organisations[index]}")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Telefonzentrale: ${telefonzentrales[index]}")
                            if (orts[index].isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Ort: ${orts[index]}")
                            }
                        }
                    }
                }
            }
        }
    }
}