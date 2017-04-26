package io.github.cs407_chatby.chatby.di.auth;

import dagger.Module;
import io.github.cs407_chatby.chatby.di.ActivityModule;
import io.github.cs407_chatby.chatby.ui.auth.AuthActivity;

@Module
public class AuthModule extends ActivityModule {

    public AuthModule(AuthActivity activity) {
        super(activity);
    }
}
