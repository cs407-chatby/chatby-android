package io.github.cs407_chatby.chatby.di;


import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import io.github.cs407_chatby.chatby.ChatByApp;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(ChatByApp application);

    @Named("Application")
    Context getContext();
}
