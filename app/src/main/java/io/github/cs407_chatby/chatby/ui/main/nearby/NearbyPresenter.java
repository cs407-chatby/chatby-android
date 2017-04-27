package io.github.cs407_chatby.chatby.ui.main.nearby;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.AuthHolder;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.github.cs407_chatby.chatby.ui.room.RoomActivity;
import io.github.cs407_chatby.chatby.utils.LocationManager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NearbyPresenter implements NearbyContract.Presenter {

    private final LocationManager locationManager;
    private final ChatByService service;
    private final AuthHolder authHolder;
    private final Geocoder geocoder;

    private Address address = null;
    private Location location = null;
    private List<Room> rooms = new ArrayList<>();

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

    private boolean checkForChange(List<Room> roomUpdate) {
        if (rooms.isEmpty() || rooms.size() != roomUpdate.size())
            return true;
        for (int i = 0; i < rooms.size(); i++) {
            if (!rooms.get(i).equals(roomUpdate.get(i)))
                return true;
        }
        return false;
    }

    private List<Room> sortRooms(List<Room> rooms) {
        rooms.sort((a, b) -> {
            if (sortOrder.equals(NearbyContract.SortOrder.Popularity)) {
                return b.getMembers().size() - a.getMembers().size();
            } else {
                double aDistance = getDistance(location.getLatitude(), a.getLatitude(),
                        location.getLongitude(), b.getLongitude());
                double bDistance = getDistance(location.getLatitude(), b.getLatitude(),
                        location.getLongitude(), b.getLongitude());
                Double diff = aDistance - bDistance;
                return diff.intValue();
            }
        });
        return rooms;
    }

    private boolean checkForChangedAddress(Address address) {
        if (this.address == null) return true;
        boolean localityChange = !this.address.getLocality().equals(address.getLocality());
        boolean adminChange = !this.address.getAdminArea().equals(address.getAdminArea());
        return localityChange || adminChange;
    }

    @Override
    public void onRefresh() {
        if (view != null) {
            view.showSortOrder(sortOrder);
            view.showLoading();
        }
        this.rooms.clear();

        Observable<Location> getLocation = locationManager.getObservable()
                .doOnNext(loc -> location = loc);

        getLocation
                .flatMapSingle(loc -> service.getRooms(loc.getLatitude(), loc.getLongitude()))
                .flatMap(rooms -> Observable.just(sortRooms(rooms)))
                .filter(this::checkForChange)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rooms -> {
                    Log.d("Refresh", rooms.size() + " Rooms found.");
                    this.rooms = rooms;
                    if (view != null && rooms.size() == 0) view.showEmpty();
                    else if (view != null) view.updateRooms(rooms);
                }, error -> {
                    if (view != null)
                        view.showError("Failed to get nearby rooms!");
                });

        getLocation
                .flatMap(loc -> {
                    List<Address> addressList = geocoder.getFromLocation(
                            loc.getLatitude(), loc.getLongitude(), 1);
                    return Observable.just(addressList);
                })
                .filter(addresses -> !addresses.isEmpty())
                .flatMap(addresses -> Observable.just(addresses.get(0)))
                .filter(this::checkForChangedAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(address -> {
                    Log.d("Refresh", address.toString());
                    this.address = address;
                    if (view != null) view.showLocation(address);
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
    public void onSortByLocationClicked() {
        sortOrder = NearbyContract.SortOrder.Location;
        onRefresh();
    }

    @Override
    public void onSortByPopularityClicked() {
        sortOrder = NearbyContract.SortOrder.Popularity;
        onRefresh();
    }
}
