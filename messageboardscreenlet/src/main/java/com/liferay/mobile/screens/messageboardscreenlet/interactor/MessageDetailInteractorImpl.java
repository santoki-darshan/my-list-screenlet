package com.liferay.mobile.screens.messageboardscreenlet.interactor;

import android.util.Log;

import com.liferay.mobile.android.service.JSONObjectWrapper;
import com.liferay.mobile.android.service.Session;
import com.liferay.mobile.android.task.callback.typed.IntegerAsyncTaskCallback;
import com.liferay.mobile.android.v62.mbmessage.MBMessageService;
import com.liferay.mobile.screens.base.interactor.BaseRemoteInteractor;
import com.liferay.mobile.screens.base.interactor.JSONObjectCallback;
import com.liferay.mobile.screens.base.interactor.JSONObjectEvent;
import com.liferay.mobile.screens.context.SessionContext;
import com.liferay.mobile.screens.messageboardscreenlet.Event.QuickReplyEvent;
import com.liferay.mobile.screens.messageboardscreenlet.callback.QuickReplyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by darshan on 17/8/15.
 */
public class MessageDetailInteractorImpl extends BaseRemoteInteractor<MessageDetailInteractorListener> implements MessageDetailInteractor {

    private final String TAG = "MsgDetailInteractorImpl";
    private long _categoryId, _groupId, _threadId, _messageId;
    private int _status;
    private String subject;

    public MessageDetailInteractorImpl(int targetScreenletId) {
        super(targetScreenletId);
    }

    public void onEvent(JSONObjectEvent event) {
        if (!isValidEvent(event)) {
            return;
        }

        if (event.isFailed()) {
            getListener().onMessageGetFailure(event.getException());
        } else {
            JSONObject mbMessage = event.getJSONObject();
            Log.d(TAG, mbMessage.toString());
            try {
                getListener().onMessageGetSuccess(mbMessage);
                _categoryId = mbMessage.getLong("categoryId");
                _groupId = mbMessage.getLong("groupId");
                _threadId = mbMessage.getLong("threadId");
                _status = mbMessage.getInt("status");
                _messageId = mbMessage.getLong("messageId");
                subject = mbMessage.getString("subject");
                getTotalCount(_groupId, _categoryId, _threadId, _status);
            } catch (JSONException e) {
                getListener().onMessageGetFailure(event.getException());
            }
        }
    }

    public void onEvent(QuickReplyEvent event) {
        if (!isValidEvent(event)) {
            return;
        }

        if (event.isFailed()) {
            getListener().onQuickReplyFailed(event.getException());
        } else {
            getListener().onQuickReplySuccess(event.getJSONObject());
        }
    }

    @Override
    public void getMessage(long messageId) throws Exception {
        Session sessionFromCurrentSession = SessionContext.createSessionFromCurrentSession();
        sessionFromCurrentSession.setCallback(new JSONObjectCallback(getTargetScreenletId()));
        MBMessageService mbMessageService = new MBMessageService(sessionFromCurrentSession);
        mbMessageService.getMessage(messageId);
    }

    @Override
    public long getThreadId() {
        return _threadId;
    }

    @Override
    public long getCategoryId() {
        return _categoryId;
    }

    @Override
    public long getGroupId() {
        return _groupId;
    }

    @Override
    public int getStatus() {
        return _status;
    }

    private void getTotalCount(long _groupId, long _categoryId, long _threadId, int _status) {
        Session sessionFromCurrentSession = SessionContext.createSessionFromCurrentSession();
        sessionFromCurrentSession.setCallback(new IntegerAsyncTaskCallback() {
            @Override
            public void onSuccess(Integer result) {
                getListener().setReplyCount(result);
                Log.d(TAG, "total count = " + result);
            }

            @Override
            public void onFailure(Exception exception) {
                exception.printStackTrace();
            }
        });

        MBMessageService mbMessageService = new MBMessageService(sessionFromCurrentSession);
        try {
            mbMessageService.getThreadMessagesCount(_groupId, _categoryId, _threadId, _status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void quickReply(long parentMessageId, String subject, String body) throws Exception {
        Session sessionFromCurrentSession = SessionContext.createSessionFromCurrentSession();
        sessionFromCurrentSession.setCallback(new QuickReplyCallback(getTargetScreenletId()));
        MBMessageService mbMessageService = new MBMessageService(sessionFromCurrentSession);
        JSONObjectWrapper serviceContextWrapper = getJsonObjectWrapper(SessionContext.getLoggedUser().getId(), _groupId, "");
        mbMessageService.addMessage(_groupId, _categoryId, _threadId, parentMessageId, subject, body, "", new JSONArray(), false, 0, false,
                                    serviceContextWrapper);
    }

    private JSONObjectWrapper getJsonObjectWrapper(Long userId, Long groupId, String tags) throws JSONException {
        JSONObject serviceContextAttributes = new JSONObject();
        serviceContextAttributes.put("userId", userId);
        serviceContextAttributes.put("scopeGroupId", groupId);
        serviceContextAttributes.put("assetTagNames", tags);

        //addCommunityPermissions: true, addGuestPermissions: true, tagsEntries: ["tag_one"]
        return new JSONObjectWrapper(serviceContextAttributes);
    }

    @Override
    public long getMessageId() {
        return _messageId;
    }

    @Override
    public String getSubject() {
        return subject;
    }
}
