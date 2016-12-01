package me.zjc.zlib.common.io;


import java.io.Closeable;
import java.io.Flushable;
import java.util.LinkedList;

/**
 * Created by ChuanZhangjiang on 2016/11/25.
 * 用来管理输入输出流的关闭
 */

public final class StreamManager {
    private final LinkedList<Closeable> closeables = new LinkedList<>();
    private final LinkedList<Flushable> flushables = new LinkedList<>();

    /**
     * 添加一个可被关闭的流
     * 先添加的会被后关闭
     */
    public void addCloseable(Closeable closeable){
        closeables.addFirst(closeable);
    }

    /**
     * 添加一个可被flush的流
     * 先添加的会被厚flush
     */
    public void addFlushable(Flushable flushable) {
        flushables.addFirst(flushable);
    }

    public void release() {
        ReleaseUtils.flushAll(flushables);
        ReleaseUtils.closeAll(closeables);
    }
}
