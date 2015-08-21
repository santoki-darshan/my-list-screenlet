package com.liferay.mobile.screens.messageboardscreenlet.Event;

import com.liferay.mobile.screens.base.interactor.BasicEvent;

import org.json.JSONObject;

/**
 * Created by darshan on 18/8/15.
 */
public class QuickReplyEvent extends BasicEvent {

    private JSONObject jsonObject;

    public QuickReplyEvent(int targetScreenletId, JSONObject jsonObject) {
        super(targetScreenletId);
        this.jsonObject = jsonObject;
    }

    public JSONObject getJSONObject() {
        return jsonObject;
    }

    public QuickReplyEvent(int targetScreenletId, Exception exception) {
        super(targetScreenletId, exception);
    }
}
