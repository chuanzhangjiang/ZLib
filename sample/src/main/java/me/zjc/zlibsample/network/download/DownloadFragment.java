package me.zjc.zlibsample.network.download;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import me.zjc.zlib.base.BaseFragment;
import me.zjc.zlibsample.R;

/**
 * Created by ChuanZhangjiang on 2016/11/20.
 *
 */

public final class DownloadFragment extends BaseFragment implements DownloadSampleContract.View {

    private LinearLayout mDownloadContainer;
    private DownloadConfigView mDownloadConfigView;

    private final Map<String, DownloadItemView> mDownloadItemViews = new HashMap<>();

    private DownloadSampleContract.Presenter mPresenter;

    @Override
    protected void initView(View contentView) {
        mDownloadContainer = (LinearLayout) contentView.findViewById(R.id.ll_container);
        mDownloadConfigView = (DownloadConfigView) contentView.findViewById(R.id.view_download_config);

        mDownloadConfigView.listenerDownloadButton(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                checkPermissionAndDownload();
            } else {
                performanceDownload();
            }
        });
    }

    private void performanceDownload() {
        if (getDownloadConfig().isNoUrlInput()) {
            showSnackBar("请输入url");
            return;
        }
        mPresenter.performanceDownload(getDownloadConfig());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkPermissionAndDownload() {
        rxPermissions().
                request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE).
                subscribe(isGranted -> {
                    if (isGranted) {
                        showSnackBar("有权限");
                        Log.e(getFragmentTag(), "有权限");
                        performanceDownload();
                    } else {
                        showSnackBar("没有权限");
                        Log.e(getFragmentTag(), "没权限");
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_download;
    }

    private DownloadConfig getDownloadConfig() {
        final DownloadConfig config = mDownloadConfigView.getDownloadConfig();
        if (config == null) {
            showSnackBar("请输入资源链接");
        }
        return config;
    }

    @Override
    public void addADownloadTask(final String taskId) {
        final DownloadItemView itemView = new DownloadItemView(getContext());
        addDownloadItemToContainer(itemView);
        itemView.toStartedStatus();
        listenerDownloadItemView(itemView, taskId);
        mDownloadItemViews.put(taskId, itemView);
    }

    private void listenerDownloadItemView(final DownloadItemView itemView,
                                          final String taskId) {
        itemView.setPauseClickListener(() -> {
            itemView.toPauseStatus();
            mPresenter.pauseTask(taskId);
        }).setStartClickListener(() -> {
            itemView.toStartedStatus();
            mPresenter.startTask(taskId);
        }).setCancelClickListener(() -> {
            itemView.toCancelStatus();
            mPresenter.cancelTask(taskId);
        });
    }

    @Override
    public void setProgressMax(String taskId, long contentLength) {
        if (mDownloadItemViews.get(taskId) == null)
            return;
        mDownloadItemViews.get(taskId).setMax((int) contentLength);
    }

    private void addDownloadItemToContainer(View downloadItemView) {
        mDownloadContainer.addView(downloadItemView, 1, getDefaultDownloadItemViewParam());
    }

    private ViewGroup.LayoutParams getDefaultDownloadItemViewParam() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        final int defaultTopMargin = dip2px(8);
        params.setMargins(0, defaultTopMargin, 0, 0);
        return params;
    }

    @Override
    public void updateProgress(String taskId, long progress) {
        if (mDownloadItemViews.get(taskId) == null)
            return;
        mDownloadItemViews.get(taskId).setProgress((int) progress);
    }


    @Override
    public void setDownloadSuccess(String taskId) {
        if (mDownloadItemViews.get(taskId) == null)
            return;
        mDownloadItemViews.get(taskId).toSuccessStatus();
    }

    @Override
    public void setDownloadError(String taskId) {
        if (mDownloadItemViews.get(taskId) != null) {
            mDownloadItemViews.get(taskId).toErrorStatus();
        }
        showSnackBar("下载出错！");
    }

    @Override
    public void setPresenter(DownloadSampleContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
