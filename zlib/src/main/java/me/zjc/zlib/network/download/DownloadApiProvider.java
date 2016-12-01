package me.zjc.zlib.network.download;

import android.util.Log;

import net.jcip.annotations.GuardedBy;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by ChuanZhangjiang on 2016/8/5.
 * 用于提供DownloadApi的实现类
 */
final class DownloadApiProvider {
    @GuardedBy("DownloadApiProvider.class")
    private static volatile DownloadApi mDownloadApi = null;

    private DownloadApiProvider() {
        throw new IllegalAccessError();
    }

    static DownloadApi getDefaultDownLoadApi() {
        if (mDownloadApi == null) {
            synchronized (DownloadApiProvider.class) {
                if (mDownloadApi == null) {
                    mDownloadApi = createDownloadApi();
                }
            }
        }
        return mDownloadApi;
    }

    static DownloadApi newDownloadApi(Interceptor... interceptors) {
        return createDownloadApi(interceptors);
    }

    private static DownloadApi createDownloadApi(Interceptor... interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        for (Interceptor interceptor: interceptors){
            builder.addInterceptor(interceptor);
        }
        builder.addInterceptor(loggerInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://noused/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(builder.build())
                .build();
        return retrofit.create(DownloadApi.class);
    }

    private static Interceptor loggerInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.e("HttpLogger: ", message);
                    }
                });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return loggingInterceptor;
    }
}
