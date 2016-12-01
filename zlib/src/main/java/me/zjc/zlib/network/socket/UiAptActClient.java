package me.zjc.zlib.network.socket;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import rx.functions.Action1;

final class UiAptActClient extends ForwardSocket {

    private UiThreadAction1<String> uiThreadAction1;

    UiAptActClient(ISocketClient client) {
        super(client);
    }

    @Override
    public void accept(Action1<String> action1) {
        uiThreadAction1 = new UiThreadAction1<>(action1);
        super.accept(uiThreadAction1);
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (uiThreadAction1 != null)
            uiThreadAction1.removeAction();
    }

    private final static class UiThreadAction1<T> implements Action1<T> {

        private final Action1<T> action1;
        private final Handler handler;

        UiThreadAction1(Action1<T> action1) {
            this.action1 = action1;
            this.handler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void call(final T x) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    action1.call(x);
                }
            });
        }

        void removeAction() {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
