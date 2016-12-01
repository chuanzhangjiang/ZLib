package me.zjc.zlibsample.network.download;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

import me.zjc.zlibsample.R;

/**
 * Created by ChuanZhangjiang on 2016/11/22.
 *
 */

public final class DownloadConfigView extends CardView {

    private EditText mUrlEt;
    private EditText mFilenameEt;
    private EditText mPathEt;
    private Button mDownloadBtn;

    public DownloadConfigView(Context context) {
        super(context);
        init(context);
    }

    public DownloadConfigView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DownloadConfigView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_download_config, this, true);
    }

    public DownloadConfig getDownloadConfig() {
        final String url = mUrlEt.getText().toString();
        final String filename = mFilenameEt.getText().toString();
        final String path = mPathEt.getText().toString();
        return DownloadConfig.getInstance(url, filename, path);
    }

    public DownloadConfigView listenerDownloadButton(OnClickListener listener) {
        mDownloadBtn.setOnClickListener(listener);
        return this;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mUrlEt = (EditText) findViewById(R.id.et_url);
        mFilenameEt = (EditText) findViewById(R.id.et_filename);
        mPathEt = (EditText) findViewById(R.id.et_path);
        mDownloadBtn = (Button) findViewById(R.id.btn_download);
    }
}
