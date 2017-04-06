package me.zjc.zlib.common.functions;

import org.junit.Test;

import me.zjc.zlib.common.functions.either.Either;

import static org.junit.Assert.*;

/**
 * Created by ChuanZhangjiang on 2016/12/21.
 *
 */
public class EitherTest {
    @SuppressWarnings("ThrowableInstanceNeverThrown")
    private final Exception lValue = new Exception();
    private final String rightValue = "hello";
    private final Either<Exception, ?> left = Either.left(lValue);
    private final Either<?, String> right = Either.right(rightValue);

    @Test
    public void isLeft() throws Exception {
        assertTrue(left.isLeft());
        assertTrue(!right.isLeft());
    }

    @Test
    public void isRight() throws Exception {
        assertTrue(!left.isRight());
        assertTrue(right.isRight());
    }

    @Test
    public void left() throws Exception {
        assertEquals(left.left(), lValue);
        try {
            right.right();
            fail();
        } catch (AssertionError error) {
            //do nothing
        }
    }

    @Test
    public void right() throws Exception {
        assertEquals(right.right(), rightValue);
        try {
            left.right();
            fail();
        } catch (AssertionError error) {
            //do nothing
        }
    }

}