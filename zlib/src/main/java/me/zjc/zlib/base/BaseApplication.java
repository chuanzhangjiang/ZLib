package me.zjc.zlib.base;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by ChuanZhangjiang on 2016/11/22.
 *
 */

public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (isDebug()) {
            openStrictMode();
        }
    }

    /**
     * 开启严格模式
     */
    protected final void openStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .detectAll()
                .penaltyLog()
                //penaltyDeath()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectAll()
                .penaltyLog()
                //.penaltyDeath()
                .build());
    }

    public abstract boolean isDebug();
}
