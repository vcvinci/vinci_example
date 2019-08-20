/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vcvinci.common.record;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于缓存字节缓冲区的简单非线程安全接口。这适用于简单的情况，例如确保给定的Consumer在迭代读取的记录时重用相同的解压缩缓冲区。
 * 对于小记录*批处理，分配一个可能很大的缓冲区（LZ4为64 KB）将主导解压缩和迭代批处理中的记录的成本。
 */
public abstract class BufferSupplier implements AutoCloseable {

    public static final BufferSupplier NO_CACHING = new BufferSupplier() {
        @Override
        public ByteBuffer get(int capacity) {
            return ByteBuffer.allocate(capacity);
        }

        @Override
        public void release(ByteBuffer buffer) {}

        @Override
        public void close() {}
    };

    public static BufferSupplier create() {
        return new DefaultSupplier();
    }

    /**
     * 提供具有所需容量的缓冲区。这可能会返回缓存的缓冲区或分配新的实例。
     */
    public abstract ByteBuffer get(int capacity);

    /**
     * 返回提供的缓冲区，以便随后调用`get`重用。
     */
    public abstract void release(ByteBuffer buffer);

    /**
     * 释放与此供应商关联的所有资源。
     */
    @Override
    public abstract void close();

    private static class DefaultSupplier extends BufferSupplier {
        // 我们目前使用单个块大小，因此针对该情况进行优化
        /**
         * bufferMap 缓存
         */
        private final Map<Integer, Deque<ByteBuffer>> bufferMap = new HashMap<>(1);

        @Override
        public ByteBuffer get(int size) {
            Deque<ByteBuffer> bufferQueue = bufferMap.get(size);
            if (bufferQueue == null || bufferQueue.isEmpty()) {
                return ByteBuffer.allocate(size);
            } else {
                return bufferQueue.pollFirst();
            }
        }

        @Override
        public void release(ByteBuffer buffer) {
            buffer.clear();
            Deque<ByteBuffer> bufferQueue = bufferMap.get(buffer.capacity());
            if (bufferQueue == null) {
                // 我们目前在运行中保留一个缓冲区，因此针对该情况进行优化
                bufferQueue = new ArrayDeque<>(1);
                bufferMap.put(buffer.capacity(), bufferQueue);
            }
            bufferQueue.addLast(buffer);
        }

        @Override
        public void close() {
            bufferMap.clear();
        }
    }
}
