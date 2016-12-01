package me.zjc.zlib.network.download;


import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.zjc.zlib.common.functions.FunctionHelper;
import me.zjc.zlib.common.schedulers.SchedulersProvides;
import me.zjc.zlib.common.io.ReleaseUtils;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * download executor
 * Execution really download operation
 */
final class DownloadExecutor {

    private final DownloadModel mDownloadModel;
    private final String mFilename;
    private final String mPath;
    private final Subject<DownloadInfo, DownloadInfo> mBus;

    private volatile boolean isPause = false;
    private volatile boolean isCancel = false;

    private long currentProgress = 0L;
    private long mContentLength = -1L;

    private Action0 mDownloadFinishListener;

    DownloadExecutor(@NonNull DownloadModel model, @NonNull String filename, @NonNull String path) {
        this.mDownloadModel = model;
        this.mFilename = filename;
        this.mPath = path;
        this.mBus = new SerializedSubject<>(PublishSubject.<DownloadInfo>create());
    }

    private final Action1<Throwable> mErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            cancel();
            mBus.onError(throwable);
        }
    };

    private final Func1<Response<ResponseBody>, Boolean> mErrorResponseFilter =
            new Func1<Response<ResponseBody>, Boolean>() {
                @Override
                public Boolean call(Response<ResponseBody> responseBodyResponse) {
                    if (!responseBodyResponse.isSuccessful()) {
                        mErrorAction.call(new Exception(responseBodyResponse.message()));
                    }
                    return responseBodyResponse.isSuccessful();
                }
            };

    private final Action0 mCompletedAction = new Action0() {
        @Override
        public void call() {
            if (isPause || isCancel)
                return;
            mBus.onCompleted();
            if (mDownloadFinishListener != null)
                mDownloadFinishListener.call();
        }
    };

    private Action1<Long> mConnectSuccessListener;
    void setOnConnectSuccessListener(Action1<Long> listener){
        mConnectSuccessListener = listener;
    }

    void startDownload() {
        resetStatus();
        mDownloadModel.download().
                filter(mErrorResponseFilter).
                map(new Func1<Response<ResponseBody>, ResponseBody>() {
                    @Override
                    public ResponseBody call(Response<ResponseBody> responseBodyResponse) {
                        return responseBodyResponse.body();
                    }
                }).
                subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(final ResponseBody responseBody) {
                        mContentLength = responseBody.contentLength();
                        FunctionHelper.safeInvokeAction1InUiThread(mConnectSuccessListener,
                                mContentLength, SchedulersProvides.getNormalSchedulerProvide());

                        saveFileFromInputStream(responseBody.byteStream(), new OutputStreamMaker() {
                            @Override
                            public OutputStream make() throws FileNotFoundException {
                                return new FileOutputStream(new File(mPath + mFilename), false);
                            }
                        });
                    }
                }, mErrorAction, mCompletedAction);
    }

    void continueDownload() {
        resetStatus();
        mDownloadModel.downloadFromPosition(currentProgress, mContentLength).
                filter(mErrorResponseFilter).
                map(new Func1<Response<ResponseBody>, ResponseBody>() {
                    @Override
                    public ResponseBody call(Response<ResponseBody> responseBodyResponse) {
                        return responseBodyResponse.body();
                    }
                }).
                subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        saveFileFromInputStream(responseBody.byteStream(), new OutputStreamMaker() {
                            @Override
                            public OutputStream make() throws FileNotFoundException {
                                return new FileOutputStream(new File(mPath + mFilename), true);
                            }
                        });
                    }
                }, mErrorAction, mCompletedAction);
    }

    private void resetStatus() {
        isPause = false;
        isCancel = false;
    }

    void pause() {
        this.isPause = true;
    }

    void cancel() {
        this.isCancel = true;
    }

    Observable<DownloadInfo> toObservable() {
        return mBus.asObservable();
    }

    void setDownloadFinishListener(Action0 listener) {
        this.mDownloadFinishListener = listener;
    }

    private void saveFileFromInputStream(final InputStream is, OutputStreamMaker maker) {
        BufferedInputStream bis = new BufferedInputStream(is);
        OutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = maker.make();
            bos = new BufferedOutputStream(fos);
            byte[] buf = new byte[2048];

            final DownloadInfo info = new DownloadInfo(currentProgress, mContentLength);

            for (long readLength;
                 !isCancel && !isPause && (readLength = bis.read(buf)) != -1; ) {

                bos.write(buf, 0, (int) readLength);

                mBus.onNext(info.setProgress(currentProgress += readLength));

                Log.e("Download: ", currentProgress + "");
            }
        } catch (IOException e) {
            mBus.onError(e);
        } finally {
            ReleaseUtils.flushAll(bos, fos);
            ReleaseUtils.closeAll(bos, fos, bis, is);
            if (isCancel) {
                deleteFile();
            }
        }
    }

    private void deleteFile() {
        Observable.just(new File(mPath + mFilename))
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.exists();
                    }
                })
                .map(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.delete();
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            System.err.print("delete file failure");
                        }
                    }
                });
    }

    interface OutputStreamMaker{
        OutputStream make() throws FileNotFoundException;
    }

}
