package com.example.chatapp.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation


var pass: String? = null
@Composable
fun PasswordTextField()  {
    val password = rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val passwordVisibility = remember { mutableStateOf(false) }

        val icon = if (passwordVisibility.value)
            painterResource(id = com.example.mychatapp.R.drawable.baseline_visibility_24)
        else
            painterResource(id = com.example.mychatapp.R.drawable.baseline_visibility_off_24)

        OutlinedTextField(
            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
            value = password.value,
            onValueChange = { password.value = it; pass = password.value},
            label = { Text("Password")
            },
            colors = TextFieldDefaults.textFieldColors(unfocusedLabelColor = MaterialTheme.colors.surface,
                focusedLabelColor = MaterialTheme.colors.surface, disabledLabelColor = MaterialTheme.colors.surface, errorLabelColor = Color.Red )
            , trailingIcon = { IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value })
            {
                Icon(
                    painter = icon,
                    contentDescription = "Visibility Icon"
                )
            }

            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None
            else PasswordVisualTransformation()
        )

    }
}