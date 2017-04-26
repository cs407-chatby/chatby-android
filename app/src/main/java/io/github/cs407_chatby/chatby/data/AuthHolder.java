package io.github.cs407_chatby.chatby.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class AuthHolder {

    private static final String PREFS_NAME = "CHATBY_ACCOUNT_PREFS";
    private static final String TOKEN_KEY = "ACCOUNT_TOKEN";

    private final Context context;

    @Inject
    public AuthHolder(@Named("Application") Context context) {
        this.context = context;
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(PREFS_NAME, 0);
    }

    public void saveToken(String token) {
        getPreferences().edit().putString(TOKEN_KEY, token).apply();
    }

    @Nullable
    public String readToken() {
        return getPreferences().getString(TOKEN_KEY, null);
    }

    public void deleteToken() {
        getPreferences().edit().remove(TOKEN_KEY).apply();
    }
}
