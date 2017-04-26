package io.github.cs407_chatby.chatby.ui.room.member;


import java.util.List;

import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.model.User;

public class MemberListContract {

    public interface Presenter {
        void onAttach(View view);
        void onDetach();
        void onInitialize(int roomId);
        void onDeletePressed(ResourceUrl memberUrl);
    }

    public interface View {
        void showRoom(Room room);
        void showMembers(List<User> members);
        void showLoading();
        void showError(String error);
        void showMemberDeleted(User user);
        void setCurrentUser(User user);
    }
}
