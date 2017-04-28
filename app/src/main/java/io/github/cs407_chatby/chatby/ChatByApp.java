package io.github.cs407_chatby.chatby;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.di.ApplicationComponent;
import io.github.cs407_chatby.chatby.di.ApplicationModule;
import io.github.cs407_chatby.chatby.di.DaggerApplicationComponent;

public class ChatByApp extends Application {

    @Inject Application app;
    protected ApplicationComponent applicationComponent;
    protected Activity currentActivity;

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

        registerActivityLifecycleCallbacks(new SimpleLifecycleListener() {
            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("currentActivity", "set to " + activity);
                currentActivity = activity;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (activity == currentActivity) {
                    Log.e("currentActivity", "cleared");
                    currentActivity = null;
                }
            }
        });
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
