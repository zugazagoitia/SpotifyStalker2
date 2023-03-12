package com.zugazagoitia.spotifystalker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zugazagoitia.spotifystalker.data.LoginDatasource
import com.zugazagoitia.spotifystalker.data.LoginRepository
import com.zugazagoitia.spotifystalker.model.LoggedInUser
import com.zugazagoitia.spotifystalker.ui.theme.SpotifyStalkerTheme
import com.zugazagoitia.spotifystalker2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.gianlu.librespot.core.Session

class MainActivity : ComponentActivity() {

    var loginRepository: LoginRepository = LoginRepository.getInstance(LoginDatasource(this))!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContent {
            SpotifyStalkerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }


    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginScreen() {

        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        var stayLoggedIn by rememberSaveable { mutableStateOf(true) }

        val snackState = remember { SnackbarHostState() }
        val snackScope = rememberCoroutineScope()

        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        fun launchFriendListActivity() {
            val intent = Intent(this@MainActivity, FriendList::class.java)
            startActivity(intent)
        }

        fun launchSnackBar(message: String) {
            snackScope.launch { snackState.showSnackbar(message, withDismissAction = true) }
        }

        suspend fun login(username: String, password: String, stayLoggedIn: Boolean) {
            //TODO: Add loading indicator
            try {
                loginRepository.login(username, password, stayLoggedIn)
                    .getOrThrow()
                launchFriendListActivity()

            } catch (e: Session.SpotifyAuthenticationException) {
                launchSnackBar("Invalid credentials")
                println("Invalid credentials while logging in")
                println(e)
            } catch (e: Exception) {
                launchSnackBar("Error while logging in")
                println("Error while logging in")
                println(e)
            }
        }

        coroutineScope.launch(Dispatchers.IO) {
            try {
                loginRepository.loginSavedCredentials().getOrThrow()
                launchFriendListActivity()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }




        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackState, Modifier) },
            content = {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        Icons.Filled.PersonSearch,
                        modifier = Modifier
                            .height(250.dp)
                            .width(250.dp),
                        contentDescription = "Logo",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text(stringResource(R.string.username)) },
                        singleLine = true,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(stringResource(R.string.password)) },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            // Please provide localized description for accessibility services
                            val description =
                                if (passwordVisible) stringResource(R.string.hidePassword) else stringResource(
                                    R.string.showPassword
                                )

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, description)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(R.string.stayLoggedIn))
                        Spacer(modifier = Modifier.width(20.dp))
                        Switch(checked = stayLoggedIn, onCheckedChange = { stayLoggedIn = it })
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                login(
                                    username,
                                    password,
                                    stayLoggedIn
                                )
                            }
                        },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(text = stringResource(R.string.login))
                    }

                }
            }
        )


    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        SpotifyStalkerTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                LoginScreen()
            }
        }
    }


}