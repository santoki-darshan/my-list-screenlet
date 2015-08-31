package com.darshan.cursorlistviewscreenlet.interactor;

import android.database.Cursor;
import android.view.View;

/**
 * Created by darshan on 24/8/15.
 */
public interface CursorListListener {
    void onListItemSelected(Cursor cursor, View view);

    void onListItemSelected(long id);
}
