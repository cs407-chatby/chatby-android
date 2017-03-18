package io.github.cs407_chatby.chatby.ui.room;


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
}
