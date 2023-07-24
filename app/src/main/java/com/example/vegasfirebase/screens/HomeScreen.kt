package com.example.vegasfirebase.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vegasfirebase.R
import com.example.vegasfirebase.MainActivity

@Composable
fun HomeScreen(
    permissionsGranted: Boolean,
    INTERNET: Boolean,
    ACCESS_NETWORK_STATE: Boolean,
    WAKE_LOCK: Boolean,
    ACCESS_NOTIFICATION_POLICY: Boolean,
    RECEIVE_BOOT_COMPLETED: Boolean,
) {
    Column() {
        Image(
            painterResource(R.drawable.vegas_02),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
    if (!permissionsGranted) {
        Column() {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "INTERNET",
                color = if (INTERNET) Color.Green else Color.Red,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(
                text = "ACCESS_NETWORK_STATE",
                color = if (ACCESS_NETWORK_STATE) Color.Green else Color.Red,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(
                text = "WAKE_LOCK",
                color = if (WAKE_LOCK) Color.Green else Color.Red,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(
                text = "ACCESS_NOTIFICATION_POLICY",
                color = if (ACCESS_NOTIFICATION_POLICY) Color.Green else Color.Red,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Text(
                text = "RECEIVE_BOOT_COMPLETED",
                color = if (RECEIVE_BOOT_COMPLETED) Color.Green else Color.Red,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

@Composable
fun MainActivity.BottomBarHome() {

    if (!permissionsGranted.value) {
        androidx.compose.material3.Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (!hasBasePermissions()) {
                    requestBasePermissions()
                }
            }

        ) {
            androidx.compose.material3.Text(text = stringResource(R.string.permissions))
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.all_permissions_granted) )
        }
    }


}
