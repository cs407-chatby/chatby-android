package io.github.cs407_chatby.chatby.ui.main.nearby;


import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.ui.auth.AuthActivity;
import io.github.cs407_chatby.chatby.ui.callbacks.ActionButtonListener;
import io.github.cs407_chatby.chatby.ui.callbacks.ScrollDirectionListener;
import io.github.cs407_chatby.chatby.ui.main.RoomAdapter;
import io.github.cs407_chatby.chatby.ui.main.account.AccountActivity;
import io.github.cs407_chatby.chatby.ui.main.create.CreateFragment;
import io.github.cs407_chatby.chatby.ui.room.RoomActivity;
import io.github.cs407_chatby.chatby.utils.ActivityUtils;
import io.github.cs407_chatby.chatby.utils.ViewUtils;

public class NearbyFragment extends Fragment implements NearbyContract.View, ActionButtonListener {

    RecyclerView roomList;
    CardView sortChip;
    CardView localChip;
    TextView sortText;
    TextView localText;
    View chips;

    @Inject
    @Nullable
    NearbyPresenter presenter;

    @Inject
    RoomAdapter roomAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);
        ((ChatByApp) getActivity().getApplication()).getComponent().inject(this);

        // Set views
        roomList = ViewUtils.findView(view, R.id.list_nearby);
        sortChip = ViewUtils.findView(view, R.id.sort_chip);
        localChip = ViewUtils.findView(view, R.id.location_chip);
        sortText = ViewUtils.findView(view, R.id.sort_text);
        localText = ViewUtils.findView(view, R.id.local_text);
        chips = ViewUtils.findView(view, R.id.chips);

        roomList.clearOnScrollListeners();
        roomList.addOnScrollListener(new ScrollDirectionListener() {
            @Override
            public void onScrollUp() {
                chips.animate()
                        .translationY(0)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(150)
                        .start();
            }

            @Override
            public void onScrollDown() {
                chips.animate()
                        .translationY(-200)
                        .setInterpolator(new AccelerateInterpolator())
                        .setDuration(150)
                        .start();
            }
        });

        if (roomList != null) {
            roomAdapter.setListener(room -> {
                if (presenter != null) presenter.onRoomClicked(room);
            });
            roomList.setAdapter(roomAdapter);
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
        if (presenter != null) presenter.onRefresh();
    }

    @Override
    public void onStop() {
        if (presenter != null) presenter.onDetach();
        super.onStop();
    }

    @Override
    public void updateRooms(List<Room> rooms) {
        roomList.setVisibility(View.VISIBLE);
        roomAdapter.setRooms(rooms);
    }

    @Override
    public void showLoading() {
        roomList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String message) {
        if (getView() != null)
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLocation(Address address) {
        if (address == null) {
            localText.setText(R.string.default_location_text);
            return;
        }
        String city = address.getLocality();
        String state = address.getAdminArea();
        String text = city + ", " + state;
        localText.setText(text);
    }

    @Override
    public void showSortOrder(NearbyContract.SortOrder order) {
        sortText.setText(order.title);

    }

    @Override
    public void openRoom(Bundle args) {
        ActivityUtils.start(getActivity(), RoomActivity.class, args, false);
    }

    @Override
    public void openAccount(Bundle args) {
        ActivityUtils.start(getActivity(), AccountActivity.class, args, false);
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
    public void openAuth() {
        ActivityUtils.start(getActivity(), AuthActivity.class, null, true);
    }

    public static NearbyFragment newInstance() {
        Bundle args = new Bundle();
        NearbyFragment fragment = new NearbyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void actionButtonClicked(View view) {
        if (presenter != null) presenter.onCreateClicked();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_nearby, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_location:
                if (presenter != null) presenter.onSortByLocationClicked();
                return true;
            case R.id.action_sort_popularity:
                if (presenter != null) presenter.onSortByPopularityClicked();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
