package io.github.cs407_chatby.chatby.ui.room.main;


import java.util.List;

import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.ui.viewModel.ViewMessage;

public class RoomContract {


    public interface Presenter {
        void onAttach(View view);
        void onInitialize(int roomId);
        void onRefresh(int roomId);
        void onDetach();
        void onSendPressed(String message);
        void onMessageLikePressed(ViewMessage likedMessage);
        void onRoomStarClicked();
        void onJoinRoomPressed();
        void onLeaveRoomPressed();
    }

    public interface View {
        void showRoom(Room room);
        void showMessages(List<ViewMessage> messages);
        void showMessageSent(ViewMessage message);
        void showMessageUpdated(ViewMessage message);
        void showError(String error);
        void showLoading();
        void showEmpty();
        void showStarred();
        void showNotStarred();
        void showJoined();
        void showJoining();
        void showNotJoined();
        void setCurrentUser(User user);
    }
}
