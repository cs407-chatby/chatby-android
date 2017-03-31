package io.github.cs407_chatby.chatby.ui.room.member;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.utils.ViewUtils;


public class MemberAdapter extends RecyclerView.Adapter {

    @Inject public MemberAdapter() {}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Button delete;

        public MemberViewHolder(View itemView) {
            super(itemView);
            name = ViewUtils.findView(itemView, R.id.name);
            delete = ViewUtils.findView(itemView, R.id.delete);
        }
    }

}
