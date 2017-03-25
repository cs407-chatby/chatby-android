package io.github.cs407_chatby.chatby.ui.room;


import io.github.cs407_chatby.chatby.data.model.ResourceUrl;

public class RoomPresenter implements RoomContract.Presenter {
    private RoomContract.View view = null;

    @Override
    public void onAttach(RoomContract.View view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    @Override
    public void onSendPressed(String message) {

    }

    @Override
    public void onMessageLikePressed(ResourceUrl url) {

    }
}
