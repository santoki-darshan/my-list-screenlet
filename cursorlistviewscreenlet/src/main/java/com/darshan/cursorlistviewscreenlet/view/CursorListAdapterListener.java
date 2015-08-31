package com.darshan.cursorlistviewscreenlet.view;

import android.database.Cursor;
import android.view.View;

/**
 * Created by darshan on 6/8/15.
 */
public interface CursorListAdapterListener {
    void onItemClick(Cursor cursor, View view);
}
