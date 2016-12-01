package me.zjc.zlib.network.download;

import android.support.annotation.NonNull;

import net.jcip.annotations.GuardedBy;

import me.zjc.zlib.common.io.PathUtils;
import me.zjc.zlib.common.utils.RandomUtils;
import me.zjc.zlib.common.utils.TextUtils;
import okhttp3.Interceptor;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by ChuanZhangjiang on 2016/8/7.
 * 下载任务
 */
public final class DownloadTask {

    private final DownloadBuilder mBuilder;

    @GuardedBy("this")
    private Status mStatus;
    private final DownloadExecutor mExecutor;

    private DownloadTask(DownloadExecutor executor, DownloadBuilder builder) {
        this.mExecutor = executor;
        this.mBuilder = builder;
        mStatus = Status.READY;
    }

    public DownloadTask onDownloadFinish(Action0 listener) {
        mExecutor.setDownloadFinishListener(listener);
        return this;
    }

    /**
     * 开始下载
     * @throws IllegalStateException
     * @return DownloadTask 自身对象
     */
    public DownloadTask start() {
        synchronized (this) {
            if (mStatus != Status.READY)
                throw new IllegalStateException("only clear task can be start");
            mStatus = Status.START;
        }
        mExecutor.startDownload();
        return this;
    }

    /**
     * 继续下载
     * @return self
     */
    public DownloadTask continueDownload() {
        synchronized (this) {
            if (mStatus != Status.PAUSE)
                throw new IllegalStateException("only paused task can be continue");
            mStatus = Status.START;
        }
        mExecutor.continueDownload();
        return this;
    }

    /**
     * 暂停方法
     */
    public void pause() {
        synchronized (this) {
            mStatus = Status.PAUSE;
        }
        mExecutor.pause();
    }

    /**
     * 取消任务
     * @throws IllegalStateException
     */
    public void cancel() {
        synchronized (this) {
            mStatus = Status.CANCEL;
        }
        mExecutor.cancel();
    }

    /**
     * 克隆一个与当前任务具有相同配置的任务
     * @return 克隆之后的任务
     */
    public DownloadTask selfClone() {
        return mBuilder.build();
    }

    /**
     * 返回一个可被观察的对象，实现监视任务进度
     * @return 可被观察的对象
     * @see Observable;
     */
    public Observable<DownloadInfo> toObservable() {
        return mExecutor.toObservable().onBackpressureBuffer();
    }

    private final String mId = RandomUtils.generateRandomId();

    /**
     * 获取该任务的ID
     */
    public String getId() {
        return mId;
    }

    /**
     * 资源第一次连接成功时候被回调，传入资源大小
     */
    public void setOnConnectSuccessListener(Action1<Long> listener){
        mExecutor.setOnConnectSuccessListener(listener);
    }

    private enum Status{
        READY, START, PAUSE, CANCEL
    }

    /**
     * 下载任务构建器
     * 如果没有设置文件名，则默认使用url中的后缀
     * 如果没有设置路径则默认下载到“/storage/emulated/0/Download”
     * 如果没有设置监听，则不回调
     */
    public static class DownloadBuilder {
        private final String url;
        private String fileName;
        private String path;
        private DownloadApi api;

        /**
         * 构造器
         * @param url 文件下载的网络路径
         */
        public DownloadBuilder(@NonNull String url) {
            this.url = url;
        }

        /**
         * 设置下载到本地之后的文件名
         * @param fileName 文件名
         * @return 设置了文件名的构建器
         */
        public DownloadBuilder setFileName(@NonNull String fileName) {
            this.fileName = fileName;
            return this;
        }

        /**
         * 设置下载到本地的某个目录
         * @param path 某个目录
         * @return 设置了目录的构建器
         */
        public DownloadBuilder setPath(@NonNull String path) {
            this.path = path;
            return this;
        }

        /**
         * 设置拦截器
         * @param interceptors 拦截器{@link Interceptor}
         * @return 自生
         */
        public DownloadBuilder setInterceptors(@NonNull Interceptor... interceptors) {
            this.api = DownloadApiProvider.newDownloadApi(interceptors);
            return this;
        }

        /**
         * 构建下载任务
         * @return 下载任务
         * @see DownloadTask
         */
        public DownloadTask build() {
            ifNullSetDefault();
            PathUtils.ifDirNotExistsCreateIt(path);
            DownloadExecutor executor = new DownloadExecutor(new DownloadModel(api, url),
                    fileName, path);
            return new DownloadTask(executor, this);
        }

        private void ifNullSetDefault() {
            if (TextUtils.isEmpty(fileName)) {
                int index = url.lastIndexOf("/");
                fileName = url.substring(index + 1);
            }

            if (TextUtils.isEmpty(path)) {
                path = PathUtils.getDefaultDownloadPath();
            }

            if (api == null) {
                api = DownloadApiProvider.getDefaultDownLoadApi();
            }
        }
    }
}
