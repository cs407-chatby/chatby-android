package io.github.cs407_chatby.chatby.ui.main.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.ui.ActionButtonListener;
import io.github.cs407_chatby.chatby.ui.main.RoomAdapter;
import io.github.cs407_chatby.chatby.ui.main.create.CreateFragment;
import io.github.cs407_chatby.chatby.utils.ViewUtils;

public class HomeFragment extends Fragment implements HomeContract.View, ActionButtonListener {

    RecyclerView roomList;

    @Inject
    @Nullable
    HomePresenter presenter;
    RoomAdapter roomAdapter = new RoomAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((ChatByApp) getActivity().getApplication()).getComponent().inject(this);

        roomList = ViewUtils.findView(view, R.id.list_created);
        Log.d("HomeFragment", "Creating view");
        if (roomList != null) {
            roomList.setAdapter(roomAdapter);
            Log.d("HomeFragment", "Adapter set");
        }
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
        if (presenter != null) presenter.onInitialize();
    }

    @Override
    public void onStop() {
        if (presenter != null) presenter.onDetach();
        super.onStop();
    }

    @Override
    public void updateCreated(List<Room> rooms) {
        roomAdapter.setCreated(rooms);
    }

    @Override
    public void updateNearby(List<Room> rooms) {
        roomAdapter.setNearby(rooms);
    }

    @Override
    public void showError(String message) {
        if (getView() != null)
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void openRoom() {
        // TODO Sprint 2
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

    @Override
    public void showNewAvailable() {
        // TODO Sprint 2-3
    }

    @Override
    public void hideNewAvailable() {
        // TODO Sprint 2-3
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void actionButtonClicked(View view) {
        if (presenter != null) presenter.onCreateClicked();
    }
}
