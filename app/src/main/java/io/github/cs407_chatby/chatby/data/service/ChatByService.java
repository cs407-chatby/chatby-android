package io.github.cs407_chatby.chatby.data.service;

import java.util.List;

import io.github.cs407_chatby.chatby.data.model.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface ChatByService {

    // region Auth

    @POST("auth/")
    Call<AuthResponse> postAuth(@Body AuthRequest credentials);

    // endregion

    // region Users

    @GET("users/")
    Call<List<User>> getUsers();

    @POST("users/")
    Call<User> postUser(@Body PostUser user);

    @GET("users/{id}/")
    Call<User> getUser(@Path("id") Integer id);

    @PUT("users/{id}/")
    Call<User> putUser(@Path("id") Integer id, @Body User user);

    @DELETE("users/{id}/")
    Call<Void> deleteUser(@Path("id") Integer id);

    // endregion

    // region Rooms

    @GET("rooms/")
    Call<List<Room>> getRooms();

    @POST("rooms/")
    Call<Room> postRoom(@Body PostRoom user);

    @GET("rooms/{id}/")
    Call<Room> getRoom(@Path("id") Integer id);

    @PUT("rooms/{id}/")
    Call<Room> putRoom(@Path("id") Integer id, @Body Room user);

    @DELETE("rooms/{id}/")
    Call<Void> deleteRoom(@Path("id") Integer id);

    // endregion

    // region Messages

    @GET("messages/?room={roomId}")
    Call<List<Message>> getMessages(@Path("roomId") Integer roomId);

    @POST("messages/")
    Call<Message> postMessage(@Body PostMessage user);

    @GET("messages/{id}/")
    Call<Message> getMessage(@Path("id") Integer id);

    @PUT("messages/{id}/")
    Call<Message> putMessage(@Path("id") Integer id, @Body Message user);

    @DELETE("messages/{id}/")
    Call<Void> deleteMessage(@Path("id") Integer id);

    // endregion
}
