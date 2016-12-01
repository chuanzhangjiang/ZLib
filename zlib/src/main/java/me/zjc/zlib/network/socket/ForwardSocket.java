package me.zjc.zlib.network.socket;


import java.io.IOException;

import rx.functions.Action1;


class ForwardSocket extends AbstractSocketClient {

    private final ISocketClient client;

    ForwardSocket(ISocketClient client) {
        this.client = client;
    }

    @Override
    public void send(String msg) {
        client.send(msg);
    }

    @Override
    public void accept(Action1<String> action1) {
        client.accept(action1);
    }

    @Override
    public boolean hasClosed() {
        return client.hasClosed();
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
