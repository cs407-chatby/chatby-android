package io.github.cs407_chatby.chatby.data.service;

import java.util.List;

import io.github.cs407_chatby.chatby.data.model.*;
import io.reactivex.Observable;
import retrofit2.http.*;

public interface ChatByService {

    // region Auth

    @POST("auth/")
    Observable<AuthResponse> postAuth(@Body AuthRequest credentials);

    // endregion

    // region Users

    @GET("users/")
    Observable<List<User>> getUsers();

    @POST("users/")
    Observable<User> postUser(@Body PostUser user);

    @GET("users/{id}/")
    Observable<User> getUser(@Path("id") Integer id);

    @PUT("users/{id}/")
    Observable<User> putUser(@Path("id") Integer id, @Body User user);

    @DELETE("users/{id}/")
    Observable<Void> deleteUser(@Path("id") Integer id);

    @PATCH("user/{id}/")
    Observable<User> patchUserPassword(@Path("id") Integer id, @Body PatchPassword newPassword);

    // endregion

    // region Rooms

    @GET("rooms/")
    Observable<List<Room>> getRooms(@Query("my_lat") double latitude, @Query("my_lon") double longitude);

    @GET("rooms/")
    Observable<List<Room>> getRooms(@Query("created_by") int userId);

    @POST("rooms/")
    Observable<Room> postRoom(@Body PostRoom user);

    @GET("rooms/{id}/")
    Observable<Room> getRoom(@Path("id") Integer id);

    @PUT("rooms/{id}/")
    Observable<Room> putRoom(@Path("id") Integer id, @Body Room room);

    @DELETE("rooms/{id}/")
    Observable<Void> deleteRoom(@Path("id") Integer id);

    // endregion

    // region Messages

    @GET("messages/")
    Observable<List<Message>> getMessages(@Query("room") Integer roomId);

    @POST("messages/")
    Observable<Message> postMessage(@Body PostMessage user);

    @GET("messages/{id}/")
    Observable<Message> getMessage(@Path("id") Integer id);

    @PUT("messages/{id}/")
    Observable<Message> putMessage(@Path("id") Integer id, @Body Message message);

    @DELETE("messages/{id}/")
    Observable<Void> deleteMessage(@Path("id") Integer id);

    // endregion

    // region Memberships

    @GET("memberships/")
    Observable<List<Membership>> getMembershipsForRoom(@Query("room") Integer roomId);

    @GET("memberships/")
    Observable<List<Membership>> getMembershipsForUser(@Query("user") Integer userId);

    @POST("memberships/")
    Observable<Membership> postMembership(@Body PostMembership membership);

    @GET("memberships/{id}/")
    Observable<Membership> getMembership(@Path("id") Integer id);

    @PUT("memberships/{id}/")
    Observable<Membership> putMembership(@Path("id") Integer id, @Body Membership user);

    @DELETE("memberships/{id}/")
    Observable<Void> deleteMembership(@Path("id") Integer id);

    // endregion

    // region Likes

    @GET("likes/")
    Observable<List<Like>> getLikes(@Query("message") Integer messageId);

    @POST("likes/")
    Observable<Like> postLike(@Body PostLike user);

    @GET("likes/{id}/")
    Observable<Like> getLike(@Path("id") Integer id);

    @PUT("likes/{id}/")
    Observable<Like> putLike(@Path("id") Integer id, @Body Like like);

    @DELETE("likes/{id}/")
    Observable<Void> deleteLike(@Path("id") Integer id);

    // endregion
}
