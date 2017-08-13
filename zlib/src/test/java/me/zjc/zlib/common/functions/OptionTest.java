package me.zjc.zlib.common.functions;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ChuanZhangjiang on 2016/12/21.
 *
 */
public class OptionTest {
    private final String someValue = "hello";
    private final Option nothing = Option.nothing();
    private final Option<String> some = Option.some(someValue);
    @Test
    public void get() throws Exception {
        assertEquals(some.get(), someValue);
        try {
            nothing.get();
            fail();
        } catch (AssertionError error) {
            //do noting
        }
    }

    @Test
    public void getOrElseTest() throws Exception {
        assertEquals(some.getOrElse("nothing"), someValue);
        assertEquals(nothing.getOrElse("nothing"), "nothing");
    }

    @Test
    public void isNothing() throws Exception {
        assertTrue(nothing.isNothing());
        assertTrue(!some.isNothing());
    }

}