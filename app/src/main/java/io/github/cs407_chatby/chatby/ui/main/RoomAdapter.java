package io.github.cs407_chatby.chatby.ui.main;

import android.support.annotation.NonNull;
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

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.utils.ViewUtils;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> rooms = new ArrayList<>();
    private Listener listener = room -> {};

    @Inject
    public RoomAdapter() {}

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setRooms(List<Room> rooms) {
        List<Room> listA = new ArrayList<>(this.rooms);
        List<Room> listB = new ArrayList<>(rooms);
        listA.removeAll(rooms);
        listB.removeAll(this.rooms);
        if (!listA.isEmpty() || !listB.isEmpty()) {
            this.rooms = rooms;
            notifyDataSetChanged();
        }
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RoomViewHolder(ViewUtils.inflate(parent, R.layout.layout_room));
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        String countText = room.getMembers().size() + " members";
        holder.title.setText(room.getName());
        holder.activeCounter.setText(countText);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d 'at' h:mm", Locale.US);
        holder.countdown.setText(formatter.format(room.getExpireTime()));

        holder.view.setOnClickListener(v -> {
            Log.d("adapter", "clicked room " + room);
            listener.onClick(room);
        });

        if (position == rooms.size() - 1)
            holder.divider.setVisibility(View.INVISIBLE);
        else
            holder.divider.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        @NonNull public final View view;
        @NonNull public final CircleImageView creatorPic;
        @NonNull public final TextView title;
        @NonNull public final TextView activeCounter;
        @NonNull public final TextView countdown;
        @NonNull public final View divider;

        public RoomViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            creatorPic = ViewUtils.findView(itemView, R.id.creator_pic);
            title = ViewUtils.findView(itemView, R.id.room_title);
            activeCounter = ViewUtils.findView(itemView, R.id.active_count);
            countdown = ViewUtils.findView(itemView, R.id.time_remaining);
            divider = ViewUtils.findView(itemView, R.id.divider);
        }
    }

    public interface Listener {
        void onClick(Room room);
    }
}
