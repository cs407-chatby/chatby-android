package io.github.cs407_chatby.chatby.ui.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class AccountHolder {

    private static final String PREFS_NAME = "CHATBY_ACCOUNT_PREFS";
    private static final String TOKEN_KEY = "ACCOUNT_TOKEN";

    private final SharedPreferences preferences;

    @Inject
    public AccountHolder(@Named("Application") Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public void saveToken(String token) {
        preferences.edit().putString(TOKEN_KEY, token).apply();
    }

    @Nullable
    public String readToken() {
        return preferences.getString(TOKEN_KEY, null);
    }

    public void deleteToken() {
        preferences.edit().remove(TOKEN_KEY).apply();
    }
}
