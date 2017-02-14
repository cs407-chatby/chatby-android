package io.github.cs407_chatby.chatby.data.service;

import java.util.List;

import io.github.cs407_chatby.chatby.data.model.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface ChatByService {

    @GET("users/")
    Call<List<User>> getUsers();

    @POST("users/")
    Call<User> postUser(@Body NewUser user);

    @GET("users/{id}/")
    Call<User> getUser(@Path("id") Integer id);

    @PUT("users/{id}/")
    Call<User> putUser(@Path("id") User user);

    @POST("auth/")
    Call<AuthResponse> postAuth(@Body AuthRequest credentials);
}
