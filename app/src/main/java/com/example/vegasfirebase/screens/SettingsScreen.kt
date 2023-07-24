package com.example.vegasfirebase.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vegasfirebase.AppFunction
import com.example.vegasfirebase.MainActivity
import com.example.vegasfirebase.R
import com.example.vegasfirebase.viewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopCenter)
    ) {
        val theme = listOf(stringResource(R.string.light), stringResource(R.string.dark))

        val (selectedThemeOption, onThemeOptionSelected) = remember { mutableStateOf(viewModel.currentTheme.value) }
        val checkedStateExitFromApp = remember { mutableStateOf(viewModel.askToExitFromApp) }
        val checkedStateKeepScreenOn = remember { mutableStateOf(viewModel.keepScreenOn.value) }

        Column(
            modifier = Modifier.padding(0.dp, 16.dp),
        ) {

            androidx.compose.material3.Text(
                text = "${stringResource(R.string.theme)}:",
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Row(
                Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .height(56.dp), verticalAlignment = Alignment.CenterVertically
            ) {

                for (theme_index in 0..theme.lastIndex) {
                    androidx.compose.material3.RadioButton(
                        selected = (theme_index == selectedThemeOption),
                        onClick = {
                            onThemeOptionSelected(theme_index)
                            viewModel.currentTheme.value = theme_index
                            AppFunction.putPreferences.run()
                        },
                        colors = androidx.compose.material3.RadioButtonDefaults.colors(),

                        )

                    ClickableText(
                        text = AnnotatedString(theme[theme_index]),
                        style = TextStyle(
                            fontSize = 18.sp, color = MaterialTheme.colorScheme.primary,
                        ),
                        onClick = {
                            onThemeOptionSelected(theme_index)
                            viewModel.currentTheme.value = theme_index
                            AppFunction.putPreferences.run()
                        }
                    )
                }

            }


            Row {
                androidx.compose.material3.Checkbox(
                    checked = checkedStateExitFromApp.value,
                    onCheckedChange = {
                        checkedStateExitFromApp.value = it
                        viewModel.askToExitFromApp = it
                        AppFunction.putPreferences.run()
                    },
                    colors = androidx.compose.material3.CheckboxDefaults.colors(),
                    modifier = Modifier.padding(10.dp),
                )
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.ask_confirmation_to_exit_from_app)),
                    modifier = Modifier.padding(0.dp, 20.dp),
                    style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 18.sp),
                    onClick = { //offset ->
                        checkedStateExitFromApp.value = !checkedStateExitFromApp.value
                        viewModel.askToExitFromApp = !viewModel.askToExitFromApp
                        AppFunction.putPreferences.run()
                    }
                )
            }

            Row {
                androidx.compose.material3.Checkbox(
                    checked = checkedStateKeepScreenOn.value,
                    onCheckedChange = {
                        checkedStateKeepScreenOn.value = it
                        viewModel.keepScreenOn.value = it
                        AppFunction.putPreferences.run()
                    },
                    colors = androidx.compose.material3.CheckboxDefaults.colors(),
                    modifier = Modifier.padding(10.dp),
                )
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.keep_screen_on)),
                    modifier = Modifier.padding(0.dp, 20.dp),
                    style = TextStyle(color = MaterialTheme.colorScheme.primary, fontSize = 18.sp),
                    onClick = { //offset ->
                        checkedStateKeepScreenOn.value = !checkedStateKeepScreenOn.value
                        viewModel.keepScreenOn.value = !viewModel.keepScreenOn.value
                        AppFunction.putPreferences.run()
                    }
                )
            }

        }

    }
}

@Composable
fun MainActivity.BottomBarSettings() {

    androidx.compose.material3.Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {

            val intent = Intent()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            } else {
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.parse("package:$packageName")
            }
            startActivity(intent)

        }
    ) {
        androidx.compose.material3.Text(text = stringResource(R.string.push_notify_prefs))
    }
}