package io.github.cs407_chatby.chatby.data.service;

import java.util.List;

import io.github.cs407_chatby.chatby.data.model.*;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.*;

public interface ChatByService {

    // region Auth

    @POST("auth/")
    Single<AuthResponse> postAuth(@Body AuthRequest credentials);

    // endregion

    // region Users

    @GET("users/")
    Observable<User> getUsers();

    @POST("users/")
    Single<User> postUser(@Body PostUser user);

    @GET("users/{id}/")
    Single<User> getUser(@Path("id") Integer id);

    @PUT("users/{id}/")
    Single<User> putUser(@Path("id") Integer id, @Body User user);

    @DELETE("users/{id}/")
    Completable deleteUser(@Path("id") Integer id);

    @PATCH("user/{id}/")
    Single<User> patchUserPassword(@Path("id") Integer id, @Body PatchPassword newPassword);

    // endregion

    // region Rooms

    @GET("rooms/")
    Observable<Room> getRooms(@Query("my_lat") double latitude, @Query("my_lon") double longitude);

    @GET("rooms/")
    Observable<Room> getRooms(@Query("created_by") int userId);

    @POST("rooms/")
    Single<Room> postRoom(@Body PostRoom user);

    @GET("rooms/{id}/")
    Single<Room> getRoom(@Path("id") Integer id);

    @PUT("rooms/{id}/")
    Single<Room> putRoom(@Path("id") Integer id, @Body Room room);

    @DELETE("rooms/{id}/")
    Completable deleteRoom(@Path("id") Integer id);

    // endregion

    // region Messages

    @GET("messages/")
    Observable<Message> getMessages(@Query("room") Integer roomId);

    @POST("messages/")
    Single<Message> postMessage(@Body PostMessage user);

    @GET("messages/{id}/")
    Single<Message> getMessage(@Path("id") Integer id);

    @PUT("messages/{id}/")
    Single<Message> putMessage(@Path("id") Integer id, @Body Message message);

    @DELETE("messages/{id}/")
    Completable deleteMessage(@Path("id") Integer id);

    // endregion

    // region Memberships

    @GET("memberships/")
    Observable<Membership> getMembershipsForRoom(@Query("room") Integer roomId);

    @GET("memberships/")
    Observable<Membership> getMembershipsForUser(@Query("user") Integer userId);

    @POST("memberships/")
    Single<Membership> postMembership(@Body PostMembership membership);

    @GET("memberships/{id}/")
    Single<Membership> getMembership(@Path("id") Integer id);

    @PUT("memberships/{id}/")
    Single<Membership> putMembership(@Path("id") Integer id, @Body Membership user);

    @DELETE("memberships/{id}/")
    Completable deleteMembership(@Path("id") Integer id);

    // endregion

    // region Likes

    @GET("likes/")
    Observable<Like> getLikes(@Query("message") Integer messageId);

    @POST("likes/")
    Single<Like> postLike(@Body PostLike user);

    @GET("likes/{id}/")
    Single<Like> getLike(@Path("id") Integer id);

    @PUT("likes/{id}/")
    Single<Like> putLike(@Path("id") Integer id, @Body Like like);

    @DELETE("likes/{id}/")
    Completable deleteLike(@Path("id") Integer id);

    // endregion
}
