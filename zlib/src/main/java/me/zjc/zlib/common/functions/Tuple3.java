package me.zjc.zlib.common.functions;

/**
 * Created by ChuanZhangjiang on 2016/12/20.
 * 三元元组
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Tuple3<T1, T2, T3> {
    private final Tuple2<T1, T2> tuple2;
    private final T3 pair_3;

    public Tuple3(T1 pair_1, T2 pair_2, T3 pair_3) {
        tuple2 = new Tuple2<>(pair_1, pair_2);
        this.pair_3 = pair_3;
    }

    /**
     * 获取元组的第一个元素
     */
    public T1 pair_1(){
        return tuple2.pair_1();
    }

    /**
     * 获取元组的第二个元素
     */
    public T2 pair_2() {
        return tuple2.pair_2();
    }

    /**
     * 获取元组的第三个元素
     */
    public T3 pair_3() {
        return pair_3;
    }

    @Override
    public String toString() {
        return "< " + pair_1() + ", " + pair_2() + ", " + pair_3() + " >";
    }
}
