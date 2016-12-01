package me.zjc.zlibsample.network.socket;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import me.zjc.zlib.common.rxbus.Rxbus;
import me.zjc.zlib.network.socket.ISocketClient;
import me.zjc.zlib.network.socket.SocketFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 模拟socket服务器
 */
public class SocketServerService extends IntentService {

    public static final int SERVER_PORT = 8081;
    public static final String SERVER_HOST = "127.0.0.1";

    private ServerSocket mServer;

    public SocketServerService() {
        super("SocketServerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            mServer = new ServerSocket(SERVER_PORT, 0, InetAddress.getLocalHost());
            Log.e("SocketServerService", "服务启动 " + mServer.getInetAddress().getHostAddress());
            Rxbus.INSTANCE.post(Event.SERVER_START);
            observerServer(mServer).
                    subscribeOn(Schedulers.immediate()).
                    observeOn(Schedulers.io()).
                    subscribe(mSocketClientHandler, mErrorAction);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SocketServerService", "服务启动失败");
        }
    }

    private final Action1<Throwable> mErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            throwable.printStackTrace();
        }
    };

    private final Action1<ISocketClient> mSocketClientHandler = new Action1<ISocketClient>() {
        @Override
        public void call(final ISocketClient iSocketClient) {
            Log.e("SocketServerService", "有客户端连接");
            iSocketClient.accept(new Action1<String>() {
                @Override
                public void call(String s) {
                    Log.e("SocketServerService", "收到：" + s);
                    if ("stop".equals(s)) {
                        try {
                            iSocketClient.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        closeServer();
                    } else {
                        iSocketClient.send("服务器收到: " + s);
                    }
                }
            });
        }
    };

    private synchronized void closeServer() {
        try {
            if (mServer != null && !mServer.isClosed())
                mServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Observable<ISocketClient> observerServer(final ServerSocket server) {
        return Observable.create(new Observable.OnSubscribe<ISocketClient>() {
            @Override
            public void call(Subscriber<? super ISocketClient> subscriber) {
                try {
                    while (!server.isClosed()) {
                        server.setSoTimeout(3 * 60 * 1000);
                        final Socket socket = server.accept();
                        subscriber.onNext(SocketFactory.getClient(socket));
                    }
                    subscriber.unsubscribe();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                    subscriber.unsubscribe();
                }
            }
        });
    }

    public enum Event {
        SERVER_START,
        SERVER_CLOSE
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("SocketServer", "onDestroy");
        Rxbus.INSTANCE.post(Event.SERVER_CLOSE);
    }
}
