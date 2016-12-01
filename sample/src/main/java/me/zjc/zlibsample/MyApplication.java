package me.zjc.zlibsample;

import me.zjc.zlib.base.BaseApplication;

/**
 * Created by ChuanZhangjiang on 2016/11/22.
 *
 */

public final class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            openStrictMode();
        }
    }
}
