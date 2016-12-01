package me.zjc.zlib.network.socket;

import java.io.Closeable;

import rx.functions.Action1;


public interface ISocketClient extends Closeable {
    /**
     * 发送一条消息到服务器
     * @param msg 消息
     */
    void send(String msg);

    /**
     * 开始轮询监听服务器
     * @param action1 当服务器有返回数据的时候执行的动作
     */
    void accept(Action1<String> action1);

    /**
     * 判断客户端是否被关闭
     */
    boolean hasClosed();

    /**
     * 新开启一个线程来轮询服务器返回的消息
     * @return socket客户端
     */
    ISocketClient asyncLoopAccept();

    /**
     * 在主线程中获取从服务器中返回的消息
     * 只有android才能用
     * @return socket客户端
     */
    ISocketClient doAcceptActionOnMainThread();

}

