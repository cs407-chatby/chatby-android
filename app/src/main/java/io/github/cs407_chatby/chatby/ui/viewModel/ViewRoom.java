package io.github.cs407_chatby.chatby.ui.viewModel;


import android.view.View;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.ui.main.RoomAdapter;

public class ViewRoom extends AbstractItem<ViewRoom, RoomAdapter.RoomViewHolder> {
    public Room room;

    public ViewRoom(Room room) {
        this.room = room;
    }

    @Override
    public RoomAdapter.RoomViewHolder getViewHolder(View v) {
        return new RoomAdapter.RoomViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.room_layout;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_room;
    }

    @Override
    public void bindView(RoomAdapter.RoomViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        String countText = room.getMembers().size() + " members";
        holder.title.setText(room.getName());
        holder.activeCounter.setText(countText);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d 'at' h:mm", Locale.US);
        holder.countdown.setText(formatter.format(room.getExpireTime()));
    }
}
