package com.liferay.mobile.screens.messageboardscreenlet.interactor;

import com.liferay.mobile.screens.base.list.interactor.BaseListInteractorListener;

/**
 * Created by darshan on 6/8/15.
 */
public interface MyListInteractorListener<E> extends BaseListInteractorListener<E> {

    void setTotalCount(Integer result);
}
