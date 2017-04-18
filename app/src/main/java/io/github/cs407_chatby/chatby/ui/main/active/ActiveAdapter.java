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

    private List<Room> created = new ArrayList<>();
    private List<Room> favorite = new ArrayList<>();
    private List<Room> active = new ArrayList<>();

    private RoomAdapter.Listener listener = room -> {};

    @Inject
    public ActiveAdapter() {}

    public void setCreated(List<Room> created) {
        this.created = created;
        notifyDataSetChanged();
    }

    public void setFavorite(List<Room> favorite) {
        this.favorite = favorite;
        notifyDataSetChanged();
    }

    public void setActive(List<Room> active) {
        this.active = active;
        notifyDataSetChanged();
    }

    public void setListener(RoomAdapter.Listener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.layout_room)
            return new RoomAdapter.RoomViewHolder(ViewUtils.inflate(parent, viewType));
        return new HeaderViewHolder(ViewUtils.inflate(parent, viewType));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int createdStart = 0;
        int favoriteStart = created.size() + 1;
        int activeStart = favoriteStart + favorite.size() + 1;

        boolean end = false;

        if (position == createdStart) {
            ((HeaderViewHolder) holder).icon.setBackgroundResource(R.drawable.ic_created_24dp);
            ((HeaderViewHolder) holder).title.setText("Created");
        } else if (position < favoriteStart) {
            if (position == favoriteStart - 1) end = true;
            bindRoom((RoomAdapter.RoomViewHolder) holder, created.get(position - 1), end);
        } else if (position == favoriteStart) {
            ((HeaderViewHolder) holder).icon.setBackgroundResource(R.drawable.ic_stars_24dp);
            ((HeaderViewHolder) holder).title.setText("Starred");
        } else if (position < activeStart) {
            if (position == activeStart - 1) end = true;
            bindRoom((RoomAdapter.RoomViewHolder) holder, created.get(position - (favoriteStart + 1)), end);
        } else if (position == activeStart) {
            ((HeaderViewHolder) holder).icon.setBackgroundResource(R.drawable.ic_inbox_24dp);
            ((HeaderViewHolder) holder).title.setText("Active");
        } else {
            if (position == getItemCount() - 1) end = true;
            bindRoom((RoomAdapter.RoomViewHolder) holder, created.get(position - (activeStart + 1)), end);
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
        return Math.max(created.size(), 0) + Math.max(favorite.size() , 0) + Math.max(active.size(), 0) + 3;
    }

    @Override
    public int getItemViewType(int position) {
        int createdStart = 0;
        int favoriteStart = created.size() + 1;
        int activeStart = favoriteStart + favorite.size() + 1;

        if (position == createdStart || position == favoriteStart || position == activeStart) {
            return R.layout.layout_header;
        } else {
            return R.layout.layout_room;
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
}
