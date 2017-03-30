package io.github.cs407_chatby.chatby.utils;


import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewUtils {

    @SuppressWarnings("unchecked")
    @Nullable
    public static <V extends View> V findView(@NonNull View view, @IdRes int id) {
        return (V) view.findViewById(id);
    }

    public static View inflate(ViewGroup parent, @LayoutRes int layout) {
        return LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
    }

    public static void toggleExistence(@NonNull View view) {
        if (view.getVisibility() == View.VISIBLE) view.setVisibility(View.GONE);
        else view.setVisibility(View.VISIBLE);
    }

    public static void toggleVisibility(@NonNull View view) {
        if (view.getVisibility() == View.VISIBLE) view.setVisibility(View.GONE);
        else view.setVisibility(View.VISIBLE);
    }

    public static boolean viewIsVisible(@NonNull View view) {
        return view.getVisibility() == View.VISIBLE;
    }

}
