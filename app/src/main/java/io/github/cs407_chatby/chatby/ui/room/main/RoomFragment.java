package io.github.cs407_chatby.chatby.ui.room.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.firebase.MessagingService;
import io.github.cs407_chatby.chatby.ui.room.RoomActivity;
import io.github.cs407_chatby.chatby.ui.room.member.MemberListFragment;
import io.github.cs407_chatby.chatby.ui.viewModel.ViewMessage;
import io.github.cs407_chatby.chatby.utils.ActivityUtils;
import io.github.cs407_chatby.chatby.utils.ViewUtils;

public class RoomFragment extends Fragment implements RoomContract.View {

    @Inject RoomPresenter presenter;
    @Inject MessageAdapter adapter;
    @MenuRes int menuLayout = R.menu.menu_room;
    boolean starred = false;

    RecyclerView messageList;
    EditText messageForm;
    FloatingActionButton sendButton;
    TextView expirationText;
    Button joinButton;
    ProgressBar progressBar;
    View emptyView;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle args = getArguments();
            if (args != null && presenter != null)
                presenter.onRefresh(args.getInt(RoomActivity.ROOM_ID));
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((ChatByApp) getActivity().getApplication()).getComponent().inject(this);

        View view = inflater.inflate(R.layout.fragment_room, container, false);
        messageList = ViewUtils.findView(view, R.id.message_list);
        messageForm = ViewUtils.findView(view, R.id.new_message_text);
        sendButton = ViewUtils.findView(view, R.id.fab);
        expirationText = ViewUtils.findView(view, R.id.expiration_text);
        joinButton = ViewUtils.findView(view, R.id.join_button);
        progressBar = ViewUtils.findView(view, R.id.loading_view);
        emptyView = ViewUtils.findView(view, R.id.empty_view);

        joinButton.setOnClickListener(v -> presenter.onJoinRoomPressed());
        sendButton.setOnClickListener(v -> presenter.onSendPressed(messageForm.getText().toString()));
        adapter.setOnLikeClickedListener(message -> presenter.onMessageLikePressed(message));
        adapter.setOnItemInsertedListener(() -> messageList.smoothScrollToPosition(0));

        messageList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onAttach(this);
        getActivity().registerReceiver(receiver,
                new IntentFilter(MessagingService.ACTION_MESSAGE_RECEIVED));
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if (args != null && presenter != null)
            presenter.onInitialize(args.getInt(RoomActivity.ROOM_ID));
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(receiver);
        if (presenter != null) presenter.onDetach();
        super.onStop();
    }

    @Override
    public void showRoom(Room room) {
        getActivity().setTitle(room.getName());
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d 'at' h:mm", Locale.US);
        expirationText.setText(formatter.format(room.getExpireTime()));
    }

    @Override
    public void showMessages(List<ViewMessage> messages) {
        messageList.setVisibility(View.VISIBLE);
        adapter.setMessages(messages);
    }

    @Override
    public void showMessageSent(ViewMessage message) {
        messageList.setVisibility(View.VISIBLE);
        adapter.addMessage(message);
        messageForm.setText("");
        messageList.scrollToPosition(0);
    }

    @Override
    public void showMessageUpdated(ViewMessage message) {
        adapter.updateMessage(message);
    }

    @Override
    public void showError(String error) {
        if (getView() != null)
            Snackbar.make(getView(), error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        messageList.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmpty() {
        messageList.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showStarred() {
        starred = true;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showNotStarred() {
        starred = false;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showJoined() {
        joinButton.animate()
                .translationY((float) ActivityUtils.dpToPixel(getActivity(), 64))
                .setDuration(300)
                .withEndAction(() -> joinButton.setVisibility(View.GONE))
                .start();
        menuLayout = R.menu.menu_joined_room;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showJoining() {

    }

    @Override
    public void showNotJoined() {
        joinButton.animate()
                .translationY(0.0f)
                .setDuration(300)
                .withStartAction(() -> joinButton.setVisibility(View.VISIBLE))
                .start();
        menuLayout = R.menu.menu_room;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void setCurrentUser(User user) {
        Log.d("Room", "Current user loaded");
        adapter.setCurrentUser(user);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            inflater.inflate(menuLayout, menu);
            MenuItem item = menu.findItem(R.id.action_star);
            if (item != null && starred)
                item.setTitle("Remove from Favorites");
            else if (item != null)
                item.setTitle("Add to Favorites");
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_members: {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down,
                                R.anim.slide_in_up, R.anim.slide_out_down)
                        .add(R.id.frame, MemberListFragment.newInstance(getArguments()))
                        .setBreadCrumbShortTitle("Members")
                        .addToBackStack(null)
                        .commit();
                return true;
            }
            case R.id.action_star: {
                presenter.onRoomStarClicked();
                return true;
            }
            case R.id.action_refresh: {
                presenter.onRefresh(getArguments().getInt(RoomActivity.ROOM_ID));
                return true;
            }
            case R.id.action_leave: {
                presenter.onLeaveRoomPressed();
                Log.d("Room", "Leaving room");
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }

    public static RoomFragment newInstance(Bundle args) {
        RoomFragment fragment = new RoomFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
