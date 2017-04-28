package io.github.cs407_chatby.chatby.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Locale;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.data.AuthInterceptor;
import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
                .create();
    }

    @Provides
    public Geocoder provideGeocoder() {
        return new Geocoder(app, Locale.US);
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
    public HttpLoggingInterceptor provideLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    OkHttpClient provideOkHttpClient(AuthInterceptor authInterceptor,
                                     HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
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

    @Provides
    public SharedPreferences provideSharedPreferences() {
        return app.getSharedPreferences("Settings", 0);
    }
}
