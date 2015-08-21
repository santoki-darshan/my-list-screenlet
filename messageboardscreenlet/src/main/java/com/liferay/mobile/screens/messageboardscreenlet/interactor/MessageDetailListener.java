package com.liferay.mobile.screens.messageboardscreenlet.interactor;

/**
 * Created by darshan on 18/8/15.
 */
public interface MessageDetailListener {
    void seeAnswers(long threadId, long categoryId, long groupId, int status, int count);

    void loadMessageFailed(Exception e);
}
