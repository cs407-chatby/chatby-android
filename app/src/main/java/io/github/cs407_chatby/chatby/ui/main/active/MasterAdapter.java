package io.github.cs407_chatby.chatby.ui.main.active;


import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.ViewHolder>{

    private List<DataSource> sources = new ArrayList<>();

    @SuppressLint("UseSparseArrays")
    private Map<Integer, ViewModelBinder> viewBinders = new HashMap<>();

    public void registerSource(DataSource adapter) {
        sources.add(adapter);
        notifyDataSetChanged();
    }

    public void setSources(List<DataSource> sources) {
        this.sources = sources;
        notifyDataSetChanged();
    }

    public void clearSources() {
        sources.clear();
        notifyDataSetChanged();
    }

    public void registerBinder(ViewModelBinder binder, int viewType) {
        viewBinders.put(viewType, binder);
    }

    public void clearBinders() {
        viewBinders.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!viewBinders.containsKey(viewType)) {
            Log.e("Adapter", "No binder capable of handling view type \'" + viewType + "\'");
            return null;
        }
        ViewModelBinder binder = viewBinders.get(viewType);
        return binder.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataSource source = getSourceWithPosition(position);
        if (source == null) {
            Log.e("Adapter", "No data source containing absolute position " + position);
            return;
        }

        if (!viewBinders.containsKey(holder.viewType)) {
            Log.e("Adapter", "No binder capable of handling view type \'" + holder.viewType + "\'");
            return;
        }
        ViewModelBinder binder = viewBinders.get(holder.viewType);
        int childPosition = getChildPosition(source, position);
        binder.bindModelToView(source.getItem(childPosition), holder);
    }

    private DataSource getSourceWithPosition(int position) {
        int currentPosition = 0;
        for (DataSource source : sources) {
            currentPosition += source.getItemCount();
            if (currentPosition > position) return source;
        }
        return null;
    }

    private int getChildPosition(DataSource source, int position) {
        return position - source.getItemCount();
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (DataSource adapter : sources)
            count += adapter.getItemCount();
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        DataSource source = getSourceWithPosition(position);
        if (source == null) {
            Log.e("Adapter", "No data source containing absolute position " + position);
            return 0;
        }
        return source.getItemViewType(getChildPosition(source, position));
    }

    public interface ViewModelBinder {
        ViewHolder createViewHolder(ViewGroup parent, int viewType);
        void bindModelToView(Object model, ViewHolder holder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int viewType;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
        }
    }
}
