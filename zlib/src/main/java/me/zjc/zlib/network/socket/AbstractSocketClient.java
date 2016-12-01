package me.zjc.zlib.network.socket;

abstract class AbstractSocketClient implements ISocketClient{
    @Override
    public ISocketClient asyncLoopAccept() {
        return SocketFactory.getAsyncAcceptClient(this);
    }

    @Override
    public ISocketClient doAcceptActionOnMainThread() {
        return SocketFactory.getUiThreadAcceptActionClient(this);
    }
}
