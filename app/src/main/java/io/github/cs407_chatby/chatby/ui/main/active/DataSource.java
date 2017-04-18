package io.github.cs407_chatby.chatby.ui.main.active;

public abstract class DataSource {
    private OnDataChangedListener listener;

    abstract int getItemCount();
    abstract int getItemViewType(int position);
    abstract Object getItem(int position);

    void setOnDataChangedListener(OnDataChangedListener listener) {
        this.listener = listener;
    }

    void notifyDataChanged() {
        if (listener != null) listener.onDataChanged();
    }

    public interface OnDataChangedListener {
        void onDataChanged();
    }
}