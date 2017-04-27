package io.github.cs407_chatby.chatby.ui.room.member;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MemberListPresenter implements MemberListContract.Presenter {

    private final ChatByService service;
    private MemberListContract.View view;
    private Room room;

    @Inject public MemberListPresenter(ChatByService service) {
        this.service = service;
    }

    @Override
    public void onAttach(MemberListContract.View view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        this.view = null;
        this.room = null;
    }

    @Override
    public void onInitialize(int roomId) {
        if (view != null) view.showLoading();

        service.getRoom(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(room -> {
                    this.room = room;
                    if (view != null) view.showRoom(room);
                    loadUserInfo();
                    loadMemberships();
                });
    }

    private void loadUserInfo() {
        service.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (view != null)
                        view.setCurrentUser(user);
                }, error -> {
                    if (view != null)
                        view.showError("Failed to get current user!");
                });
    }

    private void loadMemberships() {
        service.getMembershipsForRoom(room.getId())
                .toObservable()
                .flatMap(Observable::fromIterable)
                .flatMapSingle(membership -> service.getUser(membership.getUser().getId()))
                .distinct()
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    if (view != null) view.showMembers(users);
                }, error -> {
                    if (view != null) view.showError("Failed to get member list!");
                });
    }

    @Override
    public void onDeletePressed(ResourceUrl memberUrl) {
        service.getMembershipsForUserInRoom(memberUrl.getId(), room.getId())
                .toObservable()
                .flatMap(Observable::fromIterable)
                .flatMapSingle(membership -> service.deleteMembership(membership.getId())
                        .andThen(service.getUser(membership.getUser().getId())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (view != null) view.showMemberDeleted(user);
                }, error -> {
                    if (view != null) view.showError("Failed to delete member!");
                });
    }
}
