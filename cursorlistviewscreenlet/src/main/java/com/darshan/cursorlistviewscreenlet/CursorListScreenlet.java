package com.darshan.cursorlistviewscreenlet;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.commonsware.cwac.loaderex.SQLiteCursorLoader;
import com.darshan.cursorlistviewscreenlet.interactor.CursorListListener;
import com.darshan.cursorlistviewscreenlet.view.CursorListViewModel;
import com.liferay.mobile.screens.base.BaseScreenlet;
import com.liferay.mobile.screens.base.interactor.Interactor;

/**
 * Created by darshan on 6/8/15.
 */
public class CursorListScreenlet extends BaseScreenlet<CursorListViewModel, Interactor> implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ACTION_DELETE = "delete", ACTION_ADD = "add", ACTION_UPDATE = "update", ACTION_SEARCH = "search",
            ACTION_RESET = "reset";
    private String adapterClassName;
    private int itemLayoutId, itemProgressLayoutId, layoutId;
    private CursorListListener listener;
    private SQLiteCursorLoader loader;

    public CursorListScreenlet(Context context) {
        super(context);
    }

    public CursorListScreenlet(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public CursorListScreenlet(Context context, AttributeSet attributes, int defaultStyle) {
        super(context, attributes, defaultStyle);
    }

    @Override
    protected View createScreenletView(Context context, AttributeSet attributes) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributes, R.styleable.MyListScreenlet, 0, 0);

        layoutId = typedArray.getResourceId(R.styleable.MyListScreenlet_layoutId, 0);

        itemLayoutId = typedArray.getResourceId(R.styleable.MyListScreenlet_itemLayoutId, getDefaultItemLayoutId());

        itemProgressLayoutId = typedArray.getResourceId(R.styleable.MyListScreenlet_itemProgressLayoutId, getDefaultItemProgressLayoutId());

        adapterClassName = typedArray.getNonResourceString(R.styleable.MyListScreenlet_adapterClassName);

        typedArray.recycle();

        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    @Override
    protected Interactor createInteractor(String s) {
        return null;
    }

    @Override
    protected void onFinishInflate() {
        getViewModel().setAdapter(adapterClassName, itemLayoutId, itemProgressLayoutId);
        super.onFinishInflate();


    }

    protected int getDefaultItemLayoutId() {
        return R.layout.list_item_messagecard;
    }

    protected int getDefaultItemProgressLayoutId() {
        return R.layout.list_item_load_more;
    }

    @Override
    protected void onUserAction(String s, Interactor interactor, Object... objects) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        this.loader = (SQLiteCursorLoader) loader;
        getViewModel().changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getViewModel().changeCursor(null);
    }

    public CursorListListener getListener() {
        return listener;
    }

    public void setListener(CursorListListener listener) {
        this.listener = listener;
    }

    public void init(Activity activity, SQLiteCursorLoader loader, boolean registerForContextMenu
            , AbsListView.MultiChoiceModeListener multiChoiceModeListener) {

        if (registerForContextMenu) {
            getViewModel().registerForContextMenu(activity);
        }
        this.loader = loader;
        activity.getLoaderManager().initLoader(0, null, this);
        getViewModel().setMultiChoiceModeListener(multiChoiceModeListener);
    }

    @Override
    public void performUserAction(String userActionName, Object... args) {
        switch (userActionName) {
            case ACTION_DELETE:
                loader.delete((String) args[0], (String) args[1], (String[]) args[2]);
                break;
            case ACTION_ADD:
                loader.insert((String) args[0], (String) args[1], (ContentValues) args[2]);
                break;
            case ACTION_UPDATE:
                loader.update((String) args[0], (ContentValues) args[1], (String) args[2], (String[]) args[3]);
                break;
            case ACTION_SEARCH:
                loader.deliverResult((Cursor) args[0]);
                break;
            case ACTION_RESET:
                loader.onContentChanged();
                break;
        }
    }

    public void onItemClicked(AdapterView<?> parent, View view, int position, long id) {
        listener.onListItemSelected(id);
    }
}
