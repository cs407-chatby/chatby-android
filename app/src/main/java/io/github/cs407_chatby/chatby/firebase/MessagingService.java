package io.github.cs407_chatby.chatby.firebase;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.data.CurrentUserCache;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.ui.room.RoomActivity;

public class MessagingService extends FirebaseMessagingService {

    public static final String ACTION_MESSAGE_RECEIVED = "Message Received";

    @Inject CurrentUserCache userCache;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        ChatByApp.get(this).getComponent().inject(this);
        Log.d("Firebase", "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d("Firebase", "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("Firebase", "Message notification body: " + remoteMessage.getNotification().getBody());
        }

        Activity activity = ChatByApp.get(this).getCurrentActivity();
        String id = remoteMessage.getData().get("Id");
        Intent args = activity.getIntent();
        int currentId = args.getIntExtra(RoomActivity.ROOM_ID, -1);

        if (activity instanceof RoomActivity && Integer.valueOf(id) == currentId) {

            Intent intent = new Intent();
            intent.setAction(ACTION_MESSAGE_RECEIVED);
            sendBroadcast(intent);

        } else {

            String title = remoteMessage.getData().get("Title");
            String message = remoteMessage.getData().get("Message");
            String sender = remoteMessage.getData().get("Sender");

            String name = "None";
            User currentUser = userCache.getCurrentUser();
            if (currentUser != null) name = currentUser.getUsername();

            Intent intent = new Intent(this, RoomActivity.class);
            intent.putExtra(RoomActivity.ROOM_ID, Integer.valueOf(id));

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(this)
                    .setStyle(new NotificationCompat.MessagingStyle(name)
                            .setConversationTitle(title)
                            .addMessage(message, remoteMessage.getSentTime(), sender))
                    .setContentIntent(pendingIntent)
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(Integer.valueOf(id), notification);

        }
    }
}
