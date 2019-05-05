package com.unowmo.code.yafjl;

import java.util.Iterator;
import java.util.function.*;

/**
 * Core
 *
 * Keeping things lightweight. More to come soon...
 *
 */
public class Core {

    public static class Adapt<T> implements Iterator<T> {
        private final T [] array;
        private int index = 0;

        @Override
        public boolean hasNext() {
            return this.array != null && this.index < this.array.length;
        }

        @Override
        public T next() {
            if (this.array != null && this.index < this.array.length) {
                return this.array[this.index++];
            }

            return null;
        }

        private Adapt(final T... array) {
            this.array = array;
        }

    }

    /**
     * ...
     *
     * @param items
     * @param <T>
     *
     */
    public static <T> Iterator<T> adapt(final T... items) {
        return new Adapt<>(items);
    }

    private static class Derive<T, X> implements Iterator<X> {
        private final Function<T, X> transform;
        private final Iterator<T> source;

        @Override
        public boolean hasNext() {
            return this.source.hasNext();
        }

        @Override
        public X next() {
            if (this.transform != null) {
                return this.transform.apply(this.source.next());
            }

            return null;
        }

        private Derive(final Function<T, X> transform, final Iterator<T> source) {
            this.transform = transform;
            this.source = source;
        }

    }

    private static class Filter<T> implements Iterator<T> {
        private final Predicate<T> filter;
        private final Iterator<T> source;

        @Override
        public boolean hasNext() {
            return this.source.hasNext();
        }

        @Override
        public T next() {
            if (this.filter != null) {
                do {
                    final T item = this.source.next();

                    if (this.filter.test(item)) {
                        return item;
                    }
                }
                while (this.source.hasNext());
            }

            return this.source.next();
        }

        private Filter(final Predicate<T> filter, final Iterator<T> source) {
            this.filter = filter;
            this.source = source;
        }

    }

    private static class Each<T> implements Iterator<T> {
        private final Consumer<T> each;
        private final Iterator<T> source;

        @Override
        public boolean hasNext() {
            return this.source.hasNext();
        }

        @Override
        public T next() {
            if (this.each != null) {
                final T item = this.source.next();

                this.each.accept(item);

                return item;
            }

            return this.source.next();
        }

        private Each(final Consumer<T> each, final Iterator<T> source) {
            this.each = each;
            this.source = source;
        }

    }

    public static class In<T> implements Iterator<T> {
        private final Iterator<T> source;

        @Override
        public boolean hasNext() {
            return this.source.hasNext();
        }

        public <X> In<X> derive(final Function<T, X> f) {
            return new In<>(new Derive<>(f, this.source));
        }

        public In<T> filter(final Predicate<T> p) {
            return new In<>(new Filter<>(p, this.source));
        }

        public In<T> each(final Consumer<T> c) {
            return new In<>(new Each<>(c, this.source));
        }

        @Override
        public T next() {
            return this.source.next();
        }

        public <R> R reduce(final BiFunction<R, T, R> b, final R initial) {
            R current = initial;

            while (this.source.hasNext()) {
                current = b.apply(current, this.source.next());
            }

            return current;
        }

        public void all() {
            while (this.source.hasNext()) {
                this.source.next();
            }
        }

        public void one() {
            while (this.source.hasNext()) {
                this.source.next();
                return;
            }
        }

        private In(final Iterator<T> source) {
            this.source = source;
        }

    }

    /**
     * Produces a chainable collection from the iterable. Given an iterable set of
     * strings, you operate handlers according to the iteration order:
     *
     * <pre>
     * in(adapt("a", "b", "c", "a"))
     *     .derive((item) -> item.equals("a") ? 1 : 0)
     *     .each((item) -> {
     *         System.out.println("Found '" + item + "'");
     *     })
     *     .reduce((item, total) -> {
     *         return total += item;
     *     }, 0);
     *
     * Found '1'
     * Found '0'
     * Found '0'
     * Found '1'
     * 2
     *
     * in(adapt("a", "b", "c", "a"))
     *     .filter((item) -> item.equals("a"))
     *     .each((item) -> {
     *         System.out.println("Found '" + item + "'");
     *     })
     *     .all();
     *
     * Found 'a'
     * Found 'a'
     *
     * in(adapt("a", "b", "c", "a"))
     *     .filter((item) -> item.equals("a"))
     *     .each((item) -> {
     *         System.out.println("Found '" + item + "'");
     *     })
     *     .one();
     *
     * Found 'a'
     * </pre>
     *
     * @param source
     * @param <T>
     */
    public static <T> In<T> in(final Iterator<T> source) {
        return new In<>(source);
    }

}

/*/
 * Copyright 2019 Kirk Bulis
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
/*/
