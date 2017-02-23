package io.github.cs407_chatby.chatby.ui.home;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.cs407_chatby.chatby.R;
import io.github.cs407_chatby.chatby.data.model.Room;
import io.github.cs407_chatby.chatby.utils.ViewUtils;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    List<Room> created;
    List<Room> nearby;
    AdapterView.OnItemClickListener listener;

    public void setCreated(List<Room> rooms) {
        this.created = rooms;
        Log.d("Adapter", "Setting created");
        notifyDataSetChanged();
    }

    public void setNearby(List<Room> rooms) {
        this.nearby = rooms;
        Log.d("Adapter", "Setting nearby");
        notifyDataSetChanged();
    }

    public void setOnClickListener(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch(viewType) {
            case 0:
                view = inflater.inflate(R.layout.layout_post_header, parent, false);
                return new HeaderViewHolder(view);
            default:
                view = inflater.inflate(R.layout.layout_post, parent, false);
                return new PostViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            String text;
            if (position == 0) text = "Nearby";
            else text = "Created";
            ((HeaderViewHolder) holder).header.setText(text);
        } else if (holder instanceof PostViewHolder) {
            Room room;
            int nPos = position-1;
            int cPos = position-nearby.size()-2;
            if (nPos <= nearby.size()) room = nearby.get(nPos);
            else room = created.get(cPos);
            String countText = "+" + room.getMembers().size();
            ((PostViewHolder) holder).title.setText(room.getName());
            ((PostViewHolder) holder).activeCounter.setText(countText);
            ((PostViewHolder) holder).countdown.setText("00:00:00");
        }
    }

    @Override
    public int getItemCount() {
        return created.size() + nearby.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == nearby.size() + 1) return 0;
        else return 1;
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {

        RoomViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class HeaderViewHolder extends RoomViewHolder {
        TextView header;

        HeaderViewHolder(View itemView) {
            super(itemView);
            header = ViewUtils.findView(itemView, R.id.header);
        }
    }

    public static class PostViewHolder extends RoomViewHolder {
        CircleImageView creatorPic;
        CircleImageView activePic1;
        CircleImageView activePic2;
        CircleImageView activePic3;
        TextView title;
        TextView activeCounter;
        TextView countdown;

        public PostViewHolder(View itemView) {
            super(itemView);
            creatorPic = ViewUtils.findView(itemView, R.id.creator_pic);
            activePic1 = ViewUtils.findView(itemView, R.id.circle1);
            activePic2 = ViewUtils.findView(itemView, R.id.circle2);
            activePic3 = ViewUtils.findView(itemView, R.id.circle3);
            title = ViewUtils.findView(itemView, R.id.room_title);
            activeCounter = ViewUtils.findView(itemView, R.id.active_count);
            countdown = ViewUtils.findView(itemView, R.id.time_remaining);
        }
    }
}
