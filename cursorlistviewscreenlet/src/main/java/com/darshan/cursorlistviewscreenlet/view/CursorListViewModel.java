package com.darshan.cursorlistviewscreenlet.view;

import android.app.Activity;
import android.database.Cursor;
import android.widget.AbsListView;

import com.liferay.mobile.screens.base.view.BaseViewModel;

/**
 * Created by darshan on 6/8/15.
 */
public interface CursorListViewModel extends BaseViewModel {

    void setAdapter(String adapterClassName, int layoutId, int progressLayoutId);

    void registerForContextMenu(Activity activity);

    void setMultiChoiceModeListener(AbsListView.MultiChoiceModeListener multiChoiceModeListener);

    void changeCursor(Cursor cursor);
}
