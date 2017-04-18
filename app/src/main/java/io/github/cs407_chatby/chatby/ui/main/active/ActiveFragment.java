package io.github.cs407_chatby.chatby.ui.main.active;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.ui.auth.AuthActivity;
import io.github.cs407_chatby.chatby.ui.callbacks.ActionButtonListener;
import io.github.cs407_chatby.chatby.ui.main.create.CreateFragment;
import io.github.cs407_chatby.chatby.ui.room.RoomActivity;
import io.github.cs407_chatby.chatby.utils.ActivityUtils;
import io.github.cs407_chatby.chatby.utils.ViewUtils;


public class ActiveFragment extends Fragment implements ActiveContract.View, ActionButtonListener {

    RecyclerView activeList;

    @Inject
    @Nullable
    ActivePresenter presenter;

    @Inject
    ActiveAdapter activeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active, container, false);
        ((ChatByApp) getActivity().getApplication()).getComponent().inject(this);

        activeList = ViewUtils.findView(view, R.id.list_active);
        activeAdapter.setListener(room -> {
            if (presenter != null) presenter.onRoomClicked(room);
        });
        activeList.setAdapter(activeAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) presenter.onAttach(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.onRefresh();
    }

    @Override
    public void onStop() {
        if (presenter != null) presenter.onDetach();
        super.onStop();
    }

    @Override
    public void actionButtonClicked(View view) {
        showRoomCreation();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_nearby, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void showCreated(List<Room> created) {
        activeAdapter.setCreated(created);
    }

    @Override
    public void showFavorite(List<Room> favorite) {
        activeAdapter.setFavorite(favorite);
    }

    @Override
    public void showActive(List<Room> active) {
        activeAdapter.setActive(active);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(String message) {
        if (getView() != null)
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void openRoom(Bundle args) {
        ActivityUtils.start(getActivity(), RoomActivity.class, args, false);
    }

    @Override
    public void openAuth() {
        ActivityUtils.start(getActivity(), AuthActivity.class, null, true);
    }

    @Override
    public void showRoomCreation() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_up, R.anim.slide_out_down)
                .add(R.id.frame, CreateFragment.newInstance(), "Create")
                .commit();
    }

    public static ActiveFragment newInstance() {
        return new ActiveFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_location:
                return true;
            case R.id.action_sort_popularity:
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
