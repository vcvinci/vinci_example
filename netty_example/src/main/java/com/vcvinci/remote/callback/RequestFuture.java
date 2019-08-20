package com.vcvinci.remote.callback;

import com.vcvinci.common.Condition;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 请求Future
 * @author yupeng.sun E-mail:yupeng.sun@ucarinc.com
 * @version 创建时间：2018年5月7日 上午9:33:36
 */
public class RequestFuture<T> implements Condition {

    private static final Object EMPTY_RESULT = new Object();
    private final AtomicReference<Object> result = new AtomicReference<>(EMPTY_RESULT);
    private final ConcurrentLinkedQueue<RequestFutureListener<T>> listeners = new ConcurrentLinkedQueue<>();
    private final CountDownLatch completedLatch = new CountDownLatch(1);

    public boolean isDone() {
        return result.get() != EMPTY_RESULT;
    }

    public boolean awaitDone(long timeout, TimeUnit unit) throws InterruptedException {
        return completedLatch.await(timeout, unit);
    }

    @SuppressWarnings("unchecked")
    public T value() {
        if (!isSucceeded()) {
            throw new IllegalStateException(
                    "Attempt to retrieve value from future which hasn't successfully completed");
        }
        return (T) result.get();
    }

    public boolean isSucceeded() {
        return isDone() && !isFailed();
    }

    public boolean isFailed() {
        return result.get() instanceof RuntimeException;
    }

    public RuntimeException exception() {
        if (!isFailed()) {
            throw new IllegalStateException("Attempt to retrieve exception from future which hasn't failed");
        }
        return (RuntimeException) result.get();
    }

    public void completeAndSetResult(T value) {
        try {
            if (value instanceof RuntimeException) {
                throw new IllegalArgumentException(
                        "The argument to complete can not be an instance of RuntimeException");
            }

            if (!result.compareAndSet(EMPTY_RESULT, value)) {
                throw new IllegalStateException(
                        "Invalid attempt to complete a request future which is already complete");
            }
            fireSuccess();
        } finally {
            completedLatch.countDown();
        }
    }

    public void completeAndSetException(RuntimeException e) {
        try {
            if (e == null) {
                throw new IllegalArgumentException("The exception passed to raise must not be null");
            }

            if (!result.compareAndSet(EMPTY_RESULT, e)) {
                throw new IllegalStateException(
                        "Invalid attempt to complete a request future which is already complete");
            }

            fireFailure();
        } finally {
            completedLatch.countDown();
        }
    }

    // public void raise(Errors error) {
    // raise(error.exception());
    // }

    private void fireSuccess() {
        T value = value();
        while (true) {
            RequestFutureListener<T> listener = listeners.poll();
            if (listener == null) {
                break;
            }
            listener.onSuccess(value);
        }
    }

    private void fireFailure() {
        RuntimeException exception = exception();
        while (true) {
            RequestFutureListener<T> listener = listeners.poll();
            if (listener == null) {
                break;
            }
            listener.onFailure(exception);
        }
    }

    public void addListener(RequestFutureListener<T> listener) {
        this.listeners.add(listener);
        if (isFailed()) {
            fireFailure();
        } else if (isSucceeded()) {
            fireSuccess();
        }
    }

    public static <T> RequestFuture<T> failure(RuntimeException e) {
        RequestFuture<T> future = new RequestFuture<>();
        future.completeAndSetException(e);
        return future;
    }

    @Override
    public boolean shouldBlock() {
        return !isDone();
    }
}