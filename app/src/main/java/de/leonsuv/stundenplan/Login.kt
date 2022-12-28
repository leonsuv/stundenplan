package de.leonsuv.stundenplan

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
        setContent {
            LoginScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LoginScreen() {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    StundenplanTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Spacer(modifier = Modifier.height(15.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_phwt),
                        contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        TextField(value = username,
                            onValueChange = { username = it },
                            placeholder = { Text("Nutzername") }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        TextField(value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("Passwort") },
                            visualTransformation = PasswordVisualTransformation(),
                            isError = passwordError,
                            supportingText = { if (passwordError) Text("Passwort falsch") }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = {
                        val user = UserData(username, password)
                        val api = ApiWrapper(user)
                        if (!api.login()) {
                            passwordError = true
                            return@Button
                        }
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
