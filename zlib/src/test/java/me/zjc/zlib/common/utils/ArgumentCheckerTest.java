package me.zjc.zlib.common.utils;

import static org.junit.Assert.*;
import org.junit.Test;

import me.zjc.zlib.common.utils.ArgumentChecker;

/**
 * Created by ChuanZhangjiang on 2016/8/5.
 * 参数校验工具类单元测试
 */
public class ArgumentCheckerTest {

    @Test
    public void testCheckNotNullNoMsg() throws Exception {
        try {
            ArgumentChecker.checkNotNull(null);
            fail();
        } catch (Exception e) {
            assertTrue(e.getClass() == NullPointerException.class);
        }

        try {
            ArgumentChecker.checkNotNull(new Object());
        }catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testCheckNotNullHasMsg() throws Exception {
        String msg = "msg";
        try {
            ArgumentChecker.checkNotNull(null, msg);
        } catch (Exception e) {
            assertTrue(e.getClass() == NullPointerException.class);
            assertEquals(e.getMessage(), msg);
        }

        try {
            ArgumentChecker.checkNotNull(new Object(), msg);
        } catch (Exception e) {
            fail();
        }
    }
}
