package io.github.cs407_chatby.chatby.ui.main.active;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.ui.main.RoomAdapter;
import io.github.cs407_chatby.chatby.utils.ViewUtils;

public class ActiveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Room> list1 = new ArrayList<>();
    private List<Room> list2 = new ArrayList<>();
    private List<Room> list3 = new ArrayList<>();

    private RoomAdapter.Listener listener = room -> {};

    @Inject
    public ActiveAdapter() {}

    public void setCreated(List<Room> created) {
        this.list2 = created;
        notifyDataSetChanged();
    }

    public void setFavorite(List<Room> favorite) {
        this.list1 = favorite;
        notifyDataSetChanged();
    }

    public void setActive(List<Room> active) {
        this.list3 = active;
        notifyDataSetChanged();
    }

    public void setListener(RoomAdapter.Listener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ViewUtils.inflate(parent, viewType);
        if (viewType == R.layout.layout_room) return new RoomAdapter.RoomViewHolder(view);
        else if (viewType == R.layout.layout_no_rooms) return new EmptyViewHolder(view);
        return new HeaderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int start1 = 0;
        int start2 = Math.max(list1.size(), 1) + 1;
        int start3 = start2 + Math.max(list2.size(), 1) + 1;

        boolean end = false;

        if (holder instanceof HeaderViewHolder) {

            HeaderViewHolder h = (HeaderViewHolder) holder;

            if (position == start1) {
                h.icon.setBackgroundResource(R.drawable.ic_stars_24dp);
                h.title.setText("Favorite");
            } else if (position == start2) {
                h.icon.setBackgroundResource(R.drawable.ic_created_24dp);
                h.title.setText("Created");
            } else {
                h.icon.setBackgroundResource(R.drawable.ic_inbox_24dp);
                h.title.setText("Active");
            }

        } else if (holder instanceof EmptyViewHolder) {

            EmptyViewHolder h = (EmptyViewHolder) holder;

            if (position < start2) {
                h.title.setText("No favorite rooms");
                h.explanation.setText("Rooms added to favorites will show here.");
            } else if (position < start3) {
                h.title.setText("No created rooms");
                h.explanation.setText("Rooms that you create will show here.");
            } else {
                h.title.setText("No other active rooms");
                h.explanation.setText("Any other rooms that you have joined will show here.");
            }

        } else if (holder instanceof RoomAdapter.RoomViewHolder) {

            RoomAdapter.RoomViewHolder h = (RoomAdapter.RoomViewHolder) holder;

            if (position < start2) {
                if (position == start2 - 1) end = true;
                bindRoom(h, list1.get(position - 1), end);
            } else if (position < start3) {
                if (position == start3 - 1) end = true;
                bindRoom(h, list2.get(position - (start2 + 1)), end);
            } else {
                if (position == getItemCount() - 1) end = true;
                bindRoom(h, list3.get(position - (start3 + 1)), end);
            }

        }
    }

    private void bindRoom(RoomAdapter.RoomViewHolder holder, Room room, boolean end) {
        String countText = room.getMembers().size() + " members";
        holder.title.setText(room.getName());
        holder.activeCounter.setText(countText);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d 'at' h:mm", Locale.US);
        holder.countdown.setText(formatter.format(room.getExpireTime()));
        if (end) holder.divider.setVisibility(View.INVISIBLE);
        else holder.divider.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(v -> {
            Log.d("adapter", "clicked room " + room);
            listener.onClick(room);
        });
    }

    @Override
    public int getItemCount() {
        return Math.max(list1.size(), 1) + Math.max(list2.size() , 1) + Math.max(list3.size(), 1) + 3;
    }

    @Override
    public int getItemViewType(int position) {
        int start1 = 0;
        int start2 = Math.max(list1.size(), 1) + 1;
        int start3 = start2 + Math.max(list2.size(), 1) + 1;

        if (position == start1 || position == start2 || position == start3) {
            return R.layout.layout_header;
        } else if ((position < start2 && list1.size() > position - 1)
                || (position > start2 && position < start3 && list2.size() > (position - (start2 + 1)))
                || (position > start3 && list3.size() > (position - (start3 + 1)))) {
            return R.layout.layout_room;
        } else {
            return R.layout.layout_no_rooms;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public View icon;
        public TextView title;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            icon = ViewUtils.findView(itemView, R.id.icon);
            title = ViewUtils.findView(itemView, R.id.title);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView explanation;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            title = ViewUtils.findView(itemView, R.id.title);
            explanation = ViewUtils.findView(itemView, R.id.explanation);
        }
    }
}
