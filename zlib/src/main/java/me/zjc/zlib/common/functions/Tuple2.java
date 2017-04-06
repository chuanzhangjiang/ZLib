package me.zjc.zlib.common.functions;

/**
 * Created by ChuanZhangjiang on 2016/12/20.
 * 二维元组
 * 用于处理，某些方法需要返回多个值，但是这些值又不太容易直观的组件成一个类的情况
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Tuple2<T1, T2> {
    private final T1 pair_1;
    private final T2 pair_2;

    public Tuple2(T1 pair_1, T2 pair_2) {
        this.pair_1 = pair_1;
        this.pair_2 = pair_2;
    }

    /**
     * 获取元组的第一个元素
     */
    public T1 pair_1(){
        return pair_1;
    }

    /**
     * 获取元组的第二个元素
     */
    public T2 pair_2() {
        return pair_2;
    }

    @Override
    public String toString() {
        return "< " + pair_1 + ", " + pair_2 + " >";
    }
}
