package me.zjc.zlib.common.functions.either;

/**
 * 右值类
 * @param <RR>
 */
@SuppressWarnings("WeakerAccess")
public class Right<RR> extends Either<Object, RR> {
    private final RR mRValue;
    Right(RR rValue) {
        this.mRValue = rValue;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public Object left() {
        throw new AssertionError("Right not support the method #left()");
    }

    @Override
    public RR right() {
        return mRValue;
    }

    @Override
    public String toString() {
        return String.format("Right(%s)", mRValue);
    }
}
