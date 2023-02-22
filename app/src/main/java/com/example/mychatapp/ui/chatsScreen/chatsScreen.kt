package com.example.chatapp.ui.chatsScreen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.mychatapp.ui.chatsScreen.ChatScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


/**
 * The home view which will contain all the code related to the view for HOME.
 *
 * Here we will show the list of chat messages sent by user.
 * And also give an option to send a message and logout.
 */

@Destination
@Composable
fun ChatsScreen(
//    nav: DestinationsNavigator,
    username : String,
    homeViewModel: ChatScreenViewModel = viewModel()

) {
    val listState = rememberLazyListState()
    LaunchedEffect(homeViewModel.message.size) {
        if (!listState.isScrolledToTheEnd()) {
            val itmIndex = listState.layoutInfo.totalItemsCount - 1
            if (itmIndex >= 0) {
                val lastItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastItem?.let {
                    listState.animateScrollToItem(itmIndex, it.size + it.offset)
                }
            }
        }
    }
    var messages = homeViewModel.message
    var text by remember { mutableStateOf(TextFieldValue("")) }
    homeViewModel.username = username
    var  currentUser : Boolean
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(contentAlignment = Alignment.TopStart) {
            Row(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                Text(
                    text = "Chats",
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 20.dp, 0.dp),
                    color = MaterialTheme.colors.primary,
                    fontSize = 50.sp
                )
            }
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 0.85f, fill = true),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            reverseLayout = false,


        ) {

            items(messages) { message ->
                currentUser = message.from ==  username
                SingleMessage(
                    message = message.content,
                    isCurrentUser = currentUser,
                    username = message.from
                )
            }
        }
        OutlinedTextField(
            value = text,
            onValueChange = {
               text = it
            },
            label = {
                Text(
                    "Type Your Message"
                )
            },
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 1.dp)
                .fillMaxWidth()
                .weight(weight = 0.09f, fill = true),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = {
                        homeViewModel.sendMessage(text.text)

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Button"
                    )
                }
            }
        )
    }
}


@Composable
fun SingleMessage(message: String, isCurrentUser: Boolean, username: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if (isCurrentUser) MaterialTheme.colors.primary else Color.White
    ) {
        Text(
            text = message,
            textAlign =
            if (isCurrentUser)
                TextAlign.End
            else
                TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = if (!isCurrentUser) MaterialTheme.colors.primary else Color.White
        )
    }
    Spacer(modifier = Modifier . height(5.dp))
    if(!isCurrentUser){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ){
            Text(
                text = username,
                textAlign = if (isCurrentUser)
                    TextAlign.End
                else
                    TextAlign.Start,
                modifier = Modifier
                    .padding(start = 20.dp, bottom = 5.dp),
                fontSize = 10.sp

            )
        }
    }


}

fun LazyListState.isScrolledToTheEnd() : Boolean {
    val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastItem == null || lastItem.size + lastItem.offset <= layoutInfo.viewportEndOffset
}