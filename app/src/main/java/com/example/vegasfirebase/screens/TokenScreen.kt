package com.example.vegasfirebase.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vegasfirebase.MainActivity
import com.example.vegasfirebase.R
import com.example.vegasfirebase.viewModel
import com.google.firebase.messaging.FirebaseMessaging

@Composable
fun TokenScreen(
) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    )
    {
        Text(text = viewModel.token.value,
            fontSize = 22.sp,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}

@Composable
fun MainActivity.BottomBarToken() {

    if (viewModel.token.value.isEmpty()) {
        androidx.compose.material3.Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                FirebaseMessaging.getInstance().getToken().addOnSuccessListener(
                    {
                        viewModel.token.value = it
                        Log.d("fcm", "FCM token <${it}>")
                    })
            }
        ) {
            androidx.compose.material3.Text(text = stringResource(R.string.get_token))
        }
    } else {
        androidx.compose.material3.Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.token.value = ""
            }
        ) {
            androidx.compose.material3.Text(text = stringResource(R.string.clear))
        }
    }
}
