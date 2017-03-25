package io.github.cs407_chatby.chatby.ui.room;


import java.util.List;

import io.github.cs407_chatby.chatby.data.model.Message;
import io.github.cs407_chatby.chatby.data.model.ResourceUrl;

public class RoomContract {


    public interface Presenter {
        void onAttach(View view);
        void onDetach();
        void onSendPressed(String message);
        void onMessageLikePressed(ResourceUrl url);
    }

    public interface View {
        void showMessages(List<Message> messages);
        void showError(String error);
        void showLoading();
    }
}
