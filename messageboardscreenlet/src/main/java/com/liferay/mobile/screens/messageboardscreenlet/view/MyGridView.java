package com.liferay.mobile.screens.messageboardscreenlet.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.liferay.mobile.screens.base.list.BaseListScreenlet;

/**
 * Created by darshan on 20/8/15.
 */
public class MyGridView extends MyListView {

    private String TAG = "MyGridView";

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public MyGridView(Context context, AttributeSet attributes, int defaultStyle) {
        super(context, attributes, defaultStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

//        final StaggeredGridLayoutManager mLayoutManager =
//                new StaggeredGridLayoutManager(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS, StaggeredGridLayoutManager
// .VERTICAL);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        _recyclerView.setLayoutManager(mLayoutManager);

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

    }

}
