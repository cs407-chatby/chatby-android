package io.github.cs407_chatby.chatby.ui.auth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import io.github.cs407_chatby.chatby.R;

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
