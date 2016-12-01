package me.zjc.zlibsample;

import me.zjc.zlib.base.BaseFragment;

public class MainActivity extends AppActivity {

    private BaseFragment mMainFragment;

    @Override
    protected BaseFragment getFirstFragment() {
        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
        }
        return mMainFragment;
    }
}
