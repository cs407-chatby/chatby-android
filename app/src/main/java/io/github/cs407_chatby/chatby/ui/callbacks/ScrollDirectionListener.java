package io.github.cs407_chatby.chatby.ui.callbacks;


import android.support.v7.widget.RecyclerView;

public abstract class ScrollDirectionListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy < 0) onScrollUp();
        else if (dy > 0) onScrollDown();
    }

    public abstract void onScrollUp();

    public abstract void onScrollDown();
}
