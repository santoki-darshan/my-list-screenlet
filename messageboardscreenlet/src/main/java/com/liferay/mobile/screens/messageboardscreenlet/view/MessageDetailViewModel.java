package com.liferay.mobile.screens.messageboardscreenlet.view;

import com.liferay.mobile.screens.base.view.BaseViewModel;

/**
 * Created by darshan on 17/8/15.
 */
public interface MessageDetailViewModel extends BaseViewModel {

    void setBody(String body);

    void setSubject(String subject);

    void setTotalReply(int reply);

    void setTotalPost(int post);

    void setTotalView(int view);

     void setInfoText(String infoText, long dateTimeInMillis);
}
