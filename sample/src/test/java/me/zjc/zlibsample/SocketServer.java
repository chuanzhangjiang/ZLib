package me.zjc.zlibsample;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import me.zjc.zlib.network.socket.ISocketClient;
import me.zjc.zlib.network.socket.SocketFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ChuanZhangjiang on 2016/11/27.
 *
 */

public class SocketServer {

    public static final int SERVER_PORT = 8081;
    public static final String SERVER_HOST = "127.0.0.1";
    public static final String LOCAL_HOST = "192.168.177.2";

    @Test
    public void startServer() throws Exception {
        delayCreateSocketClient(3, TimeUnit.SECONDS);
        final ServerSocket server = new ServerSocket(SERVER_PORT, 0, InetAddress.getLocalHost());
        observerServer(server).
                subscribeOn(Schedulers.immediate()).
                observeOn(Schedulers.io()).
                subscribe(new Action1<ISocketClient>() {
                    @Override
                    public void call(ISocketClient iSocketClient) {
                        System.out.println("客户端进入");
                        final ISocketClient client = iSocketClient.asyncLoopAccept();
                        client.accept(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                System.out.println("来自客户端： " + s);
                                if (s.equals("stop")) {
                                    try {
                                        client.close();
                                        server.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private Observable<ISocketClient> observerServer(final ServerSocket server) {
        return Observable.create(new Observable.OnSubscribe<ISocketClient>() {
            @Override
            public void call(Subscriber<? super ISocketClient> subscriber) {
                try {
                    while (!server.isClosed()) {
                        final Socket socket = server.accept();
                        subscriber.onNext(SocketFactory.getClient(socket));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    private void delayCreateSocketClient(int delay, TimeUnit timeUnit) {
        Observable.timer(delay, timeUnit).
                subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object aLong) {
                        try {
                            ISocketClient client = SocketFactory.getClient(LOCAL_HOST, SERVER_PORT);
                            Thread.sleep(2*1000);
                            client.send("stop");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 监听客户端的连接
     */
    private Observable<ISocketClient> observeClientConnect(final ServerSocket server) {
        return Observable.
                just(server).
                retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(final Observable<? extends Throwable> observable) {
                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                System.out.println("retry");
                                return observable;
                            }
                        });
                    }
                }).
                repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(final Observable<? extends Void> observable) {
                        return ifServerNotCloseRepeatIt(observable, server);
                    }
                }).
                compose(new Observable.Transformer<ServerSocket, Socket>() {
                    @Override
                    public Observable<Socket> call(Observable<ServerSocket> serverSocketObservable) {
                        return waitSocketClient(serverSocketObservable);
                    }
                }).
                compose(new Observable.Transformer<Socket, ISocketClient>() {
                    @Override
                    public Observable<ISocketClient> call(Observable<Socket> socketObservable) {
                        return wrapperSocket(socketObservable);
                    }
                }).
                onBackpressureBuffer();
    }

    private Observable<?> ifServerNotCloseRepeatIt(final Observable<? extends Void> source,
                                                   final ServerSocket serverSocket) {
        return source.flatMap(new Func1<Void, Observable<?>>() {
            @Override
            public Observable<?> call(Void aVoid) {
                System.out.println("repeat");
                if (serverSocket.isClosed()) {
                    System.out.println("stop repeat");
                    return Observable.empty();
                } else {
                    System.out.println("repeat it");
                    return source;
                }
            }
        });
    }

    private Observable<Socket> waitSocketClient(Observable<ServerSocket> serverObservable) {
        return serverObservable.
                map(new Func1<ServerSocket, Socket>() {
                    @Override
                    public Socket call(ServerSocket serverSocket) {
                        try {
                            return serverSocket.accept();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }).
                filter(new Func1<Socket, Boolean>() {
                    @Override
                    public Boolean call(Socket socket) {
                        return socket != null;
                    }
                });
    }

    private Observable<ISocketClient> wrapperSocket(Observable<Socket> socketObservable) {
        return socketObservable.
                map(new Func1<Socket, ISocketClient>() {
                    @Override
                    public ISocketClient call(Socket socket) {
                        try {
                            return SocketFactory.getClient(socket);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }).
                filter(new Func1<ISocketClient, Boolean>() {
                    @Override
                    public Boolean call(ISocketClient iSocketClient) {
                        return iSocketClient != null;
                    }
                });
    }

    @Test
    public void repeatTest() throws Exception {
        Observable.just(1).
                repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(final Observable<? extends Void> observable) {
                        return observable.flatMap(new Func1<Void, Observable<?>>() {
                            @Override
                            public Observable<?> call(Void aVoid) {
                                return Observable.empty();
                            }
                        });
                    }
                }).
                subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                });
    }

    @Test
    public void serverSocketTest() throws Exception {
        final ServerSocket server = new ServerSocket(1234);
        server.setSoTimeout(3*1000);
//        Observable.timer(2, TimeUnit.SECONDS).
//                subscribeOn(Schedulers.newThread()).
//                observeOn(Schedulers.immediate()).
//                subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        try {
//                            server.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
        server.accept();
        System.out.println(server.isClosed());
    }
}
