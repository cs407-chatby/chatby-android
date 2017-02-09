package io.github.cs407_chatby.chatby.utils;


import android.support.annotation.NonNull;
import android.view.View;

public class ViewUtils {

    public static void toggleVisibility(@NonNull View view) {
        if (view.getVisibility() == View.VISIBLE) view.setVisibility(View.GONE);
        else view.setVisibility(View.VISIBLE);
    }
}
