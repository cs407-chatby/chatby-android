package io.github.cs407_chatby.chatby.ui.main.home;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.model.Room;

public class HomePresenter implements HomeContract.Presenter {
    @Nullable
    private HomeContract.View view = null;
    private boolean loading = true;

    @Inject
    public HomePresenter() {}

    @Override
    public void onAttach(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        view = null;
    }

    @Override
    public void onCreateClicked() {
        if (view != null) {
            view.showRoomCreation();
        }
    }

    @Override
    public void onRoomClicked(Room room) {
        // TODO Sprint 2
    }

    @Override
    public void onRefreshClicked() {
        loading = true;

        // TODO do actual request.

        if (view != null && loading) {
            view.updateCreated(dummyRooms());
            view.updateNearby(dummyRooms());
            loading = false;
        }
    }

    private List<Room> dummyRooms() {
        Room room1 = new Room(new ResourceUrl("whatup"), "Eaps Lecture", new Date(),
                10.0, new Date(), "oops", 10.0, 10.0, new ResourceUrl("whatup"),
                new ArrayList<ResourceUrl>());
        Room room2 = new Room(new ResourceUrl("whatup"), "Eaps Lecture 2", new Date(),
                10.0, new Date(), "oops", 10.0, 10.0, new ResourceUrl("whatup"),
                new ArrayList<ResourceUrl>());
        Room room3 = new Room(new ResourceUrl("whatup"), "Eaps Lecture 3", new Date(),
                10.0, new Date(), "oops", 10.0, 10.0, new ResourceUrl("whatup"),
                new ArrayList<ResourceUrl>());
        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
        return rooms;
    }
}
