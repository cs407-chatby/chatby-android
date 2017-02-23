package io.github.cs407_chatby.chatby.utils;


import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class ViewUtils {

    @SuppressWarnings("unchecked")
    @Nullable
    public static <V extends View> V findView(@NonNull View view, @IdRes int id) {
        return (V) view.findViewById(id);
    }

    public static void toggleVisibility(@NonNull View view) {
        if (view.getVisibility() == View.VISIBLE) view.setVisibility(View.GONE);
        else view.setVisibility(View.VISIBLE);
    }

    public static boolean viewIsVisible(@NonNull View view) {
        return view.getVisibility() == View.VISIBLE;
    }
}
