package com.dd.rxjava;

import java.util.HashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * courtesy: https://gist.github.com/benjchristensen/04eef9ca0851f3a5d7bf
 */
public class RxBus {
    private static HashMap<String, RxBus> mRxBus = new HashMap<>();

    public static RxBus register(String tag) {
        RxBus instance = new RxBus();
        mRxBus.put(tag, instance);
        return instance;
    }

    public static RxBus getRxBus(String tag) {
        return mRxBus.get(tag);
    }

    public static void unRegister(String tag) {
        mRxBus.remove(tag);
    }

    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }

}