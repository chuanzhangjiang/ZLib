package me.zjc.zlib.common.functions.either;

/**
 * Created by ChuanZhangjiang on 2016/12/20.
 * 非左即右
 * 用于处理异常
 * 规定左值为异常
 * 例如：
 *     有些方法可能会抛出异常，就让该方法返回Either类,
 *     免去该方法外面的try...catch语句
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Either<TL, TR> {

    public static <L> Either<L, ?> left(L lVal){
        return new Left<>(lVal);
    }

    public static <R> Either<?, R> right(R rVal) {
        return new Right<>(rVal);
    }

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract TL left();

    public abstract TR right();

}
