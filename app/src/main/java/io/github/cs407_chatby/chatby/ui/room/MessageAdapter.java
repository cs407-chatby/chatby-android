package io.github.cs407_chatby.chatby.ui.room;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.Message;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.utils.ViewUtils;


class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private OnLikeClickedListener listener;
    private List<Message> messages = new ArrayList<>();
    private User currentUser;

    @Inject
    public MessageAdapter() {}

    public void setCurrentUser(@NonNull User currentUser) {
        this.currentUser = currentUser;
        notifyDataSetChanged();
    }

    public void setMessages(@NonNull List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void addMessage(@NonNull Message message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void setOnLikeClickedListener(OnLikeClickedListener listener) {
        this.listener = listener;
    }

    public void removeOnLikeClickedListener() {
        this.listener = v -> {};
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: {
                View view = ViewUtils.inflate(parent, R.layout.layout_own_message);
                return new MessageViewHolder(view);
            }
            default: {
                View view = ViewUtils.inflate(parent, R.layout.layout_message);
                return new ReceivedMessageViewHolder(view);
            }
        }
    }

    private boolean hasSameCreators(Message a, Message b) {
        return a.getCreatedBy().equals(b.getCreatedBy());
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        int likes = message.getLikes().size();
        String count = "+" + likes;

        holder.messageText.setText(message.getContent());
        holder.counterText.setText(count);

        if (likes == 0) holder.counterText.setVisibility(View.INVISIBLE);

        if (holder instanceof ReceivedMessageViewHolder) {
            ReceivedMessageViewHolder h = (ReceivedMessageViewHolder) holder;
            h.likeToggle.setChecked(message.getLikes().contains(currentUser.getUrl()));

            if (position < messages.size() - 1 &&
                    hasSameCreators(messages.get(position + 1), message)) {
                h.profilePic.setVisibility(View.INVISIBLE);
            }

            h.messageText.setOnClickListener(v ->
                    ViewUtils.toggleExistence(h.likeToggle));
            h.likeToggle.setOnClickListener(v -> {
                h.likeToggle.setChecked(!h.likeToggle.isChecked());
                listener.onLike(message);
            });
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (currentUser != null) {
            if (messages.get(position).getCreatedBy().equals(currentUser.getUrl()))
                return 0;
        }
        return 1;
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView counterText;

        MessageViewHolder(View view) {
            super(view);
            messageText = ViewUtils.findView(view, R.id.text_message);
            counterText = ViewUtils.findView(view, R.id.like_counter);
        }
    }

    private static class ReceivedMessageViewHolder extends MessageViewHolder {
        ImageView profilePic;
        ToggleButton likeToggle;

        ReceivedMessageViewHolder(View view) {
            super(view);
            profilePic = ViewUtils.findView(view, R.id.creator_pic);
            likeToggle = ViewUtils.findView(view, R.id.like_toggle);
        }
    }

    interface OnLikeClickedListener {
        void onLike(Message message);
    }
}
