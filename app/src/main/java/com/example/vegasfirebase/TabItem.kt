package com.example.vegasfirebase

import androidx.compose.runtime.Composable
import com.example.vegasfirebase.screens.HomeScreen
import com.example.vegasfirebase.screens.NotificationScreen
import com.example.vegasfirebase.screens.SettingsScreen
import com.example.vegasfirebase.screens.TokenScreen

data class ScreenParams(
    val permissionsGranted: Boolean,
    val INTERNET: Boolean,
    val ACCESS_NETWORK_STATE: Boolean,
    val WAKE_LOCK: Boolean,
    val ACCESS_NOTIFICATION_POLICY: Boolean,
    val RECEIVE_BOOT_COMPLETED: Boolean,

)
typealias ComposableFun = @Composable (screenParams: ScreenParams) -> Unit

sealed class TabItem(var icon: Int, var title: Int, var screen: ComposableFun) {
    object Home : TabItem(R.drawable.ic_label, R.string.tab_name_home, { HomeScreen(
        it.permissionsGranted,
        it.INTERNET,
        it.ACCESS_NETWORK_STATE,
        it.WAKE_LOCK,
        it.ACCESS_NOTIFICATION_POLICY,
        it.RECEIVE_BOOT_COMPLETED,
    ) } )
    object Token : TabItem(R.drawable.ic_label, R.string.tab_name_token, { TokenScreen() } )
    object Notification : TabItem(R.drawable.ic_label, R.string.tab_name_notification, { NotificationScreen() } )
    object Settings : TabItem(R.drawable.ic_label, R.string.tab_name_settings, { SettingsScreen() } )
}