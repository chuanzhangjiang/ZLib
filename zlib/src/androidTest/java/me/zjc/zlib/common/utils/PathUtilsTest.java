package me.zjc.zlib.common.utils;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.zjc.zlib.common.io.PathUtils;


/**
 * Created by ChuanZhangjiang on 2016/8/8.
 * PathUtils测试类
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class PathUtilsTest extends TestCase {
    @SuppressWarnings("unused")
    public static final String TAG = PathUtilsTest.class.getSimpleName();

    private static final String defaultDownloadPath = "/storage/emulated/0/Download";

    @Test
    public void testGetDefaultDownloadPath() throws Exception {
        assertEquals(defaultDownloadPath, PathUtils.getDefaultDownloadPath());
    }
}