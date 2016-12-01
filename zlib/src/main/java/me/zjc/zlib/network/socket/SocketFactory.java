package me.zjc.zlib.network.socket;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

import rx.Observable;
import rx.Subscriber;

public final class SocketFactory {

    private SocketFactory() {}

    /**
     * 获取一个socket客户端
     * 会阻塞当前线程
     * @param host 服务器ip地址
     * @param port 服务器端口
     * @param serverCharset 服务器编码
     * @return 一个socket客户端
     * @throws IOException
     */
    public static ISocketClient getClient(String host, int port, @Nullable Charset serverCharset)
            throws IOException {
        return getClient(new Socket(host, port), serverCharset);
    }

    /**
     * 获取一个socket客户端, 默认使用客户端环境的编码
     * 会阻塞当前线程
     * @param host 服务器ip地址
     * @param port 服务器端口
     * @return 一个socket客户端
     * @throws IOException
     */
    public static ISocketClient getClient(String host, int port) throws IOException {
        return getClient(host, port, null);
    }

    /**
     * 将一个socket客户端包装成一个ISocketClient{@link ISocketClient}
     * @throws IOException
     */
    public static ISocketClient getClient(Socket socket, Charset serverCharset) throws IOException {
        Charset charset = serverCharset;
        if (charset == null)
            charset = Charset.defaultCharset();
        return new SocketClient(socket, charset);
    }

    public static ISocketClient getClient(Socket socket) throws IOException {
        return getClient(socket, null);
    }

    /**
     * 获取一个包装了socket客户端的Observable{@link Observable}
     * @param host 服务器ip地址
     * @param port 服务器端口
     * @param serverCharset 服务器编码
     * @return 包装了socket客户端的Observable
     */
    public static Observable<ISocketClient> getClientObservable(final String host, final int port,
                                                                @Nullable final Charset serverCharset) {
        return Observable.create(new Observable.OnSubscribe<ISocketClient>() {
            @Override
            public void call(Subscriber<? super ISocketClient> subscriber) {
                try {
                    subscriber.onNext(getClient(host, port, serverCharset));
                } catch (IOException e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        });
    }

    /**
     * 获取一个包装了socket客户端的Observable{@link Observable}
     * 默认使用客户端环境的编码
     * @param host 服务器ip地址
     * @param port 服务器端口
     * @return 包装了socket客户端的Observable
     */
    public static Observable<ISocketClient> getClientObservable(final String host, final int port) {
        return getClientObservable(host, port, null);
    }

    static ISocketClient getAsyncAcceptClient(ISocketClient client) {
        return new AsyncAcceptClient(client);
    }

    static ISocketClient getUiThreadAcceptActionClient(ISocketClient client) {
        return new UiAptActClient(client);
    }

}
