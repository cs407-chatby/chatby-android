package io.github.cs407_chatby.chatby.ui.main.active;


import android.os.Bundle;

import java.util.List;

import io.github.cs407_chatby.chatby.data.model.Room;

public class ActiveContract {

    public interface Presenter {
        void onAttach(View view);
        void onDetach();
        void onRefresh();
        void onRoomClicked(Room room);
        void onStarRoomClicked(Room room);
        void onLeaveRoomClicked(Room room);
    }

    public interface View {
        void showCreated(List<Room> created);
        void showFavorite(List<Room> favorite);
        void showActive(List<Room> active);
        void showLoading();
        void showError(String message);
        void openRoom(Bundle args);
        void openAuth();
        void showRoomCreation();
    }
}
