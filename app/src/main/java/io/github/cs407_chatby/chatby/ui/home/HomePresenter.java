package io.github.cs407_chatby.chatby.ui.home;

import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.PostRoom;
import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.utils.LocationManager;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class HomePresenter implements HomeContract.Presenter {

    private final LocationManager locationManager;

    @Nullable
    private HomeContract.View view = null;

    private boolean loading = false;

    @Inject
    public HomePresenter(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @Override
    public void onAttach(HomeContract.View view) {
        this.view = view;
        locationManager.start();
    }

    @Override
    public void onDetach() {
        view = null;
        locationManager.stop();
    }

    @Override
    public void onCreateClicked() {

    }

    @Override
    public void onFinalizeClicked(PostRoom room) {

    }

    @Override
    public void onRoomClicked(Room room) {

    }

    @Override
    public void onRefreshClicked() {
        Log.d("Presenter", "Refresh");
        if (view != null) {
            Log.d("Presenter", "Refresh not null");
            view.updateCreated(dummyRooms());
            view.updateNearby(dummyRooms());
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
