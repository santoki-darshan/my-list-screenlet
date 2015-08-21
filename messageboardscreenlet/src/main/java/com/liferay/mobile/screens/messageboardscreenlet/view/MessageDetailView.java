package com.liferay.mobile.screens.messageboardscreenlet.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liferay.mobile.screens.messageboardscreenlet.MessageDetailScreenlet;
import com.liferay.mobile.screens.messageboardscreenlet.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by darshan on 17/8/15.
 */
public class MessageDetailView extends RelativeLayout implements MessageDetailViewModel, View.OnClickListener {

    private TextView tvTitle, tvInfo, tvPostCount, tvViewCount, tvBody, tvReplyCount;
    private ImageView ivDP, ivReply;
    private EditText etReply;
    private Context context;
    private int replyCount;
    private RelativeLayout rlContainer;
    private ProgressBar progressBar;
    public static final String ACTION_QUICK_REPLY = "quick_reply", ACTION_LOAD_MESSAGE = "load_message";

    public MessageDetailView(Context context) {
        super(context);
        this.context = context;
    }

    public MessageDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MessageDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public void showStartOperation(String actionName) {
        switch (actionName) {
            case ACTION_LOAD_MESSAGE:
                rlContainer.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                etReply.setEnabled(false);
                break;
            case ACTION_QUICK_REPLY:
                progressBar.setVisibility(View.VISIBLE);
                etReply.setEnabled(false);
                ivReply.setEnabled(false);
                break;
        }
    }

    @Override
    public void showFinishOperation(String actionName) {
        switch (actionName) {
            case ACTION_QUICK_REPLY:
                progressBar.setVisibility(View.INVISIBLE);
                etReply.setEnabled(true);
                ivReply.setEnabled(true);
                etReply.setText("");
                ivReply.setVisibility(View.GONE);
                setTotalReply(replyCount + 1);
                break;
            case ACTION_LOAD_MESSAGE:
                rlContainer.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                etReply.setEnabled(true);
                break;
        }
    }

    @Override
    public void showFailedOperation(String actionName, Exception e) {
        switch (actionName) {
            case ACTION_LOAD_MESSAGE:
                break;
            case ACTION_QUICK_REPLY:
                progressBar.setVisibility(View.INVISIBLE);
                etReply.setEnabled(true);
                ivReply.setEnabled(true);
                break;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        progressBar = (ProgressBar) findViewById(R.id.msgdetail_progress);
        rlContainer = (RelativeLayout) findViewById(R.id.msgdetail_rl_container);
        tvBody = (TextView) findViewById(R.id.msgdetail_tv_body);
        tvInfo = (TextView) findViewById(R.id.msgdetail_tv_info);
        tvPostCount = (TextView) findViewById(R.id.msgdetail_tv_postcounts);
        tvReplyCount = (TextView) findViewById(R.id.msgdetail_tv_replycount);
        tvTitle = (TextView) findViewById(R.id.msgdetail_tv_title);
        tvViewCount = (TextView) findViewById(R.id.msgdetail_tv_viewcounts);
        ivDP = (ImageView) findViewById(R.id.msgdetail_iv_dp);
        ivReply = (ImageView) findViewById(R.id.msgdetail_iv_reply);
        etReply = (EditText) findViewById(R.id.msgdetail_et_reply);

        ivReply.setVisibility(View.GONE);
        tvReplyCount.setOnClickListener(this);
        ivReply.setOnClickListener(this);

        etReply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    ivReply.setVisibility(View.VISIBLE);
                } else {
                    ivReply.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void setBody(String body) {
        tvBody.setText(body);
    }

    @Override
    public void setSubject(String subject) {
        tvTitle.setText(subject);
    }

    @Override
    public void setTotalReply(int reply) {
        replyCount = reply;
        tvReplyCount.setText(context.getString(R.string.n_answer_on_post, (reply - 1)));
    }

    @Override
    public void setTotalPost(int post) {
//        tvPostCount.setText();
    }

    @Override
    public void setTotalView(int view) {
//        tvViewCount.setText();
    }

    @Override
    public void setInfoText(String userName, long createDateLong) {
        Calendar createCalendar = Calendar.getInstance();
        createCalendar.setTimeInMillis(createDateLong);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());
        tvInfo.setText(context.getString(R.string.by_1s_on_2s, userName, simpleDateFormat.format(createCalendar.getTime())));
//        tvInfo.setText(context.getString(R.string.by_1s_2s_ago, userName, "1 hour"));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.msgdetail_tv_replycount) {
            ((MessageDetailScreenlet) getParent()).performUserAction(MessageDetailScreenlet.ACTION_SEE_ANSWERS, replyCount);
        } else if (v.getId() == R.id.msgdetail_iv_reply) {
            ((MessageDetailScreenlet) getParent()).performUserAction(MessageDetailScreenlet.ACTION_QUICK_REPLY, etReply.getText().toString());
        }
    }
}
