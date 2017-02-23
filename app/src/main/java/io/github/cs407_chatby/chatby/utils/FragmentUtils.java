package io.github.cs407_chatby.chatby.utils;


import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public class FragmentUtils {

    @SuppressWarnings("unchecked")
    @Nullable
    public static <V extends View> V findView(@NonNull Fragment fragment, @IdRes int id) {
        View view = fragment.getView();
        if (view == null) return null;
        View v = view.findViewById(id);
        return (V) v;
    }
}
