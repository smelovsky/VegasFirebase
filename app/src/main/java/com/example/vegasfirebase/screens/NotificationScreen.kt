package com.example.vegasfirebase.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vegasfirebase.MainActivity
import com.example.vegasfirebase.R
import com.example.vegasfirebase.viewModel

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NotificationScreen(
) {
    var textFieldValueTitle: TextFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = viewModel.title
            )
        )
    }

    var textFieldValueBody: TextFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = viewModel.body
            )
        )
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column () {
        Text(
            text = "Request to FCM",
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.size(10.dp))

        TextField(
            label = { Text("Title:") },
            value = textFieldValueTitle,
            onValueChange = {
                    newValue -> textFieldValueTitle = newValue
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
            ),
            singleLine = true,
            //textStyle = TextStyle(fontSize = 16.sp ),
            textStyle = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),

            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.title = textFieldValueTitle.text
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
        )

        TextField(
            label = { Text("Body:") },
            value = textFieldValueBody,
            onValueChange = {
                    newValue -> textFieldValueBody = newValue
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
            ),
            singleLine = true,
            //textStyle = TextStyle(fontSize = 16.sp ),
            textStyle = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),

            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.body = textFieldValueTitle.text
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
        )
        
        Spacer(modifier = Modifier.size(30.dp))

        Text(
            text = "Responds from FCM",
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = "Title: ${viewModel.notification_title.value}",
            fontSize = 22.sp,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Text(
            text = "Body: ${viewModel.notification_body.value}",
            fontSize = 22.sp,
            modifier = Modifier.padding(horizontal = 10.dp)
        )

    }

}

@Composable
fun MainActivity.BottomBarNotification() {

    androidx.compose.material3.Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            viewModel.fmcRequest()
        }
    ) {
        androidx.compose.material3.Text(text = stringResource(R.string.send))
    }
}
