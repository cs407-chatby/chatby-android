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

    @GET("rooms/?my_lat={lat}&my_lon={lon}")
    Call<List<Room>> getRooms(@Path("lat") double latitude, @Path("lon") double longitude);

    @GET("rooms/?created_by={userId}")
    Call<List<Room>> getRooms(@Path("userId") int userId);

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

    @GET("messages/?room={roomId}")
    Call<List<Message>> getMessages(@Path("roomId") Integer roomId);

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

    @GET("memberships/?room={roomId}")
    Call<List<Membership>> getMembershipsForRoom(@Path("roomId") Integer roomId);

    @GET("memberships/?user={userId}")
    Call<List<Membership>> getMembershipsForUser(@Path("userId") Integer userId);

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

    @GET("likes/?message={messageId}")
    Call<List<Like>> getLikes(@Path("messageId") Integer messageId);

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
