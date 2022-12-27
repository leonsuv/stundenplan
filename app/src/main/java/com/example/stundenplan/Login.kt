package com.example.stundenplan

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stundenplan.ui.theme.StundenplanTheme

class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    StundenplanTheme {
        Surface(modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background) {
            Column {
                Spacer(modifier = Modifier.height(15.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                    contentAlignment = Alignment.Center) {
                    Image(painter = painterResource(id = R.drawable.logo_phwt),
                        contentDescription = "")
                }
                Spacer(modifier = Modifier.height(15.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                    contentAlignment = Alignment.Center) {
                    Column {
                        TextField(value = username, onValueChange = { username = it })
                        Spacer(modifier = Modifier.height(5.dp))
                        TextField(value = password, onValueChange = { password = it })
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                    contentAlignment = Alignment.Center) {
                    Button(onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("username", username)
                        intent.putExtra("password", password)
                        context.startActivity(intent)
                    }) {
                        Text("Anmelden")
                    }
                }
            }
        }
    }
}
