package me.zjc.zlibsample.network;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import me.zjc.zlib.base.BaseFragment;
import me.zjc.zlib.common.rxbus.Rxbus;
import me.zjc.zlib.common.schedulers.SchedulersProvides;
import me.zjc.zlib.common.utils.ActivityUtils;
import me.zjc.zlibsample.AppActivity;
import me.zjc.zlibsample.network.download.DownloadFragment;
import me.zjc.zlibsample.network.download.DownloadSampleContract;
import me.zjc.zlibsample.network.download.Presenter;
import me.zjc.zlibsample.network.socket.SocketActivity;
import rx.Subscription;
import rx.functions.Action1;

public class NetworkActivity extends AppActivity {

    private BaseFragment mNetworkFragment;
    private BaseFragment mDownloadFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButtonInToolbar();
        listenerFragmentEvent();
    }

    private void listenerFragmentEvent() {
        listenerNetworkFragmentEvent();
    }

    private void listenerNetworkFragmentEvent() {
        Subscription subscription = Rxbus.INSTANCE.toObservable(NetworkFragment.Event.class).
                subscribe(new Action1<NetworkFragment.Event>() {
                    @Override
                    public void call(NetworkFragment.Event event) {
                        if (event == NetworkFragment.Event.DownLoadBtnClick) {
                            showDownloadFragment();
                        } else if (event == NetworkFragment.Event.SocketBtnClick) {
                            startSocketActivity();
                        }
                    }
                });
        manageSubscription(subscription);
    }

    private void showDownloadFragment() {
        ActivityUtils.dealFragmentTransaction(getSupportFragmentManager(), new Action1<FragmentTransaction>() {
            @Override
            public void call(FragmentTransaction fragmentTransaction) {
                fragmentTransaction.
                        replace(getFragmentContainerId(), getDownloadFragment(),
                                getDownloadFragment().getFragmentTag()).
                        addToBackStack(null);
            }
        });
    }

    private BaseFragment getDownloadFragment() {
        if (mDownloadFragment == null) {
            mDownloadFragment = new DownloadFragment();
            Presenter.newInstance((DownloadSampleContract.View) mDownloadFragment,
                    SchedulersProvides.getNormalSchedulerProvide());
        }
        return mDownloadFragment;
    }

    private void startSocketActivity() {
        Intent intent = new Intent(this, SocketActivity.class);
        startActivity(intent);
    }

    @Override
    protected BaseFragment getFirstFragment() {
        if (mNetworkFragment == null) {
            mNetworkFragment = new NetworkFragment();
        }
        return mNetworkFragment;
    }
}
