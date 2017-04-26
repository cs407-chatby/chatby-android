package io.github.cs407_chatby.chatby.ui.room.main;


import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.Like;
import io.github.cs407_chatby.chatby.data.model.Membership;
import io.github.cs407_chatby.chatby.data.model.PostLike;
import io.github.cs407_chatby.chatby.data.model.PostMembership;
import io.github.cs407_chatby.chatby.data.model.PostMessage;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.github.cs407_chatby.chatby.ui.viewModel.ViewMessage;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RoomPresenter implements RoomContract.Presenter {
    private final ChatByService service;

    private RoomContract.View view = null;
    private Room room = null;

    private boolean liking = false;

    @Inject
    public RoomPresenter(ChatByService service) {
        this.service = service;
    }

    @Override
    public void onAttach(@NonNull RoomContract.View view, Room room) {
        this.view = view;
        this.room = room;
    }

    @Override
    public void onInitialize() {
        view.showLoading();

        service.getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (view != null) view.setCurrentUser(user);
                    service.getMembershipsForUserInRoom(user.getId(), room.getId())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(memberships -> {
                                if (view == null) return;
                                if (memberships.size() > 0) view.showJoined();
                                else view.showNotJoined();
                            }, error -> view.showNotJoined());
                }, error -> view.showNotJoined());

        service.getMessages(room.getId())
                .toObservable()
                .flatMap(Observable::fromIterable)
                .flatMapSingle(message -> service.getUser(message.getCreatedBy().getId())
                        .map(user -> ViewMessage.create(message, user)))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                    if (view == null) return;
                    view.hideLoading();
                    if (messages.size() > 0)
                        view.showMessages(messages);
                    else view.showEmpty();
                }, error -> view.showError("Failed to retrieve messages"));
    }

    @Override
    public void onDetach() {
        this.view = null;
        this.room = null;
    }

    @Override
    public void onSendPressed(String text) {
        if (room == null) return;
        PostMessage postMessage = new PostMessage(false, text, room.getUrl());
        service.postMessage(postMessage)
                .flatMap(message -> service.getUser(message.getCreatedBy().getId())
                        .map(user -> ViewMessage.create(message, user)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    if (view != null) view.showMessageSent(message);
                }, error -> {
                    if (view != null) view.showError("Failed to send message");
                });
    }

    @Override
    public void onMessageLikePressed(ViewMessage message) {
        if (liking) return;
        liking = true;
        service.getCurrentUser()
                .flatMap(user -> service.getLikes(message.getId(), user.getId()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(likes -> {
                    if (likes.size() > 0) dislike(message, likes.get(0));
                    else like(message);
                }, error -> liking = false);
    }

    @Override
    public void onRoomStarClicked() {
        // TODO
    }

    private void dislike(ViewMessage dislikedMessage, Like like) {
        service.deleteLike(like.getId())
                .andThen(service.getMessage(dislikedMessage.getId()))
                .flatMap(message -> service.getUser(message.getCreatedBy().getId())
                        .map(user -> ViewMessage.create(message, user)))
                .doFinally(() -> liking = false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    if (view != null) view.showMessageUpdated(message);
                }, error -> {});
    }

    private void like(ViewMessage likedMessage) {
        PostLike likePost = new PostLike(likedMessage.getUrl());
        service.postLike(likePost)
                .flatMap(like -> service.getMessage(like.getMessage().getId()))
                .flatMap(message -> service.getUser(message.getCreatedBy().getId())
                        .map(user -> ViewMessage.create(message, user)))
                .doFinally(() -> liking = false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    if (view != null) view.showMessageUpdated(message);
                }, error -> {});
    }

    @Override
    public void onJoinRoomPressed() {
        PostMembership newMembership = new PostMembership(false, room.getUrl());
        service.postMembership(newMembership)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(membership -> {
                    Log.d("membership", membership.toString());
                    if (view != null)
                        view.showJoined();
                }, error -> {
                    Log.e("membership", error.getMessage(), error);
                    if (view != null)
                        view.showError("Failed to join room " + room.getName());
                });
        if (view != null) view.showJoining();
    }

    @Override
    public void onLeaveRoomPressed() {
        service.getCurrentUser()
                .flatMap(user -> service.getMembershipsForUserInRoom(user.getId(), room.getId()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(memberships -> {
                        if (memberships.size() > 0)
                            leaveRoom(memberships.get(0));
                        else if (view != null) view.showNotJoined();
                }, error -> {
                        if (view != null) view.showError("Failed to leave room");
                });
    }

    private void leaveRoom(Membership membership) {
        service.deleteMembership(membership.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                        if (view != null) view.showNotJoined();
                }, error -> {
                        if (view != null) view.showError("Failed to leave room");
                });
    }
}
