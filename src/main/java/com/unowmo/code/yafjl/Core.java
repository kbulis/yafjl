package com.unowmo.code.yafjl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Core
 *
 * ...
 *
 */
public class Core {

    public interface ConsumerWithThrows<T> {

        void accept(final T item) throws Exception;

    }

    public interface FunctionWithThrows {

        void launch() throws Exception;

    }

    public static class In<T> {
        private final List<T> found;
        private final Exception caught;

        public In<T> then(final ConsumerWithThrows<T> c) {
            if (this.found != null && this.found.size() > 0) {
                for (final T item : this.found) {
                    if (c != null) {
                        try {
                            c.accept(item);
                        }
                        catch (final Exception eX) {
                            return new In<>(this.found, eX);
                        }
                    }
                }
            }

            return this;
        }

        public In<T> none(final FunctionWithThrows f) {
            if (this.found == null || this.found.size() == 0) {
                if (f != null) {
                    try {
                        f.launch();
                    }
                    catch (final Exception eX) {
                        return new In<>(this.found, eX);
                    }
                }
            }

            return this;
        }

        public In<T> oops(final Consumer<Exception> c) {
            if (this.caught != null) {
                if (c != null) {
                    c.accept(this.caught);
                }
            }

            return this;
        }

        private In(final List<T> found, final Exception caught) {
            this.found = found;
            this.caught = caught;
        }

        private In(final List<T> found) {
            this.found = found;
            this.caught = null;
        }

    }

    public static class Find<T> {
        private final T pattern;

        public In<T> in(final Iterable<T> iterable) {
            final ArrayList<T> found = new ArrayList<>();

            if (iterable != null && this.pattern != null) {
                for (final T item : iterable) {
                    if (item.equals(pattern)) {
                        found.add(item);
                    }
                }
            }

            return new In<>(found);
        }

        private Find(final T pattern) {
            this.pattern = pattern;
        }

    }

    public static <T> Find<T> find(final T pattern) {
        return new Find<>(pattern);
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
