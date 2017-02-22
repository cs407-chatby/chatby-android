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
import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
                .registerTypeAdapter(ResourceUrl.class, new ResourceUrl.Adapter())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://chatby.vohras.tk/api/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(ChatByService.class);
    }
}
