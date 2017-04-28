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

import java.util.Map;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.CurrentUserCache;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.ui.room.RoomActivity;

public class MessagingService extends FirebaseMessagingService {

    public static final String ACTION_MESSAGE_RECEIVED = "Message Received";

    @Inject CurrentUserCache userCache;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        ChatByApp.get(this).getComponent().inject(this);
        Log.e("Firebase", "From: " + remoteMessage.getFrom());

        Map<String, String> data = remoteMessage.getData();

        if (data.size() > 0) {
            Log.e("Firebase", "Message data payload: " + data);
        }

        ChatByApp app = (ChatByApp) getApplication();
        Activity activity = app.getCurrentActivity();
        int id = Integer.valueOf(data.get("Id"));

        if (activity != null && activity instanceof RoomActivity
                && id == activity.getIntent().getIntExtra(RoomActivity.ROOM_ID, -1)) {

            Log.e("Firebase", "refreshing room");

            Intent intent = new Intent();
            intent.setAction(ACTION_MESSAGE_RECEIVED);
            sendBroadcast(intent);

        } else {

            Log.e("Firebase", "creating notification");

            String title = data.get("Title");
            String message = data.get("Message");
            String sender = data.get("Sender");

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
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(id, notification);

        }
    }
}
