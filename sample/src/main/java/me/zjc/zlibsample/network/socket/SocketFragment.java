package me.zjc.zlibsample.network.socket;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import me.zjc.zlib.base.BaseFragment;
import me.zjc.zlib.common.rxbus.Rxbus;
import me.zjc.zlib.network.socket.ISocketClient;
import me.zjc.zlib.network.socket.SocketFactory;
import me.zjc.zlibsample.R;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ChuanZhangjiang on 2016/11/27.
 *
 */

public final class SocketFragment extends BaseFragment {

    private EditText mReceivedMsgEt;
    private EditText mInputMsgEt;
    private Button mToggleServerBtn;//管理服务的开启和关闭
    private Button mSendBtn;

    private boolean serverIsStart = false;

    private ISocketClient mClient;

    public static BaseFragment newInstance() {
        return new SocketFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mClient == null)
            listenerSocketServer();
    }

    private void listenerSocketServer() {
        Subscription subscription =
                Rxbus.INSTANCE.toObservable(SocketServerService.Event.class).
                        subscribe(event -> {
                            if (event == SocketServerService.Event.SERVER_CLOSE) {
                                releaseConnect();
                                setServerCloseStatus();
                            } else if (event == SocketServerService.Event.SERVER_START) {
                                connectServer();
                            }
                        });
        manageSubscription(subscription);
    }

    private void connectServer() {
        Subscription subscription = SocketFactory.getClientObservable(SocketServerService.SERVER_HOST,
                SocketServerService.SERVER_PORT).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(client -> {
                    mClient = client.asyncLoopAccept().doAcceptActionOnMainThread();
                    mClient.accept(s -> mReceivedMsgEt.append(s + "\n"));
                    setServerOpenStatus();
                }, mErrorAction);
        manageSubscription(subscription);
    }

    private void releaseConnect() {
        try {
            if (mClient != null)
                mClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mClient = null;
        }
    }

    private void setServerCloseStatus() {
        serverIsStart = false;
        mToggleServerBtn.setEnabled(true);
        mToggleServerBtn.setText(getResources().getText(R.string.network_socket_btn_text_open_server));
        mSendBtn.setEnabled(false);
    }

    private void setTransitionStatus() {
        mToggleServerBtn.setEnabled(false);
        mSendBtn.setEnabled(false);
    }

    private void setServerOpenStatus() {
        serverIsStart = true;
        mToggleServerBtn.setEnabled(true);
        mToggleServerBtn.setText(getResources().getText(R.string.network_socket_btn_text_close_server));
        mSendBtn.setEnabled(true);
    }

    @Override
    protected void initView(View contentView) {
        mReceivedMsgEt = (EditText) contentView.findViewById(R.id.et_received_msg);
        mInputMsgEt = (EditText) contentView.findViewById(R.id.et_input_msg);
        mToggleServerBtn = (Button) contentView.findViewById(R.id.btn_toggle_socket_server);
        mSendBtn = (Button) contentView.findViewById(R.id.btn_send);

        setServerCloseStatus();

        listenerButton();
    }

    private void listenerButton() {
        mToggleServerBtn.setOnClickListener(v -> {
            if (serverIsStart) {
                closeServer();
            } else {
                startServer();
            }
            setTransitionStatus();
        });

        mSendBtn.setOnClickListener(v -> {
            if (mClient != null) {
                mClient.send(mInputMsgEt.getText().toString());
            }
            mInputMsgEt.setText("");
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_socket;
    }

    @Override
    public void onDestroy() {
        releaseConnect();
        closeServer();
        super.onDestroy();
    }

    private void closeServer() {
        if (!serverIsStart)
            return;
        if (mClient != null && !mClient.hasClosed()) {
            mClient.send("stop");
        }
        Log.e(getFragmentTag(), "closeServer");
    }

    private void startServer() {
        Rxbus.INSTANCE.post(Event.START_SOCKET_SERVER);
    }

    private final Action1<Throwable> mErrorAction = Throwable::printStackTrace;

    public enum Event {
        START_SOCKET_SERVER
    }
}
