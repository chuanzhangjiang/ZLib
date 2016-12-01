package me.zjc.zlib.network.download;

import android.support.annotation.NonNull;

import me.zjc.zlib.common.utils.ArgumentChecker;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ChuanZhangjiang on 2016/11/20.
 * 模型层，用于链接资源
 */

final class DownloadModel {

    private final DownloadApi mApi;
    private final String mUrl;

    DownloadModel(@NonNull DownloadApi api, @NonNull String url) {
        mApi = ArgumentChecker.checkNotNull(api);
        mUrl = ArgumentChecker.checkNotNull(url);
    }

    Observable<Response<ResponseBody>> download() {
        return mApi.download(mUrl).
                subscribeOn(Schedulers.io()).
                observeOn(Schedulers.io()).
                map(new Func1<Result<ResponseBody>, Response<ResponseBody>>() {
                    @Override
                    public Response<ResponseBody> call(Result<ResponseBody> responseBodyResult) {
                        System.out.println(responseBodyResult.isError());
                        System.out.println(responseBodyResult.error());
                        System.out.println(responseBodyResult.response());
                        return responseBodyResult.response();
                    }
                }).
                onBackpressureBuffer();
    }

    Observable<Response<ResponseBody>> downloadFromPosition(long position, long contentLength) {
        return mApi.continueDownload(mUrl, generateRangeData(position, contentLength)).
                subscribeOn(Schedulers.io()).
                observeOn(Schedulers.io()).
                map(new Func1<Result<ResponseBody>, Response<ResponseBody>>() {
                    @Override
                    public Response<ResponseBody> call(Result<ResponseBody> responseBodyResult) {
                        return responseBodyResult.response();
                    }
                }).
                onBackpressureBuffer();
    }
    private String generateRangeData(long start, long end) {
        return "bytes=" + start + "-" + end;
    }

}
