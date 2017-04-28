package io.github.cs407_chatby.chatby.ui.room.main;


import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import io.github.cs407_chatby.chatby.ui.viewModel.ViewMessage;

public class DiffMessageCallback extends DiffUtil.Callback {

    private List<ViewMessage> oldMessages;
    private List<ViewMessage> newMessages;

    public DiffMessageCallback(List<ViewMessage> oldMessages, List<ViewMessage> newMessages) {
        this.oldMessages = oldMessages;
        this.newMessages = newMessages;
    }

    @Override
    public int getOldListSize() {
        return oldMessages.size();
    }

    @Override
    public int getNewListSize() {
        return newMessages.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMessages.get(oldItemPosition).getId()
                .equals(newMessages.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldMessages.get(oldItemPosition).equals(newMessages.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
