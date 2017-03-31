package io.github.cs407_chatby.chatby.ui.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.ui.ActionButtonListener;
import io.github.cs407_chatby.chatby.ui.main.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, HomeFragment.newInstance(), "Home")
                    .commit();
        }

        setupFab();
        setupBackStack();
    }

    protected void setupFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("Create");
            if (fragment == null)
                fragment = getSupportFragmentManager().findFragmentByTag("Home");
            ((ActionButtonListener) fragment).actionButtonClicked(fab);
        });
    }

    protected void setupBackStack() {
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                setTitle("ChatBy");
                fab.setImageResource(R.drawable.ic_add_24dp);
            } else {
                setTitle("Create Room");
                fab.setImageResource(R.drawable.ic_check_24dp);
            }
        });
    }
}
