package com.example.c36b.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.c36b.R
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.Black.toArgb()))
        setContent {
            LoginBody()
        }
    }
}

@Composable
fun LoginBody(){
    val context = LocalContext.current
    val activity = context as? Activity
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    Scaffold {
            innerPadding->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(R.drawable.background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize())
            LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Image(painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier.width(150.dp).height(150.dp)
                            .padding(0.dp,10.dp,0.dp,0.dp))
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(text = "BookReviews",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        ))
                    Spacer(modifier = Modifier.height(150.dp))
                }
                item {
                    Text(
                        "Sign in to continue",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 30.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(30.dp))

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        textStyle = TextStyle(color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedPlaceholderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.Red
                        ),

                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        placeholder = {
                            Text(text = "Email")
                        },
                        value = email,
                        onValueChange = { input ->
                            email = input
                        }
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedPlaceholderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.Red
                        ),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        placeholder = {
                            Text(text = "Password")
                        },
                        value = password,
                        onValueChange = { input ->
                            password = input
                        }
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    OutlinedButton(
                        onClick = {
                            isLoading = true
                            // Firebase login logic
                            val auth = FirebaseAuth.getInstance()
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        // Save login flag to SharedPreferences
                                        val sharedPreferences = context.getSharedPreferences("User", android.content.Context.MODE_PRIVATE)
                                        sharedPreferences.edit().apply {
                                            putBoolean("isLoggedIn", true)
                                            apply()
                                        }
                                        // Navigate to main screen
                                        val intent = Intent(context, NavigationActivity::class.java)
                                        context.startActivity(intent)
                                        activity?.finish()
                                    } else {
                                        Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        },
                        modifier = Modifier.height(50.dp).width(200.dp),
                        colors = if (isLoading) ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White)
                                 else ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)
                    ) {
                        Text(text = if (isLoading) "Logging in..." else "Login")
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        "Forgot Password",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        "Don't have an account, Signup",
                        style = TextStyle(
                            color = Color.White
                        ),
                        modifier = Modifier.clickable {
                            val intent = Intent(context, RegistrationActivity::class.java)
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }

    }
}

@Composable
@Preview(showBackground = true)
fun LoginPreview(){
    com.example.c36b.view.pages.ui.theme.C36BTheme {
        LoginBody()
    }
}