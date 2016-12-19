package me.zjc.zlib.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbruyelle.rxpermissions.RxPermissions;

import me.zjc.zlib.common.utils.DimenUtils;
import me.zjc.zlib.common.utils.ArgumentChecker;
import rx.Subscription;
import rx.internal.util.SubscriptionList;

/**
 * Created by ChuanZhangjiang on 2016/8/4.
 * Base class for Fragment in project
 */
public abstract class BaseFragment extends Fragment {

    private UiHelper mUiHelper = null;
    private SubscriptionList mSubscriptions;

    /**
     * 该方法调用了{@link #getLayoutId()}方法
     *
     * 想要对界面进行初始化请不要覆盖这个方法
     * 请覆盖{@link #onResume()}方法
     *
     * @return View 根据{@link #getLayoutId()}方法获取到的相应布局
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mUiHelper = UiHelper.newInstance(getActivity());
        final View contentView = inflater.inflate(getLayoutId(), null);
        initView(contentView);
        return contentView;
    }

    /**
     * 管理订阅事件
     * 当Activity销毁时，自动解除订阅事件
     */
    protected void manageSubscription(Subscription subscription) {
        if (mSubscriptions == null) {
            mSubscriptions = new SubscriptionList();
        }
        mSubscriptions.add(subscription);
    }

    /**
     * 初始化界面，在onCreateView中调用
     */
    protected abstract void initView(View contentView);

    /**
     * 获取Fragment的布局资源Id
     * 在{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}方法中调用
     * 错误的实现这个方法可能会导致{@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}运行失败
     * @return 布局文件Id
     */
    protected abstract @LayoutRes int getLayoutId();

    /**
     * 显示一个ProgressDialog,没有提示内容
     */
    @SuppressWarnings("unused")
    protected void showProgressDialog() {
        mUiHelper.showProgressDialog();
    }

    /**
     * 显示一个ProgressDialog
     * @param msg 提示信息
     */
    @SuppressWarnings("unused")
    protected void showProgressDialog(String msg) {
        mUiHelper.showProgressDialog(msg);
    }

    /**
     * 取消ProgressDialog的显示
     */
    @SuppressWarnings("unused")
    protected void dismissProgressDialog() {
        mUiHelper.dismissProgressDialog();
    }

    /**
     * 显示一个默认时长的Toast
     * 默认时常为{@link android.widget.Toast#LENGTH_SHORT}
     * @param msg 显示的消息.
     */
    @SuppressWarnings("unused")
    protected void showToast(String msg) {
        mUiHelper.showToast(msg);
    }

    /**
     * 显示一个自定义时常的Toast
     * @param msg 显示的消息
     * @param duration 显示的时长，显示时长可以为{@link android.widget.Toast#LENGTH_SHORT}
     *                 或是 {@link android.widget.Toast#LENGTH_LONG}
     */
    @SuppressWarnings("unused")
    protected void showToast(String msg, @BaseActivity.ToastDuration int duration) {
        mUiHelper.showToast(msg, duration);
    }

    /**
     * 显示一个自定义显示时长的Snackbar
     * @param msg 显示的消息
     * @param duration 显示的时长，可以为{@link android.support.design.widget.Snackbar#LENGTH_SHORT}
     *                 或是 {@link android.support.design.widget.Snackbar#LENGTH_LONG}
     *                 或是 {@link android.support.design.widget.Snackbar#LENGTH_INDEFINITE}
     */
    @SuppressWarnings("unused")
    protected void showSnackBar(String msg, @Snackbar.Duration int duration) {
        mUiHelper.showSnackBar(ArgumentChecker.checkNotNull(getView()), msg, duration);
    }

    /**
     * 显示一个默认时长的Snackbar
     * 默认时常为{@link android.support.design.widget.Snackbar#LENGTH_SHORT}
     * @param msg 显示的消息
     */
    protected void showSnackBar(String msg) {
        mUiHelper.showSnackBar(ArgumentChecker.checkNotNull(getView()), msg);
    }

    protected int dip2px(int dip) {
        return DimenUtils.dip2px(getContext(), dip);
    }

    @SuppressWarnings("unused")
    protected int px2dip(int px) {
        return DimenUtils.px2dip(getContext(), px);
    }

    @SuppressWarnings("unused")
    protected int sp2px(int sp) {
        return DimenUtils.sp2px(getContext(), sp);
    }

    @SuppressWarnings("unused")
    protected int px2sp(int px) {
        return DimenUtils.px2sp(getContext(), px);
    }

    /**
     * 获取这个Fragment的类名
     */
    public String getFragmentTag(){
        return this.getClass().getName();
    }

    /**
     * 获取一个用于申请权限的RxPermissions
     */
    protected RxPermissions rxPermissions() {
        return new RxPermissions(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscriptions != null)
            mSubscriptions.clear();
        if (mUiHelper != null)
            mUiHelper.onDestroy();
    }
}
