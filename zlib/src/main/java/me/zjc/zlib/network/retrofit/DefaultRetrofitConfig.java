package me.zjc.zlib.network.retrofit;

import android.app.Application;
import android.os.Build;
import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static me.zjc.zlib.common.utils.ArgumentChecker.checkNotNull;

/**
 * Created by ChuanZhangjiang on 2016/12/17.
 * 默认的Retrofit配置
 */

public final class DefaultRetrofitConfig {

    private final ConfInfoProvider mInfoProvider;
    private DefaultRetrofitConfig(ConfInfoProvider infoProvider) {
        this.mInfoProvider = infoProvider;
    }

    /**
     * 静态工厂方法
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public static DefaultRetrofitConfig newInstance(@NonNull ConfInfoProvider infoProvider) {
        return new DefaultRetrofitConfig(checkNotNull(infoProvider));
    }

    /**
     * 对传入的Retrofit.Builder进行配置
     * @see retrofit2.Retrofit.Builder
     */
    @SuppressWarnings("unused")
    public void config(Retrofit.Builder builder) {
        final OkHttpClient client = getDefaultOkHttpClient();
        builder.baseUrl(mInfoProvider.getBaseUrl()).
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                client(client);
    }

    //获取OkHttpClient
    @SuppressWarnings("WeakerAccess")
    public OkHttpClient getDefaultOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        configDefaultCache(builder);
        configStetho(builder);
        configDefaultUserAgent(builder);
        configDefaultTimeout(builder);
        return builder.build();
    }

    /**
     * 配置缓存
     */
    @SuppressWarnings("WeakerAccess")
    public void configDefaultCache(OkHttpClient.Builder builder) {
        final File baseCacheDir = mInfoProvider.getApplication().getCacheDir();
        if (baseCacheDir != null) {
            final File cacheDir = new File(baseCacheDir, mInfoProvider.getApplication().getPackageName());
            builder.cache(new Cache(cacheDir, 10 * 1024 * 1024));
        }
    }

    /**
     * 配置Stetho
     */
    @SuppressWarnings("WeakerAccess")
    public void configStetho(OkHttpClient.Builder builder) {
        builder.addNetworkInterceptor(new StethoInterceptor());
    }

    /**
     * 配置user-agent信息
     */
    @SuppressWarnings("WeakerAccess")
    public void configDefaultUserAgent(OkHttpClient.Builder builder) {
        builder.addInterceptor(new UserAgentInterceptor(userAgentHeaderValue()));
    }

    /**
     * 配置超时
     */
    @SuppressWarnings("WeakerAccess")
    public void configDefaultTimeout(OkHttpClient.Builder builder) {
        builder.connectTimeout(15, TimeUnit.SECONDS).
                writeTimeout(20, TimeUnit.SECONDS).
                readTimeout(20, TimeUnit.SECONDS);
    }

    /**
     * 获取默认的UserAgent, 包括以下字段
     * os=Android
     * model="这是手机型号"
     * brand="手机品牌"
     * sdk_int_version="手机SDK版本号, 如：手机是Android5.0，sdk_int_version=21"
     * application_id="应用程序Id"
     * version_name="应用程序版本名称"
     * version_code="应用程序版本号"
     */
    @SuppressWarnings("WeakerAccess")
    public String userAgentHeaderValue() {
        return "os=Android;" +
                "model=" + Build.MODEL + ";" +
                "brand=" + Build.BRAND + ";" +
                "sdk_int_version=" + Build.VERSION.SDK_INT + ";" +
                "application_id=" + mInfoProvider.getApplicationId() + ";" +
                "version_name=" + mInfoProvider.getAppVersionName() + ";" +
                "version_code=" + mInfoProvider.getAppVersionCode();
    }

    /**
     * 添加User-Agent请求头信息
     */
    private static final class UserAgentInterceptor implements Interceptor {
        private static final String USER_AGENT_HEADER_NAME = "User-Agent";
        private final String userAgentHeaderValue;

        private UserAgentInterceptor(String userAgentHeaderValue) {
            this.userAgentHeaderValue = checkNotNull(userAgentHeaderValue);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request originalRequest = chain.request();
            final Request requestWithUserAgent = originalRequest.newBuilder()
                    .removeHeader(USER_AGENT_HEADER_NAME)
                    .addHeader(USER_AGENT_HEADER_NAME, userAgentHeaderValue)
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }

    /**
     * 配置信息
     * @see DefaultRetrofitConfig#newInstance(ConfInfoProvider)
     */
    @SuppressWarnings("WeakerAccess")
    public interface ConfInfoProvider{
        /**
         * 通过应用的BuildConfig获取ApplicationId
         */
        String getApplicationId();

        /**
         * 通过应用的BuildConfig获取versionName
         */
        String getAppVersionName();

        /**
         * 通过应用的BuildConfig获取versionCode
         */
        String getAppVersionCode();

        /**
         * 获取BaseUrl
         */
        String getBaseUrl();

        /**
         * 获取当前应用的Application实例
         */
        Application getApplication();
    }
}
