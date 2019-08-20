package com.vcvinci.common.record;

import com.vcvinci.common.exception.ThrowUtils;
import com.vcvinci.common.exception.server.storage.InvalidBatchException;

import java.util.Iterator;

/**
 * @author yankai.zhang
 * @since 2019/1/24 下午4:51
 */
public abstract class RecordBatchIterator<T extends AbstractRecordBatch> implements Iterator<T> {
    private T next = null;

    protected abstract T tryMakeNext() throws InvalidBatchException;

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasNext() {
        if (this.next != null) {
            return true;
        }
        try {
            this.next = this.tryMakeNext();
            return this.next != null;
        } catch (InvalidBatchException e) {
            return false;
        }
    }

    @Override
    public T next() {
        T next = this.next;
        if (next != null) {
            try {
                this.advance(next.getHeader().sizeInBytes());
            } catch (InvalidBatchException e) {
                // 不太可能发生
                throw ThrowUtils.eraseChecked(e);
            }
            this.next = null;
        }
        return next;
    }

    protected abstract void advance(int size);
}
