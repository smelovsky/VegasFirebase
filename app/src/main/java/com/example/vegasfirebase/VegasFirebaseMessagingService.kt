package com.example.vegasfirebase

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.GregorianCalendar

class VegasFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d ("fcm", "onNewToken: " + token)

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val notification = remoteMessage.notification
        val title = notification?.title
        val body = notification?.body
        val channelId = notification?.channelId


        Log.d ("fcm", "onMessageReceived: title " + title)
        Log.d ("fcm", "onMessageReceived: body " + body)
        Log.d ("fcm", "onMessageReceived: channelId " + channelId)

        sendNotification(title, body, applicationContext)
    }

    private fun sendNotification(title: String?,
                                 body: String?,
                                 context: Context
    ) {

        val channelName = "Vegas channel Name"
        val channelId = "Vegas channel ID"

        var notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.btn_star)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.btn_star))
            .setContentTitle(title)
            .setContentText(body)
            .setWhen(GregorianCalendar.getInstance().getTimeInMillis())
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, R.color.holo_red_dark))
            .setChannelId(channelId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH)
        } else {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX)
        }

        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(title)
        bigTextStyle.bigText(body)
        notificationBuilder.setStyle(bigTextStyle)


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = title
            }
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(55 /* ID of notification */, notificationBuilder.build())

        if (body != null) {
            viewModel.notification_title.value = body
        }
        if (title != null) {
            viewModel.notification_body.value = title
        }
    }
}