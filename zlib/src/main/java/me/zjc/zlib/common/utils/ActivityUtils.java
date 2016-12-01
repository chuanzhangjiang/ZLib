package me.zjc.zlib.common.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import me.zjc.zlib.base.BaseFragment;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static me.zjc.zlib.common.utils.ArgumentChecker.checkNotNull;

/**
 * Created by ChuanZhangjiang on 2016/11/20.
 *
 */

public final class ActivityUtils {
    private ActivityUtils(){}

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity (@NonNull final FragmentManager fragmentManager,
                                              @NonNull final BaseFragment fragment, final int frameId) {
        ArgumentChecker.checkNotNull(fragment);
        dealFragmentTransaction(fragmentManager, new Action1<FragmentTransaction>() {
            @Override
            public void call(FragmentTransaction fragmentTransaction) {
                fragmentTransaction.add(frameId, fragment, fragment.getFragmentTag());
            }
        });
    }

    /**
     * 判断Fragment是否已经在FragmentManager中
     */
    public static boolean fragmentIsInManager(FragmentManager manager, BaseFragment fragment) {
        ArgumentChecker.checkNotNull(manager);
        if (fragment == null) {
            return false;
        }
        return manager.findFragmentByTag(fragment.getFragmentTag()) != null;
    }

    /**
     * 隐藏除传入Fragment外的其他Fragment
     */
    public static void hideOtherFragments(final FragmentManager manager, final BaseFragment fragment) {
        dealFragmentTransaction(manager, new Action1<FragmentTransaction>() {
            @Override
            public void call(final FragmentTransaction fragmentTransaction) {
                getOtherFragments(manager, fragment).subscribe(new Action1<BaseFragment>() {
                    @Override
                    public void call(BaseFragment fragment) {
                        fragmentTransaction.hide(fragment);
                    }
                });
            }
        });
    }

    /**
     * 获取除传入Fragment外的其他Fragment
     */
    public static Observable<BaseFragment> getOtherFragments(FragmentManager manager,
                                                             final BaseFragment fragment) {
        ArgumentChecker.checkNotNull(manager);
        ArgumentChecker.checkNotNull(fragment);
        return Observable.from(manager.getFragments()).
                cast(BaseFragment.class).
                filter(new Func1<BaseFragment, Boolean>() {
                    @Override
                    public Boolean call(BaseFragment each) {
                        return !fragment.getFragmentTag().equals(each.getFragmentTag());
                    }
                });
    }

    /**
     * 替换当前容器中的Fragment
     */
    public static void replaceFragment(@NonNull final FragmentManager fragmentManager,
                                       @NonNull final BaseFragment fragment, final int frameId) {
        ArgumentChecker.checkNotNull(fragment);
        dealFragmentTransaction(fragmentManager, new Action1<FragmentTransaction>() {
            @Override
            public void call(FragmentTransaction fragmentTransaction) {
                fragmentTransaction.replace(frameId, fragment, fragment.getFragmentTag());
            }
        });
    }

    /**
     * 处理FragmentTransaction
     * 不用自己手动调用{@link FragmentManager#beginTransaction()}
     * 和 {@link FragmentTransaction#commit()}
     * @param transactionDealer 事物处理者
     */
    public static void dealFragmentTransaction(@NonNull FragmentManager fragmentManager,
                                               @NonNull Action1<FragmentTransaction> transactionDealer) {
        ArgumentChecker.checkNotNull(fragmentManager);
        ArgumentChecker.checkNotNull(transactionDealer);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transactionDealer.call(transaction);
        transaction.commit();
    }
}
