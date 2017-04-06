package me.zjc.zlibsample.network.download;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import me.zjc.zlib.common.schedulers.SchedulersProvide;
import me.zjc.zlib.network.download.DownloadInfo;
import me.zjc.zlib.network.download.DownloadTask;
import rx.Subscriber;
import rx.Subscription;
import rx.internal.util.SubscriptionList;

import static me.zjc.zlib.common.utils.ArgumentChecker.checkNotNull;

/**
 * Created by ChuanZhangjiang on 2016/11/21.
 *
 */

public final class Presenter implements DownloadSampleContract.Presenter {
    @SuppressWarnings("unused")
    private static final String TAG = Presenter.class.getSimpleName();

    private final DownloadSampleContract.View mDownloadView;
    private final SchedulersProvide mSchedulersProvide;
    private final Map<String, DownloadTask> taskMap = new HashMap<>();
    private final SubscriptionList mSubscriptions = new SubscriptionList();

    private Presenter(DownloadSampleContract.View view, SchedulersProvide schedulersProvide) {
        mDownloadView = view;
        mSchedulersProvide = schedulersProvide;
    }

    public static Presenter newInstance(@NonNull DownloadSampleContract.View view,
                                        @NonNull SchedulersProvide schedulersProvide) {
        checkNotNull(view);
        checkNotNull(schedulersProvide);
        final Presenter presenter = new Presenter(view, schedulersProvide);
        view.setPresenter(presenter);
        return presenter;
    }

    @Override
    public void performanceDownload(DownloadConfig config) {
        if (config == null)
            return;
        final DownloadTask task = generateATask(config);
        taskMap.put(task.getId(), task);
        Subscription subscription = listenerTask(task);
        mSubscriptions.add(subscription);
        mDownloadView.addADownloadTask(task.getId());
        task.start();
    }

    private DownloadTask generateATask(DownloadConfig config) {
        return new DownloadTask.DownloadBuilder(config.getUrl()).
                setFileName(config.getFilename()).
                setPath(config.getPath()).build();
    }

    private Subscription listenerTask(final DownloadTask task) {
        task.setOnConnectSuccessListener(contentLength ->
                mDownloadView.setProgressMax(task.getId(), contentLength));

        return task.toObservable()
                .subscribeOn(mSchedulersProvide.io())
                .observeOn(mSchedulersProvide.ui())
                .subscribe(new Subscriber<DownloadInfo>() {
                    @Override
                    public void onCompleted() {
                        mDownloadView.setDownloadSuccess(task.getId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mDownloadView.setDownloadError(task.getId());
                    }

                    @Override
                    public void onNext(DownloadInfo downloadInfo) {
                        mDownloadView.updateProgress(task.getId(), downloadInfo.getProgress());
                    }
                });
    }

    @Override
    public void pauseTask(String taskId) {
        taskMap.get(taskId).pause();
    }

    @Override
    public void startTask(String taskId) {
        taskMap.get(taskId).continueDownload();
    }

    @Override
    public void cancelTask(String taskId) {
        taskMap.get(taskId).cancel();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
}
