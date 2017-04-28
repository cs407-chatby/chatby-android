package io.github.cs407_chatby.chatby.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class InstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase", "Refreshed token: " + refreshedToken);

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .edit().putString("DEVICE_ID", refreshedToken).apply();
    }
}
