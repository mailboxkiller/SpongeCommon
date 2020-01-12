/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.event.tracking;

import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nullable;

public abstract class PooledPhaseState<C extends PhaseContext<C>> implements IPhaseState<C> {

    private static final ConcurrentHashMap<IPhaseState<?>, ArrayDeque<? extends PhaseContext<?>>> stateContextPool = new ConcurrentHashMap<>();
    private final ArrayDeque<C> contextPool;
    @Nullable private C cached;
    final ReentrantLock lock = new ReentrantLock();

    protected PooledPhaseState() {
        final ArrayDeque<C> pool = new ArrayDeque<>();
        PooledPhaseState.stateContextPool.put(this, pool);
        this.contextPool = pool;
    }

    @Override
    public final C createPhaseContext() {
        this.lock.lock();
        try {
            if (this.cached != null && !this.cached.isCompleted) {
                final C cached = this.cached;
                this.cached = null;
                return cached;
            }
            final C peek = this.contextPool.pollFirst();
            if (peek != null) {
                this.cached = peek;
                return peek;
            }
            this.cached = this.createNewContext();
            return this.cached;
        } finally {
            this.lock.unlock();
        }
    }

    final void releaseContextFromPool(final C context) {
        this.lock.lock();
        try {
            if (this.cached == context) {
                return;
            }
            if (this.cached == null) {
                // We can cache this context to recycle it if it's requested later.
                // If there's no requests and just pushing, then it can be pushed to the
                // deque.
                this.cached = context;
                return;
            }
            this.contextPool.push(context);
        } finally {
            this.lock.unlock();
        }
    }

    protected abstract C createNewContext();

}
