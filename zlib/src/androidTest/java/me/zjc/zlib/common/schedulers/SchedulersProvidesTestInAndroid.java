package me.zjc.zlib.common.schedulers;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import rx.android.schedulers.AndroidSchedulers;


/**
 * Created by ChuanZhangjiang on 2016/11/15.
 *
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SchedulersProvidesTestInAndroid extends TestCase {
    private final SchedulersProvide normal = SchedulersProvides.getNormalSchedulerProvide();

    @Test
    public void testNormalSP() throws Exception {
        assertEquals(normal.ui(), AndroidSchedulers.mainThread());
    }
}