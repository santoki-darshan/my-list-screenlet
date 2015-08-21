package com.liferay.mobile.screens.messageboardscreenlet.interactor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by darshan on 17/8/15.
 */
public interface MessageDetailInteractorListener {

    void onMessageGetSuccess(JSONObject message) throws JSONException;

    void onMessageGetFailure(Exception e);

    void setReplyCount(int count);

    void onQuickReplyFailed(Exception e);

    void onQuickReplySuccess(JSONObject message);
}
