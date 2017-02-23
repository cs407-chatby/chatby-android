package io.github.cs407_chatby.chatby.ui.home;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
import io.github.cs407_chatby.chatby.data.model.PostRoom;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.utils.ViewUtils;

public class HomeFragment extends Fragment implements HomeContract.View {

    RecyclerView roomList;
    RoomAdapter roomAdapter = new RoomAdapter();

    @Inject
    @Nullable
    HomePresenter presenter;

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

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (presenter != null) presenter.onAttach(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.onRefreshClicked();
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

    }

    @Override
    public void openRoom() {

    }

    @Override
    public void showRoomCreation(PostRoom defaults) {

    }

    @Override
    public void hideRoomCreation() {

    }

    @Override
    public void showRoomFinalized() {

    }

    @Override
    public void showNewAvailable() {

    }

    @Override
    public void hideNewAvailable() {

    }
}
