package me.zjc.zlib.common.functions.either;

/**
 * 左值类
 */
@SuppressWarnings("WeakerAccess")
public class Left<LL> extends Either<LL, Object> {

    private LL mLValue;
    Left(LL lValue) {
        this.mLValue = lValue;
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public LL left() {
        return mLValue;
    }

    @Override
    public Object right() {
        throw new AssertionError("left not support method #right()");
    }

    @Override
    public String toString() {
        return String.format("Left(%s)", mLValue);
    }
}
