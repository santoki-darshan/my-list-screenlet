package com.liferay.mobile.screens.messageboardscreenlet.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.liferay.mobile.screens.base.list.BaseListScreenlet;
import com.liferay.mobile.screens.messageboardscreenlet.MyListScreenlet;
import com.liferay.mobile.screens.messageboardscreenlet.R;
import com.liferay.mobile.screens.util.LiferayLogger;
import com.liferay.mobile.screens.viewsets.defaultviews.DefaultTheme;
import com.liferay.mobile.screens.viewsets.defaultviews.ddl.list.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by darshan on 6/8/15.
 */
public class MyListView extends FrameLayout implements MyListViewModel, MyListAdapterListener {

    protected RecyclerView _recyclerView;
    private ProgressBar _progressBar;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private static final String _STATE_ENTRIES = "entries";
    private static final String _STATE_ROW_COUNT = "rowCount";
    private static final String _STATE_SUPER = "super";
    protected final int BOTTOM_OFFSET = 5;
    protected boolean checkForLoadMore;
    protected int pastVisiblesItems, visibleItemCount, totalItemCount, currentRow, totalMessages;
    protected Context context;
    private final String TAG = "MyListView";

    public MyListView(Context context) {
        super(context);
        this.context = context;
    }

    public MyListView(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.context = context;
    }

    public MyListView(Context context, AttributeSet attributes, int defaultStyle) {
        super(context, attributes, defaultStyle);
        this.context = context;
    }

    @Override
    public void onItemClick(int position, View view) {
        BaseListScreenlet screenlet = ((BaseListScreenlet) getParent());
        List entries = getAdapter().getEntries();

        // we do not want to crash if the user manages to do a phantom click
        if (!entries.isEmpty() && entries.size() > position && screenlet.getListener() != null) {
            screenlet.getListener().onListItemSelected(entries.get(position), view);
        }
    }

    public MyListAdapter getAdapter() {
        return (MyListAdapter) _recyclerView.getAdapter();
    }

    @Override
    public void showFinishOperation(int page, List entries, int rowCount) {
        LiferayLogger.i("loaded page " + page + " of list with " + entries);

        _progressBar.setVisibility(View.GONE);
        _recyclerView.setVisibility(View.VISIBLE);

        MyListAdapter adapter = getAdapter();
        List allEntries = createAllEntries(page, entries, rowCount, adapter);

        adapter.setEntries(allEntries);
        adapter.notifyDataSetChanged();
        _swipeRefreshLayout.setRefreshing(false);
        if (totalMessages > allEntries.size()) {
            checkForLoadMore = true;
        }
    }

    protected List createAllEntries(int page, List serverEntries, int rowCount, MyListAdapter adapter) {
        List entries;
        if (page == 0) {
            entries = new ArrayList<>();
        } else {
            entries = adapter.getEntries();
        }
        List allEntries = new ArrayList<>(Collections.nCopies(entries.size() + rowCount, null));

        for (int i = 0; i < entries.size(); i++) {
            allEntries.set(i, entries.get(i));
        }

        BaseListScreenlet screenlet = ((BaseListScreenlet) getParent());

        int firstRowForPage = screenlet.getFirstRowForPage(page);

        for (int i = 0; i < serverEntries.size(); i++) {
            allEntries.set(i + firstRowForPage, serverEntries.get(i));
        }
        return allEntries;
    }

    @Override
    public void showFinishOperation(int page, Exception e) {
        _progressBar.setVisibility(View.GONE);
        _recyclerView.setVisibility(View.VISIBLE);
        _swipeRefreshLayout.setRefreshing(false);
        LiferayLogger.e(getContext().getString(R.string.loading_list_error), e);
    }

    @Override
    public void showStartOperation(String actionName) {
        if (getAdapter().getEntries().size() == 0) {
            _progressBar.setVisibility(View.VISIBLE);
            _recyclerView.setVisibility(View.GONE);
            LiferayLogger.i("loading list");
        }
    }

    @Override
    public void showFinishOperation(String actionName) {
        throw new AssertionError("Use showFinishOperation(page, entries, rowCount) instead");
    }

    @Override
    public void showFailedOperation(String actionName, Exception e) {
        throw new AssertionError("Use showFinishOperation(page, entries, rowCount) instead");
    }

    @Override
    protected void onRestoreInstanceState(Parcelable inState) {
        Bundle state = (Bundle) inState;
        Parcelable superState = state.getParcelable(_STATE_SUPER);

        super.onRestoreInstanceState(superState);

        List entries = state.getParcelableArrayList(_STATE_ENTRIES);

        MyListAdapter adapter = getAdapter();
        adapter.setEntries(entries);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        MyListAdapter adapter = getAdapter();
        ArrayList entries = (ArrayList) adapter.getEntries();

        Bundle state = new Bundle();
        state.putParcelableArrayList(_STATE_ENTRIES, entries);
        state.putSerializable(_STATE_ROW_COUNT, adapter.getItemCount());
        state.putParcelable(_STATE_SUPER, superState);

        return state;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        DefaultTheme.initIfThemeNotPresent(getContext());

        _swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.liferay_swiperefreshlayout);
        _recyclerView = (RecyclerView) findViewById(R.id.liferay_recycler_list);
        _progressBar = (ProgressBar) findViewById(R.id.liferay_progress);

//        _recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        _recyclerView.setLayoutManager(mLayoutManager);
        _recyclerView.setItemAnimator(new DefaultItemAnimator());

//        DividerItemDecoration dividerItemDecoration = getDividerDecoration();
//        if (dividerItemDecoration != null) {
//            _recyclerView.addItemDecoration(getDividerDecoration());
//        }

        _recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (checkForLoadMore) {
                    if ((visibleItemCount + pastVisiblesItems + BOTTOM_OFFSET) >= totalItemCount) {
                        checkForLoadMore = false;
                        Log.d(TAG, "load next page");
                        currentRow++;
                        ((BaseListScreenlet) getParent()).loadPage(currentRow);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        _swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MyListScreenlet) getParent()).refresh();
            }
        });
    }

    protected DividerItemDecoration getDividerDecoration() {
        return new DividerItemDecoration(getResources().getDrawable(R.drawable.pixel_grey));
    }

    @Override
    public void setListCount(int totalCount) {
        totalMessages = totalCount;
    }

    @Override
    public void setAdapter(String myListAdapterClassName, int layoutId, int progressLayoutId) {
        try {
            MyListAdapter myListAdapter = (MyListAdapter) Class.forName(myListAdapterClassName)
                                                               .getConstructor(Context.class, int.class, int.class, List.class,
                                                                               MyListAdapterListener.class)
                                                               .newInstance(context, layoutId, progressLayoutId, new ArrayList<>(), this);

            _recyclerView.setAdapter(myListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
