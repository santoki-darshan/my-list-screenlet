package com.liferay.mobile.screens.messageboardscreenlet.view;

import com.liferay.mobile.screens.base.list.view.BaseListViewModel;

/**
 * Created by darshan on 6/8/15.
 */
public interface MyListViewModel<E> extends BaseListViewModel<E> {

    void setListCount(int totalCount);

    void setAdapter(String adapterClassName, int layoutId, int progressLayoutId);
}
