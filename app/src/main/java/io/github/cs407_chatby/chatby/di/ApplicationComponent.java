package io.github.cs407_chatby.chatby.di;


import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.ui.auth.AuthFragment;
import io.github.cs407_chatby.chatby.ui.main.create.CreateFragment;
import io.github.cs407_chatby.chatby.ui.main.home.HomeFragment;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(ChatByApp application);
    void inject(AuthFragment fragment);
    void inject(HomeFragment fragment);
    void inject(CreateFragment fragment);

    @Named("Application")
    Context getContext();
}
