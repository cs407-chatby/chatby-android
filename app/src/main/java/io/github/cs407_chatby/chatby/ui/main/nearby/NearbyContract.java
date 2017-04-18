package io.github.cs407_chatby.chatby.ui.main.nearby;


import android.location.Address;
import android.os.Bundle;

import java.util.List;

import io.github.cs407_chatby.chatby.data.model.Room;

class NearbyContract {

    interface Presenter {
        void onAttach(View view);
        void onDetach();
        void onCreateClicked();
        void onRoomClicked(Room room);
        void onRefresh();
        void onLogout();
        void onDeleteAccount();
        void onAccountSettingsPressed();
        void onSortByLocationClicked();
        void onSortByPopularityClicked();
    }

    interface View {
        void updateRooms(List<Room> rooms);
        void showLoading();
        void showError(String message);
        void showLocation(Address address);
        void showSortOrder(SortOrder order);
        void openRoom(Bundle args);
        void openAccount(Bundle args);
        void showRoomCreation();
        void openAuth();
    }

    enum SortOrder {
        Popularity("Popular"), Location("Nearby");

        public String title;

        SortOrder(String title) {
            this.title = title;
        }
    }
}
