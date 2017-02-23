package io.github.cs407_chatby.chatby.ui.home;


import java.util.List;

import io.github.cs407_chatby.chatby.data.model.PostRoom;
import io.github.cs407_chatby.chatby.data.model.Room;

class HomeContract {

    interface Presenter {
        void onAttach(View view);
        void onDetach();
        void onCreateClicked();
        void onFinalizeClicked(PostRoom room);
        void onRoomClicked(Room room);
        void onRefreshClicked();
    }

    interface View {
        void updateCreated(List<Room> rooms);
        void updateNearby(List<Room> rooms);
        void showError(String message);
        void openRoom();
        void showRoomCreation(PostRoom defaults);
        void hideRoomCreation();
        void showRoomFinalized();
        void showNewAvailable();
        void hideNewAvailable();
    }
}
