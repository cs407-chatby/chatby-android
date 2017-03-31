package io.github.cs407_chatby.chatby.ui.room.member;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.data.model.Membership;
import io.github.cs407_chatby.chatby.data.model.ResourceUrl;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.data.service.ChatByService;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class MemberListPresenter implements MemberListContract.Presenter {

    private final ChatByService service;
    private MemberListContract.View view;
    private Room room;

    @Inject public MemberListPresenter(ChatByService service) {
        this.service = service;
    }

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
        service.getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (view != null)
                        view.setCurrentUser(user);
                }, error -> {
                    if (view != null)
                        view.showError("Failed to get current user!");
                });

        service.getMembershipsForRoom(room.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .map(memberships -> {
                    List<User> userList = new ArrayList<>();
                    for (Membership m : memberships) {
                        userList.add(service.getUser(m.getUser().getId()).blockingGet());
                    }
                    return userList;
                })
                .subscribe(users -> {
                    if (view != null)
                        view.showMembers(users);
                }, error -> {
                    if (view != null)
                        view.showError("Failed to get member list!");
                });
    }

    @Override
    public void onDeletePressed(ResourceUrl memberUrl) {
        service.getMembershipsForUserInRoom(memberUrl.getId(), room.getId())
                .flatMap(memberships -> {
                    for (Membership m : memberships) {
                        Throwable t = service.deleteMembership(m.getId()).blockingGet();
                        if (t != null)
                            throw new RuntimeException(t);
                    }
                    return service.getUser(memberUrl.getId());
                })
                .subscribe(user -> {
                    if (view != null)
                        view.showMemberDeleted(user);
                }, error -> {
                    if (view != null)
                        view.showError("Failed to delete member!");
                });
    }
}
