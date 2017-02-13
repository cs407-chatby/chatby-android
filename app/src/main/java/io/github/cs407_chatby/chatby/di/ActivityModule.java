package io.github.cs407_chatby.chatby.di;

import android.app.Activity;
import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SuppressWarnings("WeakerAccess")
@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides @Named("Activity")
    public Context provideActivityContext() {
        return activity;
    }

    @Provides
    public Activity provideActivity() {
        return activity;
    }
}
