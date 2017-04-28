package io.github.cs407_chatby.chatby.ui.main.active;


import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.github.cs407_chatby.chatby.ui.room.RoomActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ActivePresenter implements ActiveContract.Presenter {
    private ActiveContract.View view;
    private ChatByService service;

    @Inject
    public ActivePresenter(ChatByService service) {
        this.service = service;
    }

    @Override
    public void onAttach(ActiveContract.View view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    @Override
    public void onRefresh() {
        if (view != null) view.showLoading();

        Log.e("TEST", "refreshing active presenter");

        service.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::roomListChain);
    }

    private void roomListChain(User user) {
        Log.e("TEST", "starting room list chain");

        service.getRoomLikesForUser(user.getId())
                .toObservable()
                .flatMapIterable(likes -> likes)
                .flatMapSingle(like -> service.getRoom(like.getRoom().getId()))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favoriteRooms -> {

                    Log.e("TEST", favoriteRooms.size() + " favorites");

                    service.getRooms(user.getId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(createdRooms -> {

                                Log.e("TEST", createdRooms.size() + " created");

                                service.getMembershipsForUser(user.getId())
                                        .toObservable()
                                        .flatMapIterable(memberships -> memberships)
                                        .flatMapSingle(membership ->
                                                service.getRoom(membership.getRoom().getId()))
                                        .toSortedList((a, b) ->
                                                a.getCreationTime().compareTo(b.getCreationTime()))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(activeRooms -> {

                                            Log.e("TEST", activeRooms.size() + " active");

                                            activeRooms.removeAll(favoriteRooms);
                                            activeRooms.removeAll(createdRooms);

                                            Log.e("TEST", activeRooms.size() + " filtered active");

                                            if (view != null) {
                                                view.showFavorite(favoriteRooms);
                                                view.showCreated(createdRooms);
                                                view.showActive(activeRooms);
                                            }
                                        });
                            });
                });
    }

    @Override
    public void onRoomClicked(Room room) {
        Bundle bundle = new Bundle();
        bundle.putInt(RoomActivity.ROOM_ID, room.getId());
        if (view != null) view.openRoom(bundle);
    }

    @Override
    public void onStarRoomClicked(Room room) {

    }

    @Override
    public void onLeaveRoomClicked(Room room) {

    }
}
