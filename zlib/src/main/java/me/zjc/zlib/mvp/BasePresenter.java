package me.zjc.zlib.mvp;

/**
 * Created by ChuanZhangjiang on 2016/11/15.
 * MVP架构中P的基础接口
 */

public interface BasePresenter {
    /**
     * 用于刚刚进入界面的时候获取数据
     * 在Fragment中的onResume()方法中调用
     */
    void subscribe();

    /**
     * 用于解除一些RxJava中的订阅资源
     * 在Fragment中的onDestroy()方法中调用
     */
    void unsubscribe();
}
