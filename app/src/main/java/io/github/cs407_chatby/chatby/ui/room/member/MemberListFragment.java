package io.github.cs407_chatby.chatby.ui.room.member;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.ChatByApp;
import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.utils.ViewUtils;

public class MemberListFragment extends Fragment implements MemberListContract.View {

    @Inject
    MemberListPresenter presenter;
    @Inject
    MemberAdapter adapter;

    RecyclerView memberList;
    Room room;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((ChatByApp) getActivity().getApplication()).getComponent().inject(this);
        room = new Gson().fromJson(getArguments().getString("room"), Room.class);
        View view = inflater.inflate(R.layout.fragment_member_list, container, false);
        memberList = ViewUtils.findView(view, R.id.member_list);
        if (memberList != null && adapter != null) {
            adapter.setOwner(room.getCreatedBy().getId());
            adapter.setListener(user -> {
                if (presenter != null) presenter.onDeletePressed(user.getUrl());
            });
            memberList.setAdapter(adapter);
            memberList.setLayoutManager(new LinearLayoutManager(memberList.getContext()));
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (room != null && presenter != null) presenter.onAttach(this, room);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.onInitialize();
    }

    @Override
    public void onStop() {
        if (presenter != null) presenter.onDetach();
        super.onStop();
    }

    @Override
    public void showMembers(List<User> members) {
        if (adapter != null) adapter.setMembers(members);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(String error) {
        if (getView() != null)
            Snackbar.make(getView(), error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showMemberDeleted(User user) {
        showError(user.getUsername() + " successfully removed");
        if (adapter != null) adapter.removeMember(user);
    }

    @Override
    public void setCurrentUser(User user) {
        if (adapter != null) adapter.setCurrentUser(user);
    }

    public static MemberListFragment newInstance(Bundle args) {
        MemberListFragment fragment = new MemberListFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
