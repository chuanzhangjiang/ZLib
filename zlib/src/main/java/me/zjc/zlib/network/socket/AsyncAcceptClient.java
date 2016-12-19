package me.zjc.zlib.network.socket;


import rx.functions.Action1;

final class AsyncAcceptClient extends ForwardSocket {

    AsyncAcceptClient(ISocketClient client) {
        super(client);
    }

    @Override
    public void accept(final Action1<String> action1) {
        new Thread(() -> {
            AsyncAcceptClient.super.accept(action1);
        }).start();
    }
}
