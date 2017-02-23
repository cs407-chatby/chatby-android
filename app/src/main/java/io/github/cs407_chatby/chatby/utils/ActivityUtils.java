package io.github.cs407_chatby.chatby.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

public class ActivityUtils {

    public static void finishAndStart(@NonNull Activity activity, @NonNull Class next) {
        activity.startActivity(new Intent(activity, next));
        activity.finish();
    }

    public static boolean checkPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
