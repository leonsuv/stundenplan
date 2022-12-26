package com.example.stundenplan

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stundenplan.ui.theme.StundenplanTheme
import com.example.stundenplan.wrapper.ApiWrapper
import com.example.stundenplan.wrapper.UserData


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder()
            .permitAll().build())

        val api = ApiWrapper(UserData(resources.getString(R.string.username), resources.getString(R.string.password)))

        setContent {
            StundenplanTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Row {
                        Box(modifier = Modifier
                            .width(110.dp)
                            .height(50.dp)) {
                            Greeting("Android")
                        }
                        Spacer(modifier = Modifier.width(1.dp))
                        Box(modifier = Modifier
                            .width(110.dp)
                            .height(50.dp)) {
                            ButtonLogin(api = api)
                        }
                        Spacer(modifier = Modifier.width(1.dp))
                        Box(modifier = Modifier
                            .width(110.dp)
                            .height(50.dp)) {
                            ButtonGetEvents(api = api)
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StundenplanTheme {
        Greeting("Android")
    }
}

@Composable
fun ButtonLogin(api: ApiWrapper) {
    Button(onClick = api::login) {
        Text(text = "Login")
    }
}

@Composable
fun ButtonGetEvents(api: ApiWrapper) {
    Button(onClick = api::getEvents) {
        Text(text = "Events")
    }
}