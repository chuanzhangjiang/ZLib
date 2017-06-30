package me.zjc.zlib.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import rx.Subscription;
import rx.internal.util.SubscriptionList;

/**
 * Created by ChuanZhangjiang on 2016/8/4.
 * the base class for activity in project
 */
public abstract class BaseActivity extends AppCompatActivity {

    private UiHelper mUiHelper;
    private SubscriptionList mSubscriptions;

    /**
     * 覆盖了父类的onCreate()方法
     * 该方法调用了最先调用{@link #setContentView(int)}
     * 然后传入{@link #getContentViewId()}方法的返回值
     * 最后在判断{@link #getIntent()}的返回值不为空的情况下
     * 调用{@link #handlerIntent(Intent)}方法
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentViewId());

        if (mUiHelper == null) {
            mUiHelper = UiHelper.newInstance(this);
        }

        if (getIntent() != null) {
            handlerIntent(getIntent());
        }
    }

    abstract protected @LayoutRes int getContentViewId();

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
     * 当从上一个Activity有参数传递过来的时候，覆盖这个方法
     * @param intent 从上一个Activity传递过来的intent
     */
    protected void handlerIntent(@SuppressWarnings("UnusedParameters") Intent intent) {}

    /**
     * 显示一个没有内容的ProgressDialog
     */
    @SuppressWarnings("unused")
    protected void showProgressDialog() {
        mUiHelper.showProgressDialog();
    }

    /**
     * 显示一个指定内容的ProgressDialog
     * @param content 显示的内容
     */
    @SuppressWarnings("unused")
    protected void showProgressDialog(String content) {
        mUiHelper.showProgressDialog(content);
    }

    /**
     * 隐藏等待弹窗
     */
    @SuppressWarnings("unused")
    protected void dismissProgressDialog() {
        mUiHelper.dismissProgressDialog();
    }

    /**
     * 显示一个短时常的Toast
     * @param content Toast显示的内容
     */
    @SuppressWarnings("unused")
    protected void showToast(@NonNull String content) {
        mUiHelper.showToast(content);
    }

    /**
     * 用于限制Toast显示时长类型
     */
    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    @interface ToastDuration{}

    /**
     * 显示一个Toast
     * @param content Toast显示的内容
     * @param duration Toast显示的时常, 显示时长可以为{@link android.widget.Toast#LENGTH_SHORT}
     *                 或是 {@link android.widget.Toast#LENGTH_LONG}
     */
    @SuppressWarnings("unused")
    protected void showToast(@NonNull String content, @ToastDuration int duration) {
        if (duration != Toast.LENGTH_SHORT && duration != Toast.LENGTH_LONG)
            throw new IllegalArgumentException("Illegal duration, duration need be " +
                    "Toast.LENGTH_SHORT or Toast.LENGTH_LONG, the duration accept is " +
                    duration);

        mUiHelper.showToast(content, duration);
    }

    /**
     * 显示一个短时常的Snackbar
     * @param view 当前UI中的一个View
     * @param content 显示的内容
     */
    @SuppressWarnings("unused")
    protected void showSnackBar(@NonNull View view, @NonNull String content) {
        mUiHelper.showSnackBar(view, content);
    }

    /**
     * 显示一个Snackbar
     * @param view 当前UI中的一个View
     * @param content 显示的内容
     * @param duration 显示的时常, 可以为{@link android.support.design.widget.Snackbar#LENGTH_SHORT}
     *                 或是 {@link android.support.design.widget.Snackbar#LENGTH_LONG}
     *                 或是 {@link android.support.design.widget.Snackbar#LENGTH_INDEFINITE}
     */
    @SuppressWarnings("unused")
    protected void showSnackBar(@NonNull View view, @NonNull String content,
                                @Snackbar.Duration int duration) {
        if (duration != Snackbar.LENGTH_SHORT && duration != Snackbar.LENGTH_LONG
                && duration != Snackbar.LENGTH_INDEFINITE)
            throw new IllegalArgumentException("Illegal duration, duration need be " +
                    "Snackbar.LENGTH_SHORT(-1) or Snackbar.LENGTH_LONG(0) " +
                    "or Snackbar.LENGTH_INDEFINITE(-2), " +
                    "the duration accept is " + duration);

        mUiHelper.showSnackBar(view, content, duration);
    }

    /**
     * 获取一个Activity的简单标签
     * 不加包名的名字
     */
    @SuppressWarnings("unused")
    public String getSimpleTag() {
        return getClass().getSimpleName();
    }

    /**
     * 获取Activity的全名
     * 包名+类名
     */
    @SuppressWarnings("unused")
    public String getTag() {
        return getClass().getName();
    }

    @Override
    protected void onDestroy() {
        if (mUiHelper != null)
            mUiHelper.onDestroy();
        if (mSubscriptions != null)
            mSubscriptions.clear();
        super.onDestroy();
    }
}
