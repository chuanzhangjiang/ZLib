package me.zjc.zlib.common.rxbus;

import android.support.annotation.NonNull;

import me.zjc.zlib.common.utils.ArgumentChecker;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by ChuanZhangjiang on 2016/8/5.
 * 事件总线
 */
public enum Rxbus {
    INSTANCE;

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    /**
     * 向事件总线发送一条消息
     * 消息没有经过克隆处理
     * @param object 要发送的消息
     * @param <T> 要发送的消息类型
     */
    public <T> void post(@NonNull T object) {
        post(object, null);
    }

    /**
     * 向事件总线发送一条消息
     * @param object 要发送的消息
     * @param cloneFunction 克隆函数，用于克隆要发送的消息
     * @param <T> 要发送的消息类型
     */
    public <T> void post(@NonNull T object, Func1<T, T> cloneFunction) {
        ArgumentChecker.checkNotNull(object, "can not post null object");
        T event = object;
        if (cloneFunction != null) {
            event = cloneFunction.call(object);
            if (event == object || event == null)
                throw new IllegalArgumentException("CloneFunction implement is incorrect");
        }
        bus.onNext(event);
    }

    /**
     * 将事件总线转化为可被订阅的对象
     * 供其它对象订阅
     * @param clazz 类型令牌，用于确定订阅的类型
     * @param <T> 订阅的类型
     * @return 可被订阅的总线对象
     */
    public <T> Observable<T> toObservable(Class<T> clazz) {
        return bus.ofType(clazz);
    }

}
