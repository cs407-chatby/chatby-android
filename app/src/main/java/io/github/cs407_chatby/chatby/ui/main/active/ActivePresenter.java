package io.github.cs407_chatby.chatby.ui.main.active;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

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

        // Favorite rooms


        // Created rooms
        activeRooms
                .filter(room -> room.getCreatedBy().getId().equals(currentUser.getId()))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rooms -> {
                    //if (view != null) view.showCreated(rooms);
                });

        // Active rooms
        activeRooms
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rooms -> {
                    if (view != null) {
                        view.showActive(rooms);
                        view.showCreated(rooms);
                        view.showFavorite(rooms);
                    }
                });
    }

    @Override
    public void onRoomClicked(Room room) {

    }

    @Override
    public void onStarRoomClicked(Room room) {

    }

    @Override
    public void onLeaveRoomClicked(Room room) {

    }

    private List<Room> makeDummyRooms(int amount) {
        List<Room> rooms = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        for (int i = 0; i < amount; i++) {
            rooms.add(new Room(null, "Room " + i, new Date(), 100.0, calendar.getTime(),
                    null, 0.0, 0.0, null, new ArrayList<>()));
        }
        return rooms;
    }
}
