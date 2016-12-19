package me.zjc.zlib.network.retrofit;

import net.jcip.annotations.GuardedBy;

import retrofit2.Retrofit;
import rx.functions.Action1;

import static me.zjc.zlib.common.utils.ArgumentChecker.checkNotNull;

/**
 * Created by ChuanZhangjiang on 2016/12/16.
 * Retrofit辅助类, 辅助完成动态创建后台接口调用类
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public final class RetrofitHelper {
    /**
     * 单例
     */
    @GuardedBy("RetrofitHelper.class")
    private static volatile RetrofitHelper mInstance;
    public static RetrofitHelper getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitHelper.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitHelper();
                }
            }
        }
        return mInstance;
    }
    private RetrofitHelper(){
    }

    private Retrofit mRetrofit;
    /**
     * 初始化RetrofitHelper
     * @param initAction 初始化操作策略
     */
    public synchronized void init(Action1<Retrofit.Builder> initAction) {
        checkNotNull(initAction);
        final Retrofit.Builder builder = new Retrofit.Builder();
        initAction.call(builder);
        mRetrofit = builder.build();
    }

    private synchronized Retrofit getRetrofit() {
        return mRetrofit;
    }

    /**
     * 动态代理一个接口
     * @param apiClazz 调用后台接口的接口类型
     */
    public <T> T create(Class<T> apiClazz) {
       checkNotNull(getRetrofit(), "请先调用RetrofitHelper#init()方法。");
        return getRetrofit().create(apiClazz);
    }
}
