package me.zjc.zlib.common.functions;

/**
 * Created by ChuanZhangjiang on 2016/12/20.
 * 四元元组
 */

@SuppressWarnings("unused")
public final class Tuple4<T1, T2, T3, T4> {
    private final Tuple3<T1, T2, T3> tuple3;
    private final T4 pair_4;
    
    public Tuple4(T1 pair_1, T2 pair_2, T3 pair_3, T4 pair_4) {
        tuple3 = new Tuple3<>(pair_1, pair_2, pair_3);
        this.pair_4 = pair_4;
    }

    /**
     * 获取元组的第一个元素
     */
    @SuppressWarnings("WeakerAccess")
    public T1 pair_1(){
        return tuple3.pair_1();
    }

    /**
     * 获取元组的第二个元素
     */
    @SuppressWarnings("WeakerAccess")
    public T2 pair_2() {
        return tuple3.pair_2();
    }

    /**
     * 获取元组的第三个元素
     */
    @SuppressWarnings("WeakerAccess")
    public T3 pair_3() {
        return tuple3.pair_3();
    }

    /**
     * 获取元组的第四个元素
     */
    @SuppressWarnings("WeakerAccess")
    public T4 pair_4() {
        return pair_4;
    }

    @Override
    public String toString() {
        return String.format("< %s, %s, %s, %s >",
                pair_1(), pair_2(), pair_3(), pair_4());
    }
}
