package io.github.cs407_chatby.chatby.ui.room.main;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
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
import io.github.cs407_chatby.chatby.data.model.User;
import io.github.cs407_chatby.chatby.ui.viewModel.ViewMessage;
import io.github.cs407_chatby.chatby.utils.ViewUtils;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private OnLikeClickedListener listener;
    private OnItemInsertedListener insertedListener;
    private List<ViewMessage> messages = new ArrayList<>();
    private User currentUser;
    private int activePosition = -1;

    @Inject
    public MessageAdapter() {}

    public void setCurrentUser(@NonNull User currentUser) {
        this.currentUser = currentUser;
        notifyDataSetChanged();
    }

    public void updateMessage(@NonNull ViewMessage message) {
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

    public void setMessages(@NonNull List<ViewMessage> messages) {
        final RecyclerView.Adapter adapter = this;
        DiffUtil.calculateDiff(new DiffMessageCallback(this.messages, messages))
                .dispatchUpdatesTo(new ListUpdateCallback() {
                    @Override
                    public void onInserted(int position, int count) {
                        adapter.notifyItemRangeInserted(position, count);
                        if ((position + count) < messages.size())
                            adapter.notifyItemChanged(position + count);
                        if (position > 0)
                            adapter.notifyItemChanged(position - 1);
                        insertedListener.itemInserted();
                    }
                    @Override
                    public void onRemoved(int position, int count) {
                        adapter.notifyItemRangeRemoved(position, count);
                    }
                    @Override
                    public void onMoved(int fromPosition, int toPosition) {
                        adapter.notifyItemMoved(fromPosition, toPosition);
                    }
                    @Override
                    public void onChanged(int position, int count, Object payload) {
                        adapter.notifyItemRangeChanged(position, count, payload);
                    }
                });
        this.messages = messages;
    }

    public void addMessage(@NonNull ViewMessage message) {
        messages.add(0, message);
        notifyItemInserted(0);
        if (messages.size() > 1) notifyItemChanged(1);
    }

    public void setOnLikeClickedListener(OnLikeClickedListener listener) {
        this.listener = listener;
    }

    public void setOnItemInsertedListener(OnItemInsertedListener listener) {
        this.insertedListener = listener;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0: {
                View view = ViewUtils.inflate(parent, R.layout.layout_own_message);
                return new MessageViewHolder(view);
            }
            default: {
                View view = ViewUtils.inflate(parent, R.layout.layout_new_message);
                return new ReceivedMessageViewHolder(view);
            }
        }
    }

    private boolean hasTopNeighbor(int position) {
        if (position >= messages.size() - 1) return false;
        ViewMessage top = messages.get(position + 1);
        ViewMessage self = messages.get(position);
        int topId = top.getCreatedBy().getId();
        int selfId = self.getCreatedBy().getId();
        return topId == selfId && (top.getAnonymous() == self.getAnonymous());
    }

    private boolean hasBottomNeighbor(int position) {
        if (position <= 0) return false;
        ViewMessage bottom = messages.get(position - 1);
        ViewMessage self = messages.get(position);
        int bottomId = bottom.getCreatedBy().getId();
        int selfId = self.getCreatedBy().getId();
        return bottomId == selfId && (bottom.getAnonymous() == self.getAnonymous());
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        ViewMessage message = messages.get(position);
        int likes = message.getLikes().size();
        String count = "+" + likes;

        holder.messageText.setText(message.getContent());
        holder.counterText.setText(count);

        if (likes == 0) holder.counterText.setVisibility(View.INVISIBLE);
        else holder.counterText.setVisibility(View.VISIBLE);

        if (holder instanceof ReceivedMessageViewHolder) {
            ReceivedMessageViewHolder h = (ReceivedMessageViewHolder) holder;
            if (message.getAnonymous())
                h.name.setText("Anonymous");
            else
                h.name.setText(message.getCreatedBy().getUsername());

            // Check if current user has liked the message
            if (currentUser != null)
                h.likeToggle.setChecked(message.getLikes().contains(currentUser.getUrl()));

            if (hasTopNeighbor(position) && hasBottomNeighbor(position)) {
                h.name.setVisibility(View.GONE);
                h.messageLayout.setBackgroundResource(R.drawable.ic_middle_message_bg);
                h.profilePic.setVisibility(View.INVISIBLE);
                h.space.setVisibility(View.GONE);
            } else if (hasTopNeighbor(position)) {
                h.name.setVisibility(View.GONE);
                h.messageLayout.setBackgroundResource(R.drawable.ic_bottom_message_bg);
                h.profilePic.setVisibility(View.VISIBLE);
                if (position > 0) h.space.setVisibility(View.VISIBLE);
                else h.space.setVisibility(View.GONE);
            } else if (hasBottomNeighbor(position)) {
                h.name.setVisibility(View.VISIBLE);
                h.messageLayout.setBackgroundResource(R.drawable.ic_top_message_bg);
                h.profilePic.setVisibility(View.INVISIBLE);
                h.space.setVisibility(View.GONE);
            } else {
                h.name.setVisibility(View.VISIBLE);
                h.messageLayout.setBackgroundResource(R.drawable.ic_single_message_bg);
                h.profilePic.setVisibility(View.VISIBLE);
                if (position > 0) h.space.setVisibility(View.VISIBLE);
                else h.space.setVisibility(View.GONE);
            }

            // Check if message has been clicked on for more info
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
        } else {
            if (hasTopNeighbor(position) && hasBottomNeighbor(position))
                holder.messageLayout.setBackgroundResource(R.drawable.ic_middle_sent_bg);
            else if (hasTopNeighbor(position))
                holder.messageLayout.setBackgroundResource(R.drawable.ic_bottom_sent_bg);
            else if (hasBottomNeighbor(position))
                holder.messageLayout.setBackgroundResource(R.drawable.ic_top_sent_bg);
            else
                holder.messageLayout.setBackgroundResource(R.drawable.ic_single_message_bg);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (currentUser != null) {
            if (messages.get(position).getCreatedBy().equals(currentUser))
                return 0;
        }
        return 1;
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView counterText;
        View messageLayout;

        MessageViewHolder(View view) {
            super(view);
            messageText = ViewUtils.findView(view, R.id.text_message);
            counterText = ViewUtils.findView(view, R.id.like_counter);
            messageLayout = ViewUtils.findView(view, R.id.layout_message);
        }
    }

    private static class ReceivedMessageViewHolder extends MessageViewHolder {
        View root;
        ImageView profilePic;
        ToggleButton likeToggle;
        TextView name;
        View space;

        ReceivedMessageViewHolder(View view) {
            super(view);
            root = view;
            profilePic = ViewUtils.findView(view, R.id.creator_pic);
            likeToggle = ViewUtils.findView(view, R.id.like_toggle);
            name = ViewUtils.findView(view, R.id.text_name);
            space = ViewUtils.findView(view, R.id.extra_padding);
        }
    }

    interface OnLikeClickedListener {
        void onLike(ViewMessage message);
    }

    interface OnItemInsertedListener {
        void itemInserted();
    }
}
