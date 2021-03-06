package io.github.cs407_chatby.chatby.ui.room;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.ui.room.main.RoomFragment;

public class RoomActivity extends AppCompatActivity {

    public static final String ROOM_ID = "RoomId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(getIntent().getIntExtra(ROOM_ID, -1));

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, RoomFragment.newInstance(getIntent().getExtras()), "Room")
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);


        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            invalidateOptionsMenu();
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                if (actionBar != null)
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            } else {
                if (actionBar != null)
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return true;
                }
                else {
                    onBackPressed();
                    return true;
                }
            }
            default: return super.onOptionsItemSelected(item);
        }
    }
}
