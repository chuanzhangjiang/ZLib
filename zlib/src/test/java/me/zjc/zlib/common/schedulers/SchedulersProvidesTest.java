package me.zjc.zlib.common.schedulers;

import org.junit.Test;

import me.zjc.zlib.common.schedulers.SchedulersProvide;
import me.zjc.zlib.common.schedulers.SchedulersProvides;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Created by ChuanZhangjiang on 2016/11/15.
 * SchedulersProvides测试类
 */
public class SchedulersProvidesTest {
    private final SchedulersProvide normal = SchedulersProvides.getNormalSchedulerProvide();
    private final SchedulersProvide immediate = SchedulersProvides.getImmediateSchedulersProvide();

    @Test
    public void testNormalSP() throws Exception {
        assertEquals(normal.computation(), Schedulers.computation());
        assertEquals(normal.io(), Schedulers.io());
    }

    @Test
    public void testImmediateSP() throws Exception {
        assertEquals(immediate.ui(), Schedulers.immediate());
        assertEquals(immediate.io(), Schedulers.immediate());
        assertEquals(immediate.computation(), Schedulers.immediate());
    }
}