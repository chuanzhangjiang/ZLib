package me.zjc.zlib.common.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.Collection;

import rx.Observable;
/**
 * Created by ChuanZhangjiang on 2016/8/7.
 * 资源释放工具，用于IO操作结束的时候释放资源
 */
public final class ReleaseUtils {
    private ReleaseUtils() {
        throw new IllegalAccessError();
    }

    /**
     * 执行刷新操作
     * 刷新顺序：从传入的第一个参数开始
     * @param flushes 被刷新的对象
     */
    public static void flushAll(Flushable... flushes) {
        Observable.from(flushes).
                filter(flushable -> flushable != null).
                subscribe(flushable -> {
                    try {
                        flushable.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 执行刷新操作
     * 从集合的第一个元素开始刷新
     * @param flushes 被刷新的集合
     */
    @SuppressWarnings("WeakerAccess")
    public static void flushAll(Collection<? extends Flushable> flushes) {
        Observable.from(flushes).
                filter(flushable -> flushable != null).
                subscribe(flushable -> {
                    try {
                        flushable.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 执行关闭操作
     * 刷新顺序：从传入的第一个参数开始
     * @param closeables 被关闭的对象
     */
    public static void closeAll(Closeable... closeables) {
        Observable.from(closeables).
                filter(closeable -> closeable != null).
                subscribe(closeable -> {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 执行关闭操作
     * 刷新顺序：从集合的第一个元素开始
     * @param closeables 被关闭的对象
     */
    @SuppressWarnings("WeakerAccess")
    public static void closeAll(Collection<? extends Closeable> closeables) {
        Observable.from(closeables).
                filter(closeable -> closeable != null).
                subscribe(closeable -> {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

}
