package io.github.cs407_chatby.chatby.ui.room;


import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.PostMembership;
import io.github.cs407_chatby.chatby.data.model.PostMessage;
import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RoomPresenter implements RoomContract.Presenter {
    private final ChatByService service;

    private RoomContract.View view = null;
    private Room room = null;

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
                });

        service.getMessages(room.getUrl().getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                    if (view == null) return;
                    view.hideLoading();
                    if (messages.size() > 0)
                        view.showMessages(messages);
                    else view.showEmpty();
                }, error -> view.showError("Failed to retrieve messages"));

        // TODO Check if user has already joined the room
        view.showNotJoined();
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                    if (view != null) view.showMessageSent(message);
                }, error -> {
                    if (view != null) view.showError("Failed to send message");
                });
    }

    @Override
    public void onMessageLikePressed(ResourceUrl url) {

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
        // TODO Leave room
        if (view != null) view.showNotJoined();
    }
}
