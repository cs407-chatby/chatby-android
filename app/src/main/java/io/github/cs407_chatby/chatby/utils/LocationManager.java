package io.github.cs407_chatby.chatby.utils;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class LocationManager implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final PublishSubject<Location> latestLocation = PublishSubject.create();
    private final GoogleApiClient googleApiClient;
    private final Context context;

    @Inject
    public LocationManager(@Named("Application") Context context) {
        this.context = context;
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void start() {
        googleApiClient.connect();
    }

    public void stop() {
        googleApiClient.disconnect();
    }

    public Observable<Location> getObservable() {
        return latestLocation.share();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("googleapi", "connected");

        boolean fineLocation = ActivityUtils.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        if (fineLocation) {
            FusedLocationProviderApi locationApi = LocationServices.FusedLocationApi;
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            Log.e("location", "No location permission!");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("googleapi", "connection suspended");
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("googleapi", connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("location", location.toString());
        latestLocation.onNext(location);
    }
}
