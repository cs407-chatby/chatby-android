package io.github.cs407_chatby.chatby.ui.main.active;


import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.github.cs407_chatby.chatby.ui.room.RoomActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ActivePresenter implements ActiveContract.Presenter {
    private ActiveContract.View view;
    private ChatByService service;
    private User currentUser;

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

        Observable<Room> activeRooms = service.getCurrentUser()
                .doOnSuccess(user -> currentUser = user)
                .flatMap(user -> service.getMembershipsForUser(user.getId()))
                .toObservable()
                .flatMapIterable(memberships -> memberships)
                .flatMapSingle(membership -> service.getRoom(membership.getRoom().getId()));

        showFavorite(activeRooms);
    }

    private void showFavorite(Observable<Room> observable) {
        // TODO
        showActive(observable, new ArrayList<>());
    }

    private void showActive(Observable<Room> observable, List<Room> blacklist) {
        observable
                .filter(room -> !blacklist.contains(room))
                .toSortedList((a, b) -> {
                    if (a.getCreationTime().after(b.getCreationTime())) return -1;
                    else if(a.getCreationTime().before(b.getCreationTime())) return 1;
                    else return 0;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rooms -> {
                    List<Room> created = new ArrayList<>();
                    rooms.forEach(room -> {
                        if (room.getCreatedBy().getId().equals(currentUser.getId()))
                            created.add(room);
                    });
                    rooms.removeAll(created);
                    if (view != null) {
                        view.showCreated(created);
                        view.showActive(rooms);
                    }
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
