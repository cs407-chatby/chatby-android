package io.github.cs407_chatby.chatby.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.github.cs407_chatby.chatby.ui.room.RoomActivity;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("Firebase", "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d("Firebase", "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("Firebase", "Message notification body: " + remoteMessage.getNotification().getBody());
        }

        // TODO refresh current message group if any

        String id = remoteMessage.getData().get("Id");
        String title = remoteMessage.getData().get("Title");
        String message = remoteMessage.getData().get("Message");
        String sender = remoteMessage.getData().get("Sender");

        Intent intent = new Intent(this, RoomActivity.class);
        intent.putExtra(RoomActivity.ROOM_ID, Integer.valueOf(id));

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setStyle(new NotificationCompat.MessagingStyle("TODO")
                    .setConversationTitle(title)
                    .addMessage(message, remoteMessage.getSentTime(), sender))
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(Integer.valueOf(id), notification);
    }
}
