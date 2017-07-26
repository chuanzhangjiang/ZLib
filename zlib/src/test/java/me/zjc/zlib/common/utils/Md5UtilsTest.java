package me.zjc.zlib.common.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by ChuanZhangjiang on 2016/11/16.
 * Md5Utils测试类{@link Md5Utils}
 */
public class Md5UtilsTest {
    @Test
    public void getFileMd5String() throws Exception {
        final String exceptMD5 = "AAA23AFEC751E0CFEF1B47C910BB0FDC";
        final String actMD5 = Md5Utils.getFileMd5String(new File("zlib/testFile/src.svg"));
        assertEquals(exceptMD5, actMD5);
    }

    @Test
    public void getStringMD5String() throws Exception {
        final String exceptMD5 = "A4ED45037B63A547BED80F8749C28084";
        final String actMD5 = Md5Utils.getStringMD5String("123abc!!!");
        assertEquals(exceptMD5, actMD5);
    }

}