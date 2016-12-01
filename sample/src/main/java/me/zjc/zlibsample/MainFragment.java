package me.zjc.zlibsample;

import android.content.Intent;
import android.view.View;

import me.zjc.zlib.base.BaseFragment;
import me.zjc.zlibsample.network.NetworkActivity;

/**
 * Created by ChuanZhangjiang on 2016/11/20.
 *
 */

public class MainFragment extends BaseFragment {

    @Override
    protected void initView(View contentView) {
        initNetworkButton(contentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    private void initNetworkButton(View contentView) {
        contentView.
                findViewById(R.id.btn_network).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goNetworkActivity();
                    }
                });
    }

    private void goNetworkActivity() {
        Intent intent = new Intent(getActivity(), NetworkActivity.class);
        startActivity(intent);
    }
}