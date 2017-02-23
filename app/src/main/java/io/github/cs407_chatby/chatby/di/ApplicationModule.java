package io.github.cs407_chatby.chatby.di;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.github.cs407_chatby.chatby.ui.auth.AuthInterceptor;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
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

    @Provides
    @Named("Application")
    public Context provideApplicationContext() {
        return app;
    }

    @Provides
    public Application provideApplication() {
        return app;
    }

    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(ResourceUrl.class, new ResourceUrl.Adapter())
                .create();
    }

    @Provides
    public Converter.Factory provideConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    public CallAdapter.Factory provideCallAdapterFactory() {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    @Provides
    OkHttpClient provideOkHttpClient(AuthInterceptor authInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build();
    }

    @Provides
    public ChatByService provideChatByService(OkHttpClient okHttpClient,
                                              Converter.Factory converterFactory,
                                              CallAdapter.Factory callAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl("http://chatby.vohras.tk/api/")
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .client(okHttpClient)
                .build()
                .create(ChatByService.class);
    }
}
