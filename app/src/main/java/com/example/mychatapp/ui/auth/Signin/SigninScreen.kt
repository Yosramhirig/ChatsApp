package com.example.chatapp.ui.auth.Signin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.ui.destinations.ChatsScreenDestination
import com.example.chatapp.ui.destinations.SignupScreenDestination


import com.example.chatapp.util.PasswordTextField
import com.example.chatapp.util.pass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


private lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Auth

@Destination(start = true)
@Composable
fun SigninScreen(
    nav: DestinationsNavigator,
) {
    val context = LocalContext.current
    auth = Firebase.auth
    var Invalid : Boolean = false
    val username = remember { mutableStateOf(TextFieldValue()) }
    var password : String? = null
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = com.example.mychatapp.R.drawable.rectangle_8),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopCenter
            )
    ){

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp),
                shape = RoundedCornerShape(60.dp , 0.dp, 0.dp, 0.dp),
                backgroundColor = Color.White,
                elevation = 4.dp
                ){
                Box(){
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp, bottom = 30.dp)
                    , horizontalArrangement = Arrangement.Center,

                    ){
                        Text(text = "Log In",
                            fontSize = 36 .sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                            ,modifier = Modifier
                                )
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 110.dp)
                        , horizontalArrangement = Arrangement.Center,

                        ){
                        OutlinedTextField(

                       leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = null) },
                        value = username.value,
                        onValueChange = { username.value = it},
                        label = { Text("username")
                        },
                            colors = TextFieldDefaults.textFieldColors(unfocusedLabelColor = MaterialTheme.colors.surface,
                                focusedLabelColor = MaterialTheme.colors.surface, disabledLabelColor = MaterialTheme.colors.surface, errorLabelColor = Color.Red )

                    )
            }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 190.dp)
                        , horizontalArrangement = Arrangement.Center,

                        ){
                         PasswordTextField()
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 255.dp, start = 160.dp)
                        , horizontalArrangement = Arrangement.Center,

                        ){
                        Text(text = "Forgot Password", color = MaterialTheme.colors.primary, fontSize = 14 .sp
                            ,modifier = Modifier .clickable {  })
                    }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 400.dp)
                        , horizontalArrangement = Arrangement.Center,

                        ){
                        Button(onClick = {

                                if(username.value.text.isEmpty() || pass!!.isEmpty()) {

                                    Toast.makeText(
                                        context,
                                        "Please enter the username & pass",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }else {

                                    auth.signInWithEmailAndPassword(username.value.text, pass!!)
                                        .addOnCompleteListener() { task ->
                                            if (task.isSuccessful) {
                                                nav.navigate(ChatsScreenDestination(username.value.text))
                                                Toast.makeText(
                                                    context, "you're in",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                            } else {
                                                task.exception?.printStackTrace()
                                                Toast.makeText(
                                                    context, ">:(",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }

                                }},
                        modifier = Modifier .width(300.dp) .height(50.dp)) {
                            Text(text = "Login", fontSize = 20.sp)
                        }
                    }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 470.dp)
                        , horizontalArrangement = Arrangement.Center,

                        ){
                        Text(text = "Don't have account? ", color = Color.Black, fontSize = 14 .sp)
                        Text(text = "Sign up here", color = MaterialTheme.colors.primary, fontSize = 14 .sp
                            ,modifier = Modifier .clickable { nav.navigate(SignupScreenDestination) })
                    }
                    
            }}



       }}




