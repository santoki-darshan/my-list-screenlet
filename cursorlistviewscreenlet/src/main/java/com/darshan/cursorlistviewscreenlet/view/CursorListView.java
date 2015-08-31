package com.darshan.cursorlistviewscreenlet.view;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.darshan.cursorlistviewscreenlet.CursorListScreenlet;
import com.darshan.cursorlistviewscreenlet.R;
import com.liferay.mobile.screens.viewsets.defaultviews.DefaultTheme;

/**
 * Created by darshan on 6/8/15.
 */
public class CursorListView extends FrameLayout implements CursorListViewModel, CursorListAdapterListener {

    protected ListView listView;
    //    private static final String _STATE_ENTRIES = "entries";
//    private static final String _STATE_ROW_COUNT = "rowCount";
//    private static final String _STATE_SUPER = "super";
    protected int currentRow;
    protected Context context;
    private final String TAG = "CursorListView";

    public CursorListView(Context context) {
        super(context);
        this.context = context;
    }

    public CursorListView(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.context = context;
    }

    public CursorListView(Context context, AttributeSet attributes, int defaultStyle) {
        super(context, attributes, defaultStyle);
        this.context = context;
    }

    @Override
    public void onItemClick(Cursor cursor, View view) {
        CursorListScreenlet screenlet = ((CursorListScreenlet) getParent());
        screenlet.getListener().onListItemSelected(cursor, view);
    }

//    @Override
//    protected void onRestoreInstanceState(Parcelable inState) {
//        Bundle state = (Bundle) inState;
//        Parcelable superState = state.getParcelable(_STATE_SUPER);
//
//        super.onRestoreInstanceState(superState);
//
//        List entries = state.getParcelableArrayList(_STATE_ENTRIES);
//
//        CursorListAdapter adapter = getAdapter();
//        adapter.setEntries(entries);
//        adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//
//        CursorListAdapter adapter = getAdapter();
//        ArrayList entries = (ArrayList) adapter.getEntries();
//
//        Bundle state = new Bundle();
//        state.putParcelableArrayList(_STATE_ENTRIES, entries);
//        state.putSerializable(_STATE_ROW_COUNT, adapter.getItemCount());
//        state.putParcelable(_STATE_SUPER, superState);
//
//        return state;
//    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        DefaultTheme.initIfThemeNotPresent(getContext());

        listView = (ListView) findViewById(R.id.mylistview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((CursorListScreenlet) getParent()).onItemClicked(parent, view, position, id);
            }
        });

        listView.setEmptyView(findViewById(R.id.mylistview_tv_emptyview));
    }

    @Override
    public void setAdapter(String myListAdapterClassName, int layoutId, int progressLayoutId) {
        try {
            CursorListAdapter cursorListAdapter = (CursorListAdapter) Class.forName(myListAdapterClassName)
                                                               .getConstructor(Context.class, Cursor.class, CursorListAdapterListener.class)
                                                               .newInstance(context, null, this);

            listView.setAdapter(cursorListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void registerForContextMenu(Activity activity) {
        activity.registerForContextMenu(listView);
    }

    @Override
    public void setMultiChoiceModeListener(AbsListView.MultiChoiceModeListener multiChoiceModeListener) {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(multiChoiceModeListener);
    }

    @Override
    public void changeCursor(Cursor cursor) {
        ((CursorListAdapter) listView.getAdapter()).changeCursor(cursor);
    }

    @Override
    public void showStartOperation(String s) {

    }

    @Override
    public void showFinishOperation(String s) {

    }

    @Override
    public void showFailedOperation(String s, Exception e) {

    }
}
