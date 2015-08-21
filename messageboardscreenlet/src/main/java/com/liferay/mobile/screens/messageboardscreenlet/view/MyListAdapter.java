package com.liferay.mobile.screens.messageboardscreenlet.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by darshan on 6/8/15.
 */
public abstract class MyListAdapter<E, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int _layoutId;
    private int _progressLayoutId;
    private LayoutInflater inflater;

    public MyListAdapter(Context context, int layoutId, int progressLayoutId, List<E> list, MyListAdapterListener listener) {
        _progressLayoutId = progressLayoutId;
        _layoutId = layoutId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        myBindViewHolder((T) holder, position);
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(_layoutId, parent, false);

        return getViewHolder(view);
    }

    protected abstract T getViewHolder(View view);

    protected abstract void myBindViewHolder(T holder, int position);

    protected abstract void setEntries(List<E> entries);

    protected abstract List<E> getEntries();

}
