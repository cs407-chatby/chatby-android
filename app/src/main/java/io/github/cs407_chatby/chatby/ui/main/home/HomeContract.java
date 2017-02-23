package io.github.cs407_chatby.chatby.ui.main.home;


import java.util.List;

import io.github.cs407_chatby.chatby.data.model.Room;

class HomeContract {

    interface Presenter {
        void onAttach(View view);
        void onDetach();
        void onCreateClicked();
        void onRoomClicked(Room room);
        void onInitialize();
    }

    interface View {
        void updateCreated(List<Room> rooms);
        void updateNearby(List<Room> rooms);
        void showError(String message);
        void openRoom();
        void showRoomCreation();
        void showNewAvailable();
        void hideNewAvailable();
    }
}
