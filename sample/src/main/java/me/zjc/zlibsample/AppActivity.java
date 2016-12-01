package me.zjc.zlibsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import me.zjc.zlib.base.BaseActivity;
import me.zjc.zlib.base.BaseFragment;
import me.zjc.zlib.common.utils.ActivityUtils;

import static me.zjc.zlib.common.utils.ArgumentChecker.checkNotNull;

/**
 * Created by ChuanZhangjiang on 2016/11/20.
 *
 */

public abstract class AppActivity extends BaseActivity {

    private ViewGroup mFragmentContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (isNoFragmentInContainer())
            ActivityUtils.replaceFragment(getSupportFragmentManager(), getFirstFragment()
                    , getFragmentContainerId());
    }

    private boolean isNoFragmentInContainer() {
        return getSupportFragmentManager().findFragmentById(getFragmentContainerId()) == null;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_app;
    }

    /**
     * 获取Fragment容器Id
     */
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    /**
     * 获取Fragment容器
     */
    protected ViewGroup getFragmentContainer() {
        if (mFragmentContainer == null) {
            mFragmentContainer = (ViewGroup) findViewById(getFragmentContainerId());
        }
        return mFragmentContainer;
    }

    /**
     * 在Toolbar上显示后退按钮
     */
    protected void showBackButtonInToolbar() {
        checkNotNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * 获取进入界面时加载的第一个Fragment
     */
    abstract protected BaseFragment getFirstFragment();
}
