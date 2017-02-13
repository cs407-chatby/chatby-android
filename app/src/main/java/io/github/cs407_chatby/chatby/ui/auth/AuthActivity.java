package io.github.cs407_chatby.chatby.ui.auth;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;
import javax.inject.Named;

import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.di.ActivityComponent;
import io.github.cs407_chatby.chatby.di.ActivityModule;
import io.github.cs407_chatby.chatby.di.DaggerActivityComponent;

public class AuthActivity extends AppCompatActivity {

    @Inject @Named("Application")
    Context context;

    private ActivityComponent activityComponent;

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
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(ChatByApp.get(this).getComponent())
                    .build();
        }
        return activityComponent;
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
