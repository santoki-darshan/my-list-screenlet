package com.liferay.mobile.screens.messageboardscreenlet.callback;

import com.liferay.mobile.screens.base.interactor.BasicEvent;
import com.liferay.mobile.screens.base.interactor.InteractorAsyncTaskCallback;
import com.liferay.mobile.screens.messageboardscreenlet.Event.QuickReplyEvent;

import org.json.JSONObject;

/**
 * Created by darshan on 18/8/15.
 */
public class QuickReplyCallback extends InteractorAsyncTaskCallback<JSONObject> {

    public QuickReplyCallback(int targetScreenletId) {
        super(targetScreenletId);
    }

    @Override
    protected BasicEvent createEvent(int targetScreenletId, Exception e) {
        return new QuickReplyEvent(targetScreenletId, e);
    }

    @Override
    protected BasicEvent createEvent(int targetScreenletId, JSONObject result) {
        return new QuickReplyEvent(targetScreenletId, result);
    }

    @Override
    public JSONObject transform(Object obj) throws Exception {
        return (JSONObject) obj;
    }
}
