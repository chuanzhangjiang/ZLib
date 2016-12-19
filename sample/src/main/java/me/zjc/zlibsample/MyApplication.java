package me.zjc.zlibsample;

import android.app.Application;

import me.zjc.zlib.base.BaseApplication;
import me.zjc.zlib.network.retrofit.DefaultRetrofitConfig;
import me.zjc.zlib.network.retrofit.RetrofitHelper;

/**
 * Created by ChuanZhangjiang on 2016/11/22.
 *
 */

public final class MyApplication extends BaseApplication implements DefaultRetrofitConfig.ConfInfoProvider {
    @Override
    public void onCreate() {
        super.onCreate();
//        RetrofitHelper.
//                getInstance().
//                init(DefaultRetrofitConfig.newInstance(this)::config);
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public String getApplicationId() {
        return BuildConfig.APPLICATION_ID;
    }

    @Override
    public String getAppVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public String getAppVersionCode() {
        return BuildConfig.VERSION_CODE + "";
    }

    @Override
    public String getBaseUrl() {
        return "";
    }

    @Override
    public Application getApplication() {
        return this;
    }
}
