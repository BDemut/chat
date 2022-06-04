package com.defconapplications.czat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.defconapplications.czat.login.LoginActivity
import com.defconapplications.czat.ui.theme.MainTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModel()

    val loginLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.username = result.data?.getStringExtra(USER_NAME_KEY)
            getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putString(USER_NAME_KEY, viewModel.username)
                .commit()
        } else {
            finishAndRemoveTask()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userName = getPreferences(Context.MODE_PRIVATE)
            .getString(USER_NAME_KEY, null)

        if (userName == null) {
            loginLauncher.launch(
                Intent(this, LoginActivity::class.java)
            )
        } else {
            viewModel.username = userName
        }

        setContent {
            MainTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = { Appbar() },
                        content = {  }
                    )
                }
            }
        }
    }

    @Composable
    fun Appbar() {
        TopAppBar (
            title = { Text(("Welcome to the chat, " + (viewModel.username ?: ""))) }
        )
    }

    companion object {
        const val USER_NAME_KEY = "user"
    }
}