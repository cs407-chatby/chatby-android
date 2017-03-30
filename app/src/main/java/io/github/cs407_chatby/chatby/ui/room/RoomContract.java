package io.github.cs407_chatby.chatby.ui.room;


import java.util.List;

import io.github.cs407_chatby.chatby.data.model.Message;
import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.model.User;

public class RoomContract {


    public interface Presenter {
        void onAttach(View view, Room room);
        void onInitialize();
        void onDetach();
        void onSendPressed(String message);
        void onMessageLikePressed(ResourceUrl url);
        void onJoinRoomPressed();
        void onLeaveRoomPressed();
    }

    public interface View {
        void showMessages(List<Message> messages);
        void showMessageSent(Message message);
        void showError(String error);
        void showLoading();
        void hideLoading();
        void showEmpty();
        void showJoined();
        void showJoining();
        void showNotJoined();
        void setCurrentUser(User user);
    }
}
