package me.zjc.zlibsample.network.download;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import me.zjc.zlib.common.functions.FunctionHelper;
import me.zjc.zlibsample.R;
import rx.functions.Action0;

/**
 * Created by ChuanZhangjiang on 2016/11/21.
 *
 */

public final class DownloadItemView extends CardView {

    private ProgressBar mDownloadProgressBar;
    private Button mPauseBtn;
    private Action0 mPauseClickCallBack;
    private Button mStartBtn;
    private Action0 mStartClickCallBack;
    private Button mCancelBtn;
    private Action0 mCancelClickCallBack;

    public DownloadItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DownloadItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DownloadItemView(Context context) {
        super(context);
        final View contentView = init(context);
        bindView(contentView);
        listenerEvent();
    }

    private View init(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.item_download, this, true);
    }

    public void setMax(int max) {
        mDownloadProgressBar.setMax(max);
    }

    public void setProgress(int progress) {
        mDownloadProgressBar.setProgress(progress);
    }

    public DownloadItemView setPauseClickListener(Action0 callBack) {
        mPauseClickCallBack = callBack;
        return this;
    }

    public DownloadItemView setStartClickListener(Action0 callBack) {
        mStartClickCallBack = callBack;
        return this;
    }

    public DownloadItemView setCancelClickListener(Action0 callBack) {
        mCancelClickCallBack = callBack;
        return this;
    }

    public void toSuccessStatus() {
        toCancelStatus();
    }

    public void toErrorStatus() {
        toCancelStatus();
    }

    public void toCancelStatus() {
        mStartBtn.setEnabled(false);
        mCancelBtn.setEnabled(false);
        mPauseBtn.setEnabled(false);
    }

    public void toStartedStatus() {
        mStartBtn.setEnabled(false);
        mCancelBtn.setEnabled(true);
        mPauseBtn.setEnabled(true);
    }

    public void toPauseStatus() {
        mStartBtn.setEnabled(true);
        mCancelBtn.setEnabled(true);
        mPauseBtn.setEnabled(false);
    }

    private void bindView(View contentView) {
        mDownloadProgressBar = (ProgressBar) contentView.findViewById(R.id.pb_download_progress);
        mPauseBtn = (Button) contentView.findViewById(R.id.btn_pause);
        mStartBtn = (Button) contentView.findViewById(R.id.btn_start);
        mCancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
    }

    private void listenerEvent() {
        mPauseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FunctionHelper.safeInvokeAction0(mPauseClickCallBack);
            }
        });

        mStartBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FunctionHelper.safeInvokeAction0(mStartClickCallBack);
            }
        });

        mCancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FunctionHelper.safeInvokeAction0(mCancelClickCallBack);
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bindView(this);
        listenerEvent();
    }
}
