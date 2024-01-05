package com.ozcanbayram.gezle.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ozcanbayram.gezle.R
import com.ozcanbayram.gezle.view.MainActivity
import com.ozcanbayram.gezle.view.Welcome

private lateinit var auth: FirebaseAuth

private lateinit var pendingIntent : PendingIntent

const val channelId = "notification_channel"
const val channelName = "com.ozcanbayram.gezle.services"
class MyFirebaseMessagingService : FirebaseMessagingService() {
    //Step1: Generate the notification - Bildirimi oluşturmak
    //Step2: Attach the notification created with the custom layout - Özel düzen ile oluşturulan bildirimi ekleyin
    //Step3: Show the notification - Bildirimi göster

    //Step3:
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)

    }

    fun getRemoteView(title: String,message: String): RemoteViews {
        val remoteView = RemoteViews("com.ozcanbayram.gezle.services",R.layout.notification)

        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.message,message)
        remoteView.setImageViewResource(R.id.notification_app_logo,R.drawable.notification)

        return remoteView
    }

    fun generateNotification(title: String, message: String) {

        auth = Firebase.auth
        val currentUser = auth.currentUser

        if (currentUser != null){ //Aktif kullanıcı varsa Anasayfaya yönlendirme kontrolü.
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT)
        }
        else{
            val intent = Intent(this, Welcome::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        //Channel id, Channel name
        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.notification)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Version Control
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())

    }
}