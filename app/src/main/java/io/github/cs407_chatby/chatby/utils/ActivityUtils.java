package io.github.cs407_chatby.chatby.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;

public class ActivityUtils {

    public static void start(@NonNull Activity activity, @NonNull Class next, Bundle args, boolean finish) {
        Intent intent = new Intent(activity, next);
        if (args != null) intent.putExtras(args);
        activity.startActivity(intent);
        if (finish) activity.finish();
    }

    public static boolean checkPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <V extends View> V findView(@NonNull Activity activity, @IdRes int id) {
        return (V) activity.findViewById(id);
    }

    public static int dpToPixel(@NonNull Activity activity, int dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        return (int) Math.ceil(dp * logicalDensity);
    }

    public static int pixelToDp(@NonNull Activity activity, float pixels) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        return (int) Math.ceil(pixels / logicalDensity);
    }
}
