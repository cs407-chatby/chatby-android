package io.github.cs407_chatby.chatby.ui.main.nearby;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.AuthHolder;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.github.cs407_chatby.chatby.ui.room.RoomActivity;
import io.github.cs407_chatby.chatby.utils.LocationManager;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class NearbyPresenter implements NearbyContract.Presenter {

    private final LocationManager locationManager;
    private final ChatByService service;
    private final AuthHolder authHolder;
    private final Geocoder geocoder;

    private Location location;

    @Nullable
    private NearbyContract.View view = null;
    private NearbyContract.SortOrder sortOrder = NearbyContract.SortOrder.Location;

    @Inject
    public NearbyPresenter(LocationManager locationManager, ChatByService service,
                           AuthHolder authHolder, Geocoder geocoder) {
        this.locationManager = locationManager;
        this.service = service;
        this.authHolder = authHolder;
        this.geocoder = geocoder;
    }

    @Override
    public void onAttach(NearbyContract.View view) {
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
        bundle.putInt(RoomActivity.ROOM_ID, room.getId());
        if (view != null) view.openRoom(bundle);
    }

    private double getDistance(double alat, double blat, double alng, double blng) {
        double deltaLat = alat - blat;
        double deltaLng = alng - blng;
        return Math.pow(Math.pow(deltaLat, 2) + Math.pow(deltaLng, 2), 0.5);
    }

    @Override
    public void onRefresh() {
        if (view != null) {
            view.showSortOrder(sortOrder);
            view.showLoading();
        }

        locationManager.getObservable()
                .doOnNext(loc -> location = loc)
                .flatMapSingle(loc -> service.getRooms(loc.getLatitude(), loc.getLongitude()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rooms -> {
                    rooms.sort((a, b) -> {
                        switch (sortOrder) {
                            case Popularity:
                                return b.getMembers().size() - a.getMembers().size();
                            default: {
                                double aDistance = getDistance(location.getLatitude(), a.getLatitude(),
                                        location.getLongitude(), b.getLongitude());
                                double bDistance = getDistance(location.getLatitude(), b.getLatitude(),
                                        location.getLongitude(), b.getLongitude());
                                Double diff = aDistance - bDistance;
                                return diff.intValue();
                            }
                        }
                    });
                    if (view != null) view.updateRooms(rooms);
                }, error -> {
                    if (view != null)
                        view.showError("Failed to get nearby rooms!");
                });

        locationManager.getObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loc -> {
                    List<Address> addressList = geocoder.getFromLocation(
                            loc.getLatitude(), loc.getLongitude(), 1);
                    if (!addressList.isEmpty() && view != null)
                        view.showLocation(addressList.get(0));
                    else
                        view.showLocation(null);
                }, error -> {
                    if (view != null) {
                        view.showLocation(null);
                        view.showError("Failed to name location.");
                    }
                });
    }

    @Override
    public void onLogout() {
        authHolder.deleteToken();
        if (view != null)
            view.openAuth();
    }

    @Override
    public void onDeleteAccount() {
        service.getCurrentUser()
                .flatMapCompletable(user -> service.deleteUser(user.getId()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLogout, error -> {
                    if (view != null)
                        view.showError("Failed to delete account!");
                });
    }

    @Override
    public void onAccountSettingsPressed() {
        service.getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (view != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("user", new Gson().toJson(user));
                        view.openAccount(bundle);
                    }
                }, error -> {
                    if (view != null)
                        view.showError("Failed to get current user");
                });
    }

    @Override
    public void onSortByLocationClicked() {
        sortOrder = NearbyContract.SortOrder.Location;
        if (view != null) onRefresh();
    }

    @Override
    public void onSortByPopularityClicked() {
        sortOrder = NearbyContract.SortOrder.Popularity;
        if (view != null) onRefresh();
    }
}
