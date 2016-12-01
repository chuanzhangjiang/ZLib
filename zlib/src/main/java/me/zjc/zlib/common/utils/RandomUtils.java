package me.zjc.zlib.common.utils;

import java.util.UUID;

/**
 * Created by ChuanZhangjiang on 2016/11/21.
 *
 */

public final class RandomUtils {

    private RandomUtils(){}

    /**
     * 随机生成不重复的字符串Id
     */
    public static String generateRandomId() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }
}
