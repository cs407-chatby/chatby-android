package io.github.cs407_chatby.chatby.ui.main;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.ui.main.create.CreateFragment;
import io.github.cs407_chatby.chatby.utils.ActivityUtils;

import static android.view.ViewAnimationUtils.createCircularReveal;

public class MainActivity extends AppCompatActivity {

    public static final String CREATE_FRAGMENT = "Create Fragment";

    View root;
    ViewPager pager;
    FrameLayout createFrame;
    FloatingActionButton fab;
    BottomNavigationView bottomBar;

    MainPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.main_root);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.create_frame, CreateFragment.newInstance(), CREATE_FRAGMENT)
                .commit();

        setupFab();
        setupPager();
        setupBottomBar();
    }

    protected void setupFab() {
        fab = ActivityUtils.findView(this, R.id.fab);
        createFrame = ActivityUtils.findView(this, R.id.create_frame);

        fab.setOnClickListener(v -> {
            if (createFrame.getVisibility() == View.GONE)
                showRoomCreation();
        });
    }

    protected void setupPager() {
        pager = ActivityUtils.findView(this, R.id.pager);
        adapter = new MainPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);
        pager.clearOnPageChangeListeners();
    }

    protected void setupBottomBar() {
        bottomBar = ActivityUtils.findView(this, R.id.bottom_bar);

        bottomBar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_nearby:
                    showNearby();
                    return true;
                case R.id.action_active:
                    showActive();
                    return true;
                case R.id.action_account:
                    showAccount();
                    return true;
                default: return false;
            }
        });
        bottomBar.setSelectedItemId(R.id.action_nearby);

        pager.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            int deltaY = scrollY - oldScrollY;
            if (deltaY > 0) {
                bottomBar.animate().translationY(200).setDuration(200).start();
            } else {
                bottomBar.animate().translationY(0).setDuration(200).start();
            }
        });
    }

    private void showNearby() {
        pager.setCurrentItem(0, false);
        fab.show();
    }

    private void showActive() {
        pager.setCurrentItem(1, false);
        fab.show();
    }

    private void showAccount() {
        pager.setCurrentItem(2, false);
        fab.hide();
    }

    private void showRoomCreation() {
        int fabX = Float.valueOf(fab.getX() + (fab.getWidth() / 2)).intValue();
        int fabY = Float.valueOf(fab.getY() + (fab.getHeight() / 2)).intValue();
        float size = (float) Math.hypot(root.getHeight(), root.getWidth());
        createFrame.setVisibility(View.VISIBLE);
        Animator animator = createCircularReveal(createFrame, fabX, fabY, 0, size)
                .setDuration(400);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        View createFrame = findViewById(R.id.create_frame);
        if (createFrame.getVisibility() == View.VISIBLE) {
            int fabX = Float.valueOf(fab.getX() + (fab.getWidth() / 2)).intValue();
            int fabY = Float.valueOf(fab.getY() + (fab.getHeight() / 2)).intValue();
            float size = (float) Math.hypot(root.getHeight(), root.getWidth());
            Animator animator = ViewAnimationUtils.createCircularReveal(createFrame, fabX, fabY, size, 0)
                    .setDuration(400);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) {
                    createFrame.setVisibility(View.GONE);
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            animator.start();
            fab.setVisibility(View.VISIBLE);
        } else super.onBackPressed();
    }
}
