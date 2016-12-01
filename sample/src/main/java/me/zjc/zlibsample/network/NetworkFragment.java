package me.zjc.zlibsample.network;

import android.view.View;

import me.zjc.zlib.base.BaseFragment;
import me.zjc.zlib.common.rxbus.Rxbus;
import me.zjc.zlibsample.R;

/**
 * Created by ChuanZhangjiang on 2016/11/20.
 *
 */

public final class NetworkFragment extends BaseFragment {

    @Override
    protected void initView(View contentView) {
        initDownloadButton(contentView);
        initSocketButton(contentView);
    }

    private void initDownloadButton(View contentView) {
        contentView.findViewById(R.id.btn_download_sample).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Rxbus.INSTANCE.post(Event.DownLoadBtnClick);
                    }
                });
    }

    private void initSocketButton(View contentView) {
        contentView.findViewById(R.id.btn_socket_sample).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Rxbus.INSTANCE.post(Event.SocketBtnClick);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_network;
    }

    public enum Event{
        DownLoadBtnClick,
        SocketBtnClick
    }

}
