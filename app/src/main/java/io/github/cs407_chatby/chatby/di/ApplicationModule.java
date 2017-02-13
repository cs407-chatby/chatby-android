package io.github.cs407_chatby.chatby.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.github.cs407_chatby.chatby.ChatByApp;

@SuppressWarnings("WeakerAccess")
@Module
public class ApplicationModule {

    private final ChatByApp app;

    public ApplicationModule(ChatByApp app) {
        this.app = app;
    }

    @Provides @Named("Application")
    public Context provideApplicationContext() {
        return app;
    }

    @Provides
    public Application provideApplication() {
        return app;
    }
}
