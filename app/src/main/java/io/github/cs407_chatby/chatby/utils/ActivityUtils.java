package io.github.cs407_chatby.chatby.utils;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

public class ActivityUtils {

    public static void finishAndStart(@NonNull Activity activity, @NonNull Class next) {
        activity.startActivity(new Intent(activity, next));
        activity.finish();
    }
}
