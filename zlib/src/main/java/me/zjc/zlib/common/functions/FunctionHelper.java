package me.zjc.zlib.common.functions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.zjc.zlib.common.schedulers.SchedulersProvide;
import me.zjc.zlib.common.schedulers.SchedulersProvides;
import me.zjc.zlib.common.utils.ArgumentChecker;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Action3;


/**
 * Created by ChuanZhangjiang on 2016/11/24.
 *
 */

public final class FunctionHelper {
    private FunctionHelper(){}

    /**
     * 安全调用action0,免去对action0的非空判断
     */
    public static void safeInvokeAction0(@Nullable Action0 action0) {
        if (action0 != null)
            action0.call();
    }

    /**
     * 安全调用action1,免去对action1的非空判断
     */
    @SuppressWarnings("unused")
    public static <T> void safeInvokeAction1(@Nullable Action1<T> action1, T param) {
        if (action1 != null)
            action1.call(param);
    }

    /**
     * 安全调用action2,免去对action2的非空判断
     */
    @SuppressWarnings("unused")
    public static <T1, T2> void safeInvokeAction2(@Nullable Action2<T1, T2> action2,
                                                  T1 param1, T2 param2) {
        if (action2 != null)
            action2.call(param1, param2);
    }

    /**
     * 安全调用action3,免去对action3的非空判断
     */
    @SuppressWarnings("unused")
    public static <T1, T2, T3> void safeInvokeAction3(@Nullable Action3<T1, T2, T3> action3,
                                                      T1 param1, T2 param2, T3 param3) {
        if (action3 != null) {
            action3.call(param1, param2, param3);
        }
    }

    /**
     * 在Ui线程中调用Action0.call(), 并且不用对action0做非空判断
     * @param provide 线程调度提供者{@link SchedulersProvide} &&
     * {@link SchedulersProvides}
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static void safeInvokeAction0InUiThread(@Nullable Action0 action0,
                                                   @NonNull SchedulersProvide provide) {
        ArgumentChecker.checkNotNull(provide);
        if (action0 != null)
            Observable.just(null).
                    subscribeOn(provide.immediate()).
                    observeOn(provide.ui()).
                    subscribe(nothing -> action0.call());
    }

    /**
     * 在Ui线程中调用Action1<T>#call(T), 并且不用对action1做非空判断
     * @param provide 线程调度提供者{@link SchedulersProvide} &&
     * {@link SchedulersProvides}
     */
    public static <T> void safeInvokeAction1InUiThread(@Nullable Action1<T> action1, final T param,
                                                       @NonNull SchedulersProvide provide) {
        ArgumentChecker.checkNotNull(provide);
        if (action1 != null)
            Observable.just(null).
                    subscribeOn(provide.immediate()).
                    observeOn(provide.ui()).
                    subscribe(nothing -> action1.call(param));
    }

    /**
     * 在Ui线程中调用Action2.call(), 并且不用对action2做非空判断
     * @param provide 线程调度提供者{@link SchedulersProvide} &&
     * {@link SchedulersProvides}
     */
    @SuppressWarnings("unused")
    public static <T1, T2> void safeInvokeAction2InUiThread(@Nullable Action2<T1, T2> action2,
                                                            final T1 param1, final T2 param2,
                                                            @NonNull SchedulersProvide provide) {
        ArgumentChecker.checkNotNull(provide);
        if (action2 != null)
            Observable.just(null).
                    subscribeOn(provide.immediate()).
                    observeOn(provide.ui()).
                    subscribe(nothing -> action2.call(param1, param2));
    }

}
