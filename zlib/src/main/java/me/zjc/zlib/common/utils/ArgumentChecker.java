package me.zjc.zlib.common.utils;

/**
 * Created by ChuanZhangjiang on 2016/8/5.
 * 参数校验工具
 * 用于检测方法传入参数是否有问题
 */
public final class ArgumentChecker {

    private ArgumentChecker() {
        throw new IllegalAccessError("Constructor can not be access");
    }

    /**
     * 校验参数是否为空
     * 为空则抛出空指针异常
     * @param object 被校验的参数
     * @param msg 抛出异常时的消息
     * @throws NullPointerException
     */
    public static <T> T checkNotNull(T object, String msg) {
        if (object == null) {
            throw new NullPointerException(msg);
        }
        return object;
    }

    /**
     * 校验参数是否为空
     * 为空时抛出异常
     * 异常时抛出的消息使用默认值
     * @param object 被校验的参数
     * @throws NullPointerException
     */
    public static <T> T checkNotNull(T object) {
        return checkNotNull(object, "");
    }

}
