package io.github.cs407_chatby.chatby;

import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.di.ApplicationComponent;
import io.github.cs407_chatby.chatby.di.ApplicationModule;
import io.github.cs407_chatby.chatby.di.DaggerApplicationComponent;

public class ChatByApp extends Application {

    @Inject Application app;

    protected ApplicationComponent applicationComponent;

    public static ChatByApp get(Context context) {
        return (ChatByApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
