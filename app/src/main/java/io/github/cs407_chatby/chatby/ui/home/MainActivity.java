package io.github.cs407_chatby.chatby.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import io.github.cs407_chatby.chatby.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, HomeFragment.newInstance())
                    .commit();
        }

        initFab();
    }

    protected void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context context = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Fab clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
