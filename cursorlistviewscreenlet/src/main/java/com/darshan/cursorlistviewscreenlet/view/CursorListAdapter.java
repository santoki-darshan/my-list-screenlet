package com.darshan.cursorlistviewscreenlet.view;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;

/**
 * Created by darshan on 6/8/15.
 */
public abstract class CursorListAdapter<E> extends CursorAdapter {

    public CursorListAdapter(Context context, Cursor c, CursorListAdapterListener listener) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

}
