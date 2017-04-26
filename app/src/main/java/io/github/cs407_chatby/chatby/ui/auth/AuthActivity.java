package io.github.cs407_chatby.chatby.ui.auth;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;
import javax.inject.Named;

import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.di.ActivityComponent;
import io.github.cs407_chatby.chatby.di.ActivityModule;
import io.github.cs407_chatby.chatby.di.DaggerActivityComponent;
import io.github.cs407_chatby.chatby.utils.ActivityUtils;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.forms, AuthFragment.newInstance())
                    .commit();
        }

        boolean fineLocation = ActivityUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (!fineLocation) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().getFragments().get(0);
        if (fragment instanceof OnBackPressedListener) {
            if (((OnBackPressedListener) fragment).backPressed())
                return;
        }
        super.onBackPressed();
    }
}
