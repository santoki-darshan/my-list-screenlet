package com.liferay.mobile.screens.messageboardscreenlet;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.liferay.mobile.screens.base.BaseScreenlet;
import com.liferay.mobile.screens.messageboardscreenlet.interactor.MessageDetailInteractor;
import com.liferay.mobile.screens.messageboardscreenlet.interactor.MessageDetailInteractorImpl;
import com.liferay.mobile.screens.messageboardscreenlet.interactor.MessageDetailInteractorListener;
import com.liferay.mobile.screens.messageboardscreenlet.interactor.MessageDetailListener;
import com.liferay.mobile.screens.messageboardscreenlet.view.MessageDetailView;
import com.liferay.mobile.screens.messageboardscreenlet.view.MessageDetailViewModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by darshan on 17/8/15.
 */
public class MessageDetailScreenlet extends BaseScreenlet<MessageDetailViewModel, MessageDetailInteractor> implements
        MessageDetailInteractorListener {

    private MessageDetailListener activityListener;
    public static final String ACTION_SEE_ANSWERS = "see_answers", ACTION_QUICK_REPLY = "quick_reply";
    private final String TAG = "MessageDetailScreenlet";

    public MessageDetailScreenlet(Context context) {
        super(context);
    }

    public MessageDetailScreenlet(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public MessageDetailScreenlet(Context context, AttributeSet attributes, int defaultStyle) {
        super(context, attributes, defaultStyle);
    }

    @Override
    protected View createScreenletView(Context context, AttributeSet attributes) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributes, R.styleable.MessageDetailScreenlet, 0, 0);

        int layoutId = typedArray.getResourceId(R.styleable.MessageDetailScreenlet_layoutId, 0);

        typedArray.recycle();

        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    @Override
    protected MessageDetailInteractor createInteractor(String actionName) {
        return new MessageDetailInteractorImpl(getScreenletId());
    }

    @Override
    protected void onUserAction(String userActionName, MessageDetailInteractor interactor, Object... args) {
        switch (userActionName) {
            case ACTION_SEE_ANSWERS:
                int count = (int) args[0];
                if (count > 1) {
                    MessageDetailInteractor messageDetailInteractor = getInteractor();
                    activityListener.seeAnswers(messageDetailInteractor.getThreadId(), messageDetailInteractor.getCategoryId(),
                                                messageDetailInteractor.getGroupId(), messageDetailInteractor.getStatus(), count);
                }
                break;
            case ACTION_QUICK_REPLY:
                String reply = (String) args[0];
                MessageDetailInteractor messageDetailInteractor = getInteractor();
                String subject = messageDetailInteractor.getSubject();
                if (!subject.startsWith("RE")) {
                    subject = "RE: " + subject;
                }
                try {
                    getViewModel().showStartOperation(MessageDetailView.ACTION_QUICK_REPLY);
                    messageDetailInteractor.quickReply(messageDetailInteractor.getMessageId(), subject, reply);
                } catch (Exception e) {
                    e.printStackTrace();
                    getViewModel().showFailedOperation(MessageDetailView.ACTION_QUICK_REPLY, e);
                }
                break;
        }
    }

    @Override
    public void onMessageGetSuccess(JSONObject message) throws JSONException {
        MessageDetailViewModel messageDetailViewModel = getViewModel();
        messageDetailViewModel.setBody(message.getString("body"));
        messageDetailViewModel.setSubject(message.getString("subject"));
        messageDetailViewModel.setInfoText(message.getString("userName"), message.getLong("createDate"));
        messageDetailViewModel.showFinishOperation(MessageDetailView.ACTION_LOAD_MESSAGE);
    }

    @Override
    public void onMessageGetFailure(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void setReplyCount(int count) {
        getViewModel().setTotalReply(count);
    }

    @Override
    public void onQuickReplyFailed(Exception e) {
        e.printStackTrace();
        getViewModel().showFailedOperation(MessageDetailView.ACTION_QUICK_REPLY, e);
    }

    @Override
    public void onQuickReplySuccess(JSONObject message) {
        Log.d(TAG, message.toString());
        getViewModel().showFinishOperation(MessageDetailView.ACTION_QUICK_REPLY);
    }

    public void loadMessage(long messageId) {
        try {
            getViewModel().showStartOperation(MessageDetailView.ACTION_LOAD_MESSAGE);
            getInteractor().getMessage(messageId);
        } catch (Exception e) {
            getViewModel().showFailedOperation(MessageDetailView.ACTION_LOAD_MESSAGE, e);
            activityListener.loadMessageFailed(e);
            e.printStackTrace();
        }
    }

    public void setListener(MessageDetailListener messageDetailListener) {
        activityListener = messageDetailListener;
    }

    @Override
    public void performUserAction(String userActionName, Object... args) {
        MessageDetailInteractor interactor = getInteractor();

        if (interactor != null) {
            onUserAction(userActionName, interactor, args);
        }
    }
}
