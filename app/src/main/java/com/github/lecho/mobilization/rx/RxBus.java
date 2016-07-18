package com.github.lecho.mobilization.rx;

import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Leszek on 18.07.2016.
 */
public class RxBus {

    private static final Subject<Object, Object> subject = new SerializedSubject<>(PublishSubject.create());


    public static <T> Subscription subscribe(final Class<T> eventClass, Action1<T> onNext) {
        return subject
                .filter(event -> event.getClass().equals(eventClass))
                .map(obj -> (T) obj)
                .subscribe(onNext);
    }

    public static void post(Object event) {
        subject.onNext(event);
    }
}
