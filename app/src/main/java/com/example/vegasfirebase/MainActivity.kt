package com.example.vegasfirebase

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vegasfirebase.screens.BottomBarHome
import com.example.vegasfirebase.screens.BottomBarNotification
import com.example.vegasfirebase.screens.BottomBarSettings
import com.example.vegasfirebase.screens.BottomBarToken
import com.example.vegasfirebase.ui.theme.VegasFirebaseTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

val tabs = listOf(
    TabItem.Home,
    TabItem.Token,
    TabItem.Notification,
    TabItem.Settings,
)

val tabs_init = listOf(
    TabItem.Home,
)

sealed class AppFunction(var run: () -> Unit) {

    object putPreferences : AppFunction( {} )
}

val basePermissions = arrayOf(
    Manifest.permission.INTERNET,
    Manifest.permission.ACCESS_NETWORK_STATE,
    Manifest.permission.ACCESS_NOTIFICATION_POLICY,
    Manifest.permission.RECEIVE_BOOT_COMPLETED,
    Manifest.permission.WAKE_LOCK,
)

lateinit var viewModel: VegasViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var INTERNET: MutableState<Boolean>
    lateinit var ACCESS_NETWORK_STATE: MutableState<Boolean>
    lateinit var WAKE_LOCK: MutableState<Boolean>
    lateinit var ACCESS_NOTIFICATION_POLICY: MutableState<Boolean>
    lateinit var RECEIVE_BOOT_COMPLETED: MutableState<Boolean>

    lateinit var prefs: SharedPreferences
    val APP_PREFERENCES_THEME = "theme"
    val APP_PREFERENCES_ASK_TO_EXIT_FROM_APP = "ask_to_exit_from_app"
    val APP_PREFERENCES_KEEP_SCREEN_ON = "keep_screen_on"

    lateinit var permissionsGranted: MutableState<Boolean>

    var isAppInited: Boolean = false
    var isFistStart: Boolean = true

    @OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        AppFunction.putPreferences.run = ::putPreferences

        setContent {
            viewModel = hiltViewModel()

            getPreferences()

            INTERNET = remember { mutableStateOf(false) }
            ACCESS_NETWORK_STATE = remember { mutableStateOf(false) }
            //READ_EXTERNAL_STORAGE = remember { mutableStateOf(false) }
            //WRITE_EXTERNAL_STORAGE = remember { mutableStateOf(false) }
            WAKE_LOCK = remember { mutableStateOf(false) }
            ACCESS_NOTIFICATION_POLICY = remember { mutableStateOf(false) }
            RECEIVE_BOOT_COMPLETED = remember { mutableStateOf(false) }

            permissionsGranted = remember { mutableStateOf(hasAllPermissions()) }

            if (viewModel.keepScreenOn.value) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }

            isAppInited = true

            if (isFistStart) {
                if (permissionsGranted.value) {


                    isFistStart = false
                }
            }

            VegasFirebaseTheme(viewModel.currentTheme.value == 1) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var pagerState: PagerState = rememberPagerState(0)

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                backgroundColor = MaterialTheme.colorScheme.background,
                                title = {
                                    Row() {
                                        Text(
                                            text = stringResource(R.string.app_name),
                                        )
                                    }

                                },
                                modifier = Modifier.height(30.dp),
                                actions = {
                                    IconButton(onClick = {
                                        exitFromApp()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.ExitToApp,
                                            contentDescription = "Exit",
                                        )
                                    }
                                },
                            )
                        },
                        bottomBar = {
                            BottomAppBar(
                                modifier = Modifier.height(60.dp),
                                backgroundColor = MaterialTheme.colorScheme.background,
                            )
                            {
                                when (tabs[pagerState.currentPage]) {
                                    TabItem.Home -> BottomBarHome()
                                    TabItem.Token -> BottomBarToken()
                                    TabItem.Notification -> BottomBarNotification()
                                    TabItem.Settings -> BottomBarSettings()
                                    else -> {}
                                }
                            }
                        }
                    ) {
                            padding ->
                        Column(modifier = Modifier
                            .padding(padding)
                            //.navigationBarsWithImePadding(),
                        ) {
                            Tabs(tabs = if (permissionsGranted.value) tabs else tabs_init, pagerState = pagerState)
                            TabsContent(
                                tabs = if (permissionsGranted.value) tabs else tabs_init,
                                pagerState = pagerState,
                                permissionsGranted.value,
                                INTERNET.value,
                                ACCESS_NETWORK_STATE.value,
                                WAKE_LOCK.value,
                                ACCESS_NOTIFICATION_POLICY.value,
                                RECEIVE_BOOT_COMPLETED.value,
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Tabs(tabs: List<TabItem>, pagerState: PagerState) {
        val scope = rememberCoroutineScope()
        ScrollableTabRow(
            backgroundColor = MaterialTheme.colorScheme.background,
            selectedTabIndex = pagerState.currentPage,
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    text = { Text(stringResource(tab.title)) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun TabsContent(
        tabs: List<TabItem>, pagerState: PagerState,
        permissionsGranted: Boolean,
        INTERNET: Boolean,
        ACCESS_NETWORK_STATE: Boolean,
        WAKE_LOCK: Boolean,
        ACCESS_NOTIFICATION_POLICY: Boolean,
        RECEIVE_BOOT_COMPLETED: Boolean,
        //READ_EXTERNAL_STORAGE: Boolean,
        //WRITE_EXTERNAL_STORAGE: Boolean,
    ) {
        HorizontalPager(state = pagerState, count = tabs.size) { page ->

            var screenParams: ScreenParams = ScreenParams(
                permissionsGranted,
                INTERNET,
                ACCESS_NETWORK_STATE,
                WAKE_LOCK,
                ACCESS_NOTIFICATION_POLICY,
                RECEIVE_BOOT_COMPLETED,
            )

            tabs[page].screen(screenParams)

        }
    }

    override fun onBackPressed() {

        if (viewModel.askToExitFromApp) {

            val alertDialog = android.app.AlertDialog.Builder(this)

            alertDialog.apply {
                setIcon(R.drawable.vegas_03)
                setTitle(getApplicationContext().getResources().getString(R.string.app_name))
                setMessage(getApplicationContext().getResources().getString(R.string.do_you_really_want_to_close_the_application))
                setPositiveButton(getApplicationContext().getResources().getString(R.string.yes))
                { _: DialogInterface?, _: Int -> exitFromApp() }
                setNegativeButton(getApplicationContext().getResources().getString(R.string.no))
                { _, _ -> }

            }.create().show()
        }
        else {
            exitFromApp()
        }

    }

    fun exitFromApp() {
        onBackPressedDispatcher.onBackPressed()
    }

    fun hasAllPermissions(): Boolean{
        var result = true

        if (!hasBasePermissions()) {
            result = false
        }

        return result
    }

    fun hasBasePermissions(): Boolean{
        var result = true
        basePermissions.forEach {

            val permission = ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            if ( !permission)
            {
                result = false
            }
            when (it) {
                Manifest.permission.INTERNET -> INTERNET.value = permission
                Manifest.permission.ACCESS_NETWORK_STATE -> ACCESS_NETWORK_STATE.value = permission
                Manifest.permission.WAKE_LOCK -> WAKE_LOCK.value = permission
                Manifest.permission.ACCESS_NOTIFICATION_POLICY -> ACCESS_NOTIFICATION_POLICY.value = permission
                Manifest.permission.RECEIVE_BOOT_COMPLETED -> RECEIVE_BOOT_COMPLETED.value = permission
            }
        }
        return result
    }

    fun requestBasePermissions() {
        ActivityCompat.requestPermissions(this, basePermissions,101)
    }

    fun putPreferences() {
        val editor = prefs.edit()
        editor.putInt(APP_PREFERENCES_THEME, viewModel.currentTheme.value).apply()
        editor.putBoolean(APP_PREFERENCES_ASK_TO_EXIT_FROM_APP, viewModel.askToExitFromApp).apply()
        editor.putBoolean(APP_PREFERENCES_KEEP_SCREEN_ON, viewModel.keepScreenOn.value).apply()
    }

    fun getPreferences() {
        if(prefs.contains(APP_PREFERENCES_THEME)){
            viewModel.currentTheme.value = prefs.getInt(APP_PREFERENCES_THEME, 0)
        }
        if(prefs.contains(APP_PREFERENCES_ASK_TO_EXIT_FROM_APP)){
            viewModel.askToExitFromApp = prefs.getBoolean(APP_PREFERENCES_ASK_TO_EXIT_FROM_APP, true)
        }

        if(prefs.contains(APP_PREFERENCES_KEEP_SCREEN_ON)){
            viewModel.keepScreenOn.value = prefs.getBoolean(APP_PREFERENCES_KEEP_SCREEN_ON, true)
        }
    }

    override fun onStart() {
        super.onStart()
        if (isAppInited) {
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (isAppInited) {
            permissionsGranted.value = hasAllPermissions()
        }
    }

}

