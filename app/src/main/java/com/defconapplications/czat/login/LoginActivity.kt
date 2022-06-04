package com.defconapplications.czat.login

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.defconapplications.czat.MainActivity.Companion.USER_NAME_KEY
import com.defconapplications.czat.ui.theme.MainTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : ComponentActivity() {

    val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContent {
            MainTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = { Appbar() },
                        content = { LoginScreen() })
                }
            }
        }
    }

    private fun onNameSubmitted() {
        val intent = Intent()
        intent.putExtra(USER_NAME_KEY, viewModel.username)
        setResult(RESULT_OK, intent)
        finishAndRemoveTask()
    }

    @Composable
    fun LoginScreen() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                label = { Text("Your name") }
            )
            Button(onClick = { onNameSubmitted() }) {
                Text("Submit")
            }
        }
    }

    @Composable
    fun Appbar() {
        TopAppBar (
            title = { Text("Input your name") }
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MainTheme {
            LoginScreen()
        }
    }
}