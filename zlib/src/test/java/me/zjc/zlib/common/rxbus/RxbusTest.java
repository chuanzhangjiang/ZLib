package me.zjc.zlib.common.rxbus;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;

import me.zjc.zlib.common.rxbus.Rxbus;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by ChuanZhangjiang on 2016/8/5.
 * rxbus单元测试类
 */
public class RxbusTest {

    @Mock
    private Action1<String> stringSubscriber;
    @Mock
    private Action1<Integer> integerSubscriber;
    @Mock
    private Action1<SomeEvent> someEventSubscriber;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestNoCloneSubscribe() throws Exception {
        SomeEvent event01 = new SomeEvent("event01", 1);
        //订阅
        Rxbus.INSTANCE.toObservable(SomeEvent.class)
                .subscribe(someEventSubscriber);
        //发送
        Rxbus.INSTANCE.post(event01);
        //验证
        ArgumentCaptor<SomeEvent> someArgumentCaptor = ArgumentCaptor.forClass(SomeEvent.class);
        verify(someEventSubscriber, times(1)).call(someArgumentCaptor.capture());
        assertTrue(event01 == someArgumentCaptor.getValue());

    }

    @Test
    public void TestCloneSubscribe() throws Exception {
        SomeEvent event = new SomeEvent("event", 1);
        //订阅
        Rxbus.INSTANCE.toObservable(SomeEvent.class)
                .subscribe(someEventSubscriber);

        //发送
        Rxbus.INSTANCE.post(event, new Func1<SomeEvent, SomeEvent>() {
            @Override
            public SomeEvent call(SomeEvent obj) {
                return new SomeEvent(obj.msg, obj.code);
            }
        });

        //验证
        ArgumentCaptor<SomeEvent> captor = ArgumentCaptor.forClass(SomeEvent.class);
        verify(someEventSubscriber, times(1)).call(captor.capture());
        assertNotNull(captor.getValue());
        assertEquals(event, captor.getValue());
        assertFalse(event == captor.getValue());
    }

    /**
     * 测试一个事件，两个接收者
     * @throws Exception
     */
    @Test
    public void testOneEventTowReceiver() throws Exception{
        SomeEvent event = new SomeEvent("event", 1);

        @SuppressWarnings("unchecked")
        Action1<SomeEvent> someEventSubscriber02 = mock(Action1.class);
        //订阅
        Rxbus.INSTANCE.toObservable(SomeEvent.class)
                .subscribe(someEventSubscriber);
        Rxbus.INSTANCE.toObservable(SomeEvent.class)
                .subscribe(someEventSubscriber02);

        //发送
        Rxbus.INSTANCE.post(event);

        //验证
        ArgumentCaptor<SomeEvent> captor = ArgumentCaptor.forClass(SomeEvent.class);
        verify(someEventSubscriber, times(1)).call(captor.capture());
        verify(someEventSubscriber02, times(1)).call(captor.capture());
    }

    private static class SomeEvent{
        final String msg;
        final int code;
        SomeEvent(String msg, int code) {
            this.msg = msg;
            this.code = code;
        }

        @Override
        public String toString() {
            return "SomeEvent{" +
                    "msg='" + msg + '\'' +
                    ", code=" + code +
                    '}';
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;

            SomeEvent event = (SomeEvent) object;

            return code == event.code && msg.equals(event.msg);

        }

        @Override
        public int hashCode() {
            int result = msg.hashCode();
            result = 31 * result + code;
            return result;
        }
    }

}
