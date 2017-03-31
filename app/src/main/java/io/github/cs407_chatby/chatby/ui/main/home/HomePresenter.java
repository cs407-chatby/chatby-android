package io.github.cs407_chatby.chatby.ui.main.home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.Collections;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.github.cs407_chatby.chatby.utils.LocationManager;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class HomePresenter implements HomeContract.Presenter {

    private final LocationManager locationManager;
    private final ChatByService service;

    @Nullable
    private HomeContract.View view = null;

    @Inject
    public HomePresenter(LocationManager locationManager, ChatByService service) {
        this.locationManager = locationManager;
        this.service = service;
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
        if (view != null) {
            view.showRoomCreation();
        }
    }

    @Override
    public void onRoomClicked(Room room) {
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        String json = gson.toJson(room);
        bundle.putString("room", json);
        if (view != null) view.openRoom(bundle);
    }

    @Override
    public void onInitialize() {
        if (view != null) {
            view.updateCreated(Collections.emptyList());
            view.updateNearby(Collections.emptyList());
        }

        locationManager.getObservable()
                .flatMapSingle(loc -> service.getRooms(loc.getLatitude(), loc.getLongitude()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rooms -> {
                    if (view != null)
                        view.updateNearby(rooms);
                }, error -> {
                    if (view != null)
                        view.showError("Failed to get nearby rooms!");
                });

        service.getCurrentUser()
                .flatMap(user -> service.getRooms(user.getUrl().getId()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rooms -> {
                    if (view != null)
                        view.updateCreated(rooms);
                }, error -> {
                    if (view != null)
                        view.showError("Failed to get created rooms!");
                });
    }

    @Override
    public void onLogout() {

    }

    @Override
    public void onDeleteAccount() {

    }
}
