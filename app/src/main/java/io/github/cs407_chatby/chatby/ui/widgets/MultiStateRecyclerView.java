package io.github.cs407_chatby.chatby.ui.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


public class MultiStateRecyclerView extends RecyclerView {
    private View loadingView;
    private View emptyView;

    State state = State.Loading;

    public enum State {
        Content, Loading, Error, Empty
    }

    public MultiStateRecyclerView(Context context) {
        super(context);
    }

    public MultiStateRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public View getLoadingView() {
        return loadingView;
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
        if (state == State.Loading) loadingView.setVisibility(View.VISIBLE);
        else loadingView.setVisibility(View.GONE);
    }

    public View getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        if (state == State.Empty) loadingView.setVisibility(View.VISIBLE);
        else loadingView.setVisibility(View.GONE);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        switch (state) {
            case Content:
                loadingView.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
            case Loading:
                loadingView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                this.setVisibility(View.GONE);
                break;
            default:
                loadingView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                this.setVisibility(View.GONE);
                break;
        }
    }
}
