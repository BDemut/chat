package com.defconapplications.czat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.defconapplications.czat.api.Message
import com.defconapplications.czat.login.LoginActivity
import com.defconapplications.czat.ui.theme.MainTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModel()

    val loginLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.username = result.data?.getStringExtra(USER_NAME_KEY) ?: ""
            if (viewModel.username.isNotBlank()) {
                getPreferences(Context.MODE_PRIVATE)
                    .edit()
                    .putString(USER_NAME_KEY, viewModel.username)
                    .commit()
            } else {
                finishAndRemoveTask()
            }
        } else {
            finishAndRemoveTask()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        val userName = getPreferences(Context.MODE_PRIVATE)
            .getString(USER_NAME_KEY, "")!!

        if (userName.isBlank()) {
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
                        content = { Content() }
                    )
                }
            }
        }
    }

    @Composable
    fun Appbar() {
        TopAppBar (
            title = { Text(("Welcome to the chat, " + (viewModel.username))) }
        )
    }

    @Composable
    fun Content() {
        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
                ) {
            MessageList(messages = viewModel.messages)
            Input()
        }
    }

    @Composable
    fun Input() {
        Row (
            modifier = Modifier.padding(all = 4.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
                ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = viewModel.currentMessage,
                onValueChange = { viewModel.currentMessage = it },
                maxLines = 1
            )
            Button(
                modifier = Modifier
                    .height(48.dp)
                    .aspectRatio(1f)
                    .padding(start = 4.dp),
                onClick = {
                    viewModel.send()
                }) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send"
                )
            }
        }
    }

    @Composable
    fun MessageList(messages: List<Message>) {
        LazyColumn (
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()
        ) {
            items(messages) { message ->
                MessageItem(message)
            }
        }
    }

    @Composable
    fun MessageItem(msg: Message) {
        Column(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            horizontalAlignment = msg.getAlignment()
        ) {
            Text(
                text = msg.username,
                color = MaterialTheme.colors.secondary,
                style = MaterialTheme.typography.subtitle2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp) {
                Text(
                    text = msg.message,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }

    private fun Message.getAlignment() = if (username == viewModel.username) {
        Alignment.End
    } else {
        Alignment.Start
    }

    companion object {
        const val USER_NAME_KEY = "user"
    }
}