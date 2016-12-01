package me.zjc.zlib.common.utils;

import android.support.annotation.Nullable;

/**
 * Created by ChuanZhangjiang on 2016/8/8.
 * 文本工具，拷贝android SDK中的TextUtils中的部分代码，方便测试
 */
public final class TextUtils {

    private TextUtils() {
        throw new IllegalAccessError();
    }

    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }
}
