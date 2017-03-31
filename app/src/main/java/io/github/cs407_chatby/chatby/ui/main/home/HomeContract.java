package io.github.cs407_chatby.chatby.ui.main.home;


import android.os.Bundle;

import java.util.List;

import io.github.cs407_chatby.chatby.data.model.Room;

class HomeContract {

    interface Presenter {
        void onAttach(View view);
        void onDetach();
        void onCreateClicked();
        void onRoomClicked(Room room);
        void onInitialize();
        void onLogout();
        void onDeleteAccount();
        void onAccountSettingsPressed();
    }

    interface View {
        void updateCreated(List<Room> rooms);
        void updateNearby(List<Room> rooms);
        void showError(String message);
        void openRoom(Bundle args);
        void openAccount(Bundle args);
        void showRoomCreation();
        void openAuth();
        void showNewAvailable();
        void hideNewAvailable();
    }
}
