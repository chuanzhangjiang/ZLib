package me.zjc.zlib.network.retrofit;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by ChuanZhangjiang on 2016/12/18.
 * 转换函数辅助类
 */

@SuppressWarnings("unused")
public final class Transformers {
    private Transformers(){}

    /**
     * 解构Observable<Result<T>>为Observable<T>
     * 结合{@link Observable#compose(Observable.Transformer)}使用
     */
    public static <T> Observable<T> resultToData(Observable<Result<? extends T>> inputObservable) {
        return inputObservable.flatMap(
                result -> Observable.create(
                        subscriber -> {
                            if (onNetworkError(result, subscriber)) {
                                return;
                            }

                            if (onServerError(result, subscriber)) {
                                return;
                            }

                            subscriber.onNext(result.response().body());
                            subscriber.onCompleted();
                        }
                )
        );
    }

    /**
     * 网络出错时会返回true
     */
    private static <T> boolean onNetworkError(Result<? extends T> result,
                                              Subscriber<? super T> subscriber) {
        if (result.isError()) {
            subscriber.onError(result.error());
            return true;
        }
        if (result.response() == null) {
            subscriber.onError(new NullPointerException("no response"));
            return true;
        }
        return false;
    }

    /**
     * 服务器出错时会返回true
     */
    private static <T> boolean onServerError(Result<? extends T> result,
                                              Subscriber<? super T> subscriber) {
        final Response<? extends T> response = result.response();
        if (!response.isSuccessful()) {
            subscriber.onError(new IOException(response.message()));
            return true;
        }
        return false;
    }
}
