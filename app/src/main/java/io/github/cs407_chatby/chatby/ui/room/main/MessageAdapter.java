package io.github.cs407_chatby.chatby.ui.room.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.Message;
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.utils.ViewUtils;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private OnLikeClickedListener listener;
    private List<Message> messages = new ArrayList<>();
    private User currentUser;

    int activePosition = -1;

    @Inject
    public MessageAdapter() {}

    public void setCurrentUser(@NonNull User currentUser) {
        this.currentUser = currentUser;
        notifyDataSetChanged();
    }

    public void updateMessage(@NonNull Message message) {
        int position = -1;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getUrl().equals(message.getUrl())) {
                position = i;
                break;
            }
        }
        if (position >= 0) {
            messages.remove(position);
            messages.add(position, message);
            notifyItemChanged(position);
        }
    }

    public void setMessages(@NonNull List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void addMessage(@NonNull Message message) {
        messages.add(0, message);
        notifyItemInserted(0);
    }

    public void setOnLikeClickedListener(OnLikeClickedListener listener) {
        this.listener = listener;
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
            if (currentUser != null)
                h.likeToggle.setChecked(message.getLikes().contains(currentUser.getUrl()));

            if (position > 0 &&
                    hasSameCreators(messages.get(position - 1), message)) {
                h.profilePic.setVisibility(View.INVISIBLE);
            } else {
                h.profilePic.setVisibility(View.VISIBLE);
            }

            if (position == activePosition) {
                h.likeToggle.setVisibility(View.VISIBLE);
                h.counterText.setVisibility(View.VISIBLE);
            } else {
                h.likeToggle.setVisibility(View.GONE);
            }

            h.root.setOnClickListener(v -> {
                int oldPosition = activePosition;
                activePosition = h.getAdapterPosition();
                notifyItemChanged(oldPosition);
                if (activePosition == oldPosition)
                    activePosition = -1;
                else
                    notifyItemChanged(activePosition);
            });
            h.likeToggle.setOnClickListener(v -> {
                int c = message.getLikes().size();
                if (h.likeToggle.isChecked()) c++;
                else c = Math.max(0, c-1);
                h.counterText.setText(String.format(Locale.US, "+%d", c));
                if (listener != null) listener.onLike(message);
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
        View root;
        ImageView profilePic;
        ToggleButton likeToggle;

        ReceivedMessageViewHolder(View view) {
            super(view);
            root = view;
            profilePic = ViewUtils.findView(view, R.id.creator_pic);
            likeToggle = ViewUtils.findView(view, R.id.like_toggle);
        }
    }

    interface OnLikeClickedListener {
        void onLike(Message message);
    }
}
