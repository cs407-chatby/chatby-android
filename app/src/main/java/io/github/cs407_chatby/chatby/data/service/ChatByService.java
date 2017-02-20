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

    @PATCH("user/{id}/")
    Call<User> patchUserPassword(@Path("id") Integer id, @Body PatchPassword newPassword);

    // endregion

    // region Rooms

    @GET("rooms/")
    Call<List<Room>> getRooms(@Query("my_lat") double latitude, @Query("my_lon") double longitude);

    @GET("rooms/")
    Call<List<Room>> getRooms(@Query("created_by") int userId);

    @POST("rooms/")
    Call<Room> postRoom(@Body PostRoom user);

    @GET("rooms/{id}/")
    Call<Room> getRoom(@Path("id") Integer id);

    @PUT("rooms/{id}/")
    Call<Room> putRoom(@Path("id") Integer id, @Body Room room);

    @DELETE("rooms/{id}/")
    Call<Void> deleteRoom(@Path("id") Integer id);

    // endregion

    // region Messages

    @GET("messages/")
    Call<List<Message>> getMessages(@Query("room") Integer roomId);

    @POST("messages/")
    Call<Message> postMessage(@Body PostMessage user);

    @GET("messages/{id}/")
    Call<Message> getMessage(@Path("id") Integer id);

    @PUT("messages/{id}/")
    Call<Message> putMessage(@Path("id") Integer id, @Body Message message);

    @DELETE("messages/{id}/")
    Call<Void> deleteMessage(@Path("id") Integer id);

    // endregion

    // region Memberships

    @GET("memberships/")
    Call<List<Membership>> getMembershipsForRoom(@Query("room") Integer roomId);

    @GET("memberships/")
    Call<List<Membership>> getMembershipsForUser(@Query("user") Integer userId);

    @POST("memberships/")
    Call<Membership> postMembership(@Body PostMembership membership);

    @GET("memberships/{id}/")
    Call<Membership> getMembership(@Path("id") Integer id);

    @PUT("memberships/{id}/")
    Call<Membership> putMembership(@Path("id") Integer id, @Body Membership user);

    @DELETE("memberships/{id}/")
    Call<Void> deleteMembership(@Path("id") Integer id);

    // endregion

    // region Likes

    @GET("likes/")
    Call<List<Like>> getLikes(@Query("message") Integer messageId);

    @POST("likes/")
    Call<Like> postLike(@Body PostLike user);

    @GET("likes/{id}/")
    Call<Like> getLike(@Path("id") Integer id);

    @PUT("likes/{id}/")
    Call<Like> putLike(@Path("id") Integer id, @Body Like like);

    @DELETE("likes/{id}/")
    Call<Void> deleteLike(@Path("id") Integer id);

    // endregion
}
