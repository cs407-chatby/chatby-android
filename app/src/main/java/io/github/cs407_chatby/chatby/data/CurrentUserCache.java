package io.github.cs407_chatby.chatby.data;


import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.cs407_chatby.chatby.data.model.User;

@Singleton
public class CurrentUserCache {

    @Nullable private User currentUser = null;

    @Inject public CurrentUserCache() {}

    @Nullable
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(@Nullable User currentUser) {
        this.currentUser = currentUser;
    }
}
