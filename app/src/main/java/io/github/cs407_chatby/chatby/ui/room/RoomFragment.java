package io.github.cs407_chatby.chatby.ui.room;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.Message;
import io.github.cs407_chatby.chatby.data.model.Room;

public class RoomFragment extends Fragment implements RoomContract.View {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_room, container, false);
    }

    @Override
    public void showMessages(List<Message> messages) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showLoading() {

    }

    public static RoomFragment newInstance(Room room) {
        Bundle args = new Bundle();
        RoomFragment fragment = new RoomFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
