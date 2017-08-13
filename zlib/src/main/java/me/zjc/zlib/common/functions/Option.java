package me.zjc.zlib.common.functions;

/**
 * Created by ChuanZhangjiang on 2016/12/21.
 * Option类，用来处理有的时候会出现空值的情况
 * 例如:
 *      作为Map中的值, 当某些key要对应null值的时候用Nothing替代,
 *      可以区分没有该key和该key对应null的情况
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Option<V> {

    public static <VV> Option<VV> some(VV value) {
        return new Some<>(value);
    }

    public static Option<?> nothing() {
        return new Nothing();
    }

    public abstract V get();
    public abstract V getOrElse(V elseValue);
    public abstract boolean isNothing();

    public static class Nothing<VV> extends Option<VV> {

        @Override
        public VV get() {
            throw new AssertionError("noting not support method #get()");
        }

        @Override
        public VV getOrElse(VV elseValue) {
            return elseValue;
        }

        @Override
        public boolean isNothing() {
            return true;
        }
    }

    public static class Some<VV> extends Option<VV> {
        private final VV mValue;
        private Some(VV value) {
            mValue = value;
        }

        @Override
        public VV get() {
            return mValue;
        }

        @Override
        public VV getOrElse(VV elseValue) {
            return mValue;
        }

        @Override
        public boolean isNothing() {
            return false;
        }
    }
}
