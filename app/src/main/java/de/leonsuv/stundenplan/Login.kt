package de.leonsuv.stundenplan

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stundenplan.R
import de.leonsuv.stundenplan.model.UserData
import de.leonsuv.stundenplan.ui.theme.StundenplanTheme
import de.leonsuv.stundenplan.wrapper.ApiWrapper


class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setze die Richtlinien für den Thread-Modus, um Netzwerkanfragen zuzulassen (für Testzwecke)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
        // Setze den Inhalt des Bildschirms auf das LoginScreen-Layout
        setContent {
            LoginScreen()
        }
    }
}

@Preview
@Composable
// Funktion zum Erstellen des Login-Bildschirms
fun LoginScreen() {
    // Kontext abrufen
    val context = LocalContext.current
    // Zustände für Benutzername und Passwort verfolgen
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // Zustand für Passwortfehler verfolgen
    var passwordError by remember { mutableStateOf(false) }

    // Thema anwenden
    StundenplanTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                // Abstand
                Spacer(modifier = Modifier.height(15.dp))
                // Logo anzeigen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(275.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_phwt),
                        contentDescription = ""
                    )
                }
                // Abstand
                Spacer(modifier = Modifier.height(15.dp))
                // Anmeldeformular anzeigen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        // Benutzername-Eingabefeld anzeigen
                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            placeholder = { Text("Nutzername") }
                        )
                        // Abstand
                        Spacer(modifier = Modifier.height(5.dp))
                        // Passwort-Eingabefeld anzeigen
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("Passwort") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            isError = passwordError,
                            supportingText = { if (passwordError) Text("Passwort falsch") }
                        )
                    }
                }
                // Anmeldebutton anzeigen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = {
                        // Erzeuge UserData-Objekt mit Benutzername und Passwort
                        val user = UserData(username, password)
                        // Erzeuge ApiWrapper-Objekt mit dem UserData-Objekt
                        val api = ApiWrapper(user)
                        // Führe den Anmeldevorgang aus und überprüfe das Ergebnis
                        if (!api.login()) {
                            passwordError = true
                            return@Button
                        }
                        // Navigiere zur MainActivity und übergebe das Base64-Token
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("base64", user.getBase64())
                        context.startActivity(intent)
                    }) {
                        Text("Anmelden")
                    }
                }
            }
        }
    }
}
