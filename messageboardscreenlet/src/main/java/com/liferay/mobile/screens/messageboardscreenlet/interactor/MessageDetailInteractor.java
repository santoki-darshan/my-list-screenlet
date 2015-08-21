package com.liferay.mobile.screens.messageboardscreenlet.interactor;

import com.liferay.mobile.screens.base.interactor.Interactor;

/**
 * Created by darshan on 17/8/15.
 */
public interface MessageDetailInteractor extends Interactor<MessageDetailInteractorListener> {

    void getMessage(long messageId) throws Exception;

    void quickReply(long parentMessageId, String subject, String body) throws Exception;

    long getThreadId();

    long getCategoryId();

    long getGroupId();

    int getStatus();

    long getMessageId();

    String getSubject();
}
