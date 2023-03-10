package com.example.chatapp.ui.chatsScreen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import coil.compose.AsyncImage

import com.example.mychatapp.ui.chatsScreen.ChatScreenViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@Destination
@Composable
fun ChatsScreen(
//    nav: DestinationsNavigator,
    username : String,
    homeViewModel: ChatScreenViewModel = viewModel()

) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> imageUri = uri }
    )

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
                message.content?.let {
                    SingleMessage(
                        message = it,
                        isCurrentUser = currentUser,
                        username = message.from,
                        image = imageUri
                    )
                }
            }
        }
        Row(
            verticalAlignment  = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){


        Spacer(modifier = Modifier .width(2.dp))
        IconButton(
            onClick = {
                imageLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                homeViewModel.sendMessage("emptyString",imageUri)
//

            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Message add"
            )
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
                        homeViewModel.sendMessage(text.text,null)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Button"
                    )
                }

            }
        )
    }}
}


@Composable
fun SingleMessage(message: String, isCurrentUser: Boolean, username: String, image: Uri?) {
    if(message != "emptyString") {
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
    }else{
        Card(
            shape = RoundedCornerShape(16.dp),
            backgroundColor = if (isCurrentUser) MaterialTheme.colors.primary else Color.White
        ){
            AsyncImage(
                model = image,
                contentDescription = "image"
            )
        }

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