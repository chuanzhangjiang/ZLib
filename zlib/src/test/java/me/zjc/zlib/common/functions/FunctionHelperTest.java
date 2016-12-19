package me.zjc.zlib.common.functions;

import org.junit.Test;

import me.zjc.zlib.common.schedulers.SchedulersProvides;

import static org.junit.Assert.*;

/**
 * Created by ChuanZhangjiang on 2016/12/18.
 *
 */
public class FunctionHelperTest {

    @Test
    public void safeInvokeAction0InUiThread() throws Exception {
        FunctionHelper.safeInvokeAction0InUiThread(
                ()-> assertEquals(Thread.currentThread().getName(), "main"),
                SchedulersProvides.getImmediateSchedulersProvide()
        );
    }
}