package me.zjc.zlib.common.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Created by ChuanZhangjiang on 2016/11/15.
 * RxJava中线程调度提供者
 */

public interface SchedulersProvide {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();

    /**
     * 当前线程
     */
    @NonNull
    Scheduler immediate();
}
