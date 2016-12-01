package me.zjc.zlib.common.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ChuanZhangjiang on 2016/11/15.
 * 提供SchedulersProvide{@link SchedulersProvide}
 */

public final class SchedulersProvides {

    private SchedulersProvides(){}

    private volatile static SchedulersProvide mNormalSchedulerProvide;
    private volatile static SchedulersProvide mImmediateSchedulersProvide;

    /**
     * 获取一个正常的线程调度提供者
     * @return 正常的线程调度提供者
     */
    public static SchedulersProvide getNormalSchedulerProvide() {
        if (mNormalSchedulerProvide == null) {
            synchronized (NormalSchedulersProvide.class) {
                if (mNormalSchedulerProvide == null)
                    mNormalSchedulerProvide = new NormalSchedulersProvide();
            }
        }
        return mNormalSchedulerProvide;
    }

    /**
     * 获取一个总是返回当前线程的线程调度提供者
     * @return 总是返回当前线程的线程调度提供者
     */
    public static SchedulersProvide getImmediateSchedulersProvide() {
        if (mImmediateSchedulersProvide == null) {
            synchronized (ImmediateSchedulersProvide.class) {
                if (mImmediateSchedulersProvide == null)
                    mImmediateSchedulersProvide = new ImmediateSchedulersProvide();
            }
        }
        return mImmediateSchedulersProvide;
    }

    /**
     * 正常的线程调度提供者
     */
    private static final class NormalSchedulersProvide implements SchedulersProvide {
        @NonNull
        @Override
        public Scheduler computation() {
            return Schedulers.computation();
        }

        @NonNull
        @Override
        public Scheduler io() {
            return Schedulers.io();
        }

        @NonNull
        @Override
        public Scheduler ui() {
            return AndroidSchedulers.mainThread();
        }

        @NonNull
        @Override
        public Scheduler immediate() {
            return Schedulers.immediate();
        }
    }

    /**
     * 总是返回当前线程的调度提供者
     */
    private static final class ImmediateSchedulersProvide implements SchedulersProvide {
        @NonNull
        @Override
        public Scheduler computation() {
            return Schedulers.immediate();
        }

        @NonNull
        @Override
        public Scheduler io() {
            return Schedulers.immediate();
        }

        @NonNull
        @Override
        public Scheduler ui() {
            return Schedulers.immediate();
        }

        @NonNull
        @Override
        public Scheduler immediate() {
            return Schedulers.immediate();
        }
    }

}
