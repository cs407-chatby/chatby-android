package io.github.cs407_chatby.chatby.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.cs407_chatby.chatby.ui.main.account.AccountFragment;
import io.github.cs407_chatby.chatby.ui.main.active.ActiveFragment;
import io.github.cs407_chatby.chatby.ui.main.nearby.NearbyFragment;


public class MainPagerAdapter extends FragmentPagerAdapter {

    private Fragment nearby = NearbyFragment.newInstance();
    private Fragment active = ActiveFragment.newInstance();
    private Fragment account = AccountFragment.newInstance(new Bundle());

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return nearby;
            case 1: return active;
            case 2: return account;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
