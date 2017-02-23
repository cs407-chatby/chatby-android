package io.github.cs407_chatby.chatby.ui.main.home;

import android.location.Location;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.github.cs407_chatby.chatby.utils.LocationManager;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import lombok.Builder;
import lombok.Data;

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
        // TODO Sprint 2
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
}
