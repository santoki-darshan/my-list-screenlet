package com.liferay.mobile.screens.messageboardscreenlet.interactor;

import com.liferay.mobile.screens.base.interactor.Interactor;

/**
 * Created by darshan on 6/8/15.
 */
public interface MyListInteractor extends Interactor<MyListInteractorListener> {

    void loadRows(int startRow, int endRow) throws Exception;

}
