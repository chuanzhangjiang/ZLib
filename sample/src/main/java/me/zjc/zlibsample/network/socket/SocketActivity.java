package me.zjc.zlibsample.network.socket;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import me.zjc.zlib.base.BaseFragment;
import me.zjc.zlib.common.rxbus.Rxbus;
import me.zjc.zlibsample.AppActivity;
import rx.Subscription;
import rx.functions.Action1;

public class SocketActivity extends AppActivity {

    private boolean serverIsStart = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listenerSocketFragment();
        listenerSocketServer();
    }

    private void listenerSocketFragment() {
        Subscription subscription =
                Rxbus.INSTANCE.toObservable(SocketFragment.Event.class)
                .subscribe(new Action1<SocketFragment.Event>() {
                    @Override
                    public void call(SocketFragment.Event event) {
                        if (event == SocketFragment.Event.START_SOCKET_SERVER)
                            if (!serverIsStart)
                                startSocketService();
                    }
                });
        manageSubscription(subscription);
    }

    private void listenerSocketServer() {
        Subscription subscription = Rxbus.INSTANCE.toObservable(SocketServerService.Event.class)
                .subscribe(new Action1<SocketServerService.Event>() {
                    @Override
                    public void call(SocketServerService.Event event) {
                        if (event == SocketServerService.Event.SERVER_CLOSE)
                            serverIsStart = false;
                        else if (event == SocketServerService.Event.SERVER_START)
                            serverIsStart = true;
                    }
                });
        manageSubscription(subscription);
    }

    private void startSocketService() {
        Intent intent = new Intent(this, SocketServerService.class);
        startService(intent);
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return SocketFragment.newInstance();
    }
}
