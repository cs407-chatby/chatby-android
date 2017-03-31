package io.github.cs407_chatby.chatby.ui.room.member;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.utils.ViewUtils;


public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private List<User> members = new ArrayList<>();
    private User currentUser;
    private Integer ownerId;

    private OnDeleteClickedListener listener;

    @Inject public MemberAdapter() {}

    public void setMembers(@NonNull List<User> members) {
        this.members = members;
        notifyDataSetChanged();
    }

    public void removeMember(User user) {
        int index = members.indexOf(user);
        if (index > 0) {
            members.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void setListener(OnDeleteClickedListener listener) {
        this.listener = listener;
    }

    public void setOwner(Integer owner) {
        this.ownerId = owner;
        notifyDataSetChanged();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        notifyDataSetChanged();
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MemberViewHolder(ViewUtils.inflate(parent, R.layout.layout_member));
    }

    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        User member = members.get(position);
        if (!currentUser.getId().equals(ownerId))
            holder.delete.setVisibility(View.INVISIBLE);
        if (member.getId().equals(ownerId))
            holder.delete.setVisibility(View.INVISIBLE);

        holder.name.setText(member.getUsername());

        holder.delete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClicked(member);
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Button delete;

        MemberViewHolder(View itemView) {
            super(itemView);
            name = ViewUtils.findView(itemView, R.id.name);
            delete = ViewUtils.findView(itemView, R.id.delete);
        }
    }

    public interface OnDeleteClickedListener {
        void onDeleteClicked(User user);
    }

}
