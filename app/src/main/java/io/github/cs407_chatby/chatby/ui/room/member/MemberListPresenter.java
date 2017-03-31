package io.github.cs407_chatby.chatby.ui.room.member;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.model.Room;


public class MemberListPresenter implements MemberListContract.Presenter {
    MemberListContract.View view;
    Room room;

    @Inject public MemberListPresenter() {}

    @Override
    public void onAttach(MemberListContract.View view, Room room) {
        this.view = view;
        this.room = room;
    }

    @Override
    public void onDetach() {
        this.view = null;
        this.room = null;
    }

    @Override
    public void onInitialize() {
        // TODO get members and current user
    }

    @Override
    public void onDeletePressed(ResourceUrl memberUrl) {
        // TODO delete member from room
    }
}
