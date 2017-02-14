package io.github.cs407_chatby.chatby.di;

import android.app.Application;
import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

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

    @Provides
    public ChatByService provideChatByService() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://chatby.vohras.tk/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(ChatByService.class);
    }
}
