package me.zjc.zlib.mvp;


/**
 * Created by ChuanZhangjiang on 2016/11/15.
 * MVP架构中V的基础接口
 */

public interface BaseView<T extends BasePresenter> {
    /**
     * 将Presenter绑定到View中
     */
    void setPresenter(T presenter);
}
