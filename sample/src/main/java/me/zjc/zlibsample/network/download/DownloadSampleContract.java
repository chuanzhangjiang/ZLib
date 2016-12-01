package me.zjc.zlibsample.network.download;


import me.zjc.zlib.mvp.BasePresenter;
import me.zjc.zlib.mvp.BaseView;

/**
 * Created by ChuanZhangjiang on 2016/11/21.
 * 下载Sample契约类
 */

public interface DownloadSampleContract {
    interface Presenter extends BasePresenter{
        void performanceDownload(DownloadConfig config);
        void pauseTask(String taskId);
        void startTask(String taskId);
        void cancelTask(String taskId);
    }

    interface View extends BaseView<Presenter>{
        void addADownloadTask(String taskId);
        void setProgressMax(String taskId, long contentLength);
        void updateProgress(String taskId, long progress);
        void setDownloadSuccess(String taskId);
        void setDownloadError(String taskId);
    }
}
