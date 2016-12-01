package me.zjc.zlib.base;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by ChuanZhangjiang on 2016/11/22.
 *
 */

public class BaseApplication extends Application {

    /**
     * 开启严格模式
     */
    protected void openStrictMode() {
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
}
