/*
 * This file is part of kfaryarok-android.
 *
 * kfaryarok-android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * kfaryarok-android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with kfaryarok-android.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.kfaryarok.kfaryarokapp.util.functional;

/**
 * @author tbsc on 19/09/2017
 */
public class FunctionalUtil {

    /**
     * Returns a non-functional Function instance. It does nothing, and returns null.
     * @param <T> Anything
     * @param <R> Also anything
     * @return An instance of {@link Function} that will do nothing and return null
     */
    public static <T, R> Function<T, R> emptyFunction() {
        return new Function<T, R>() {
            @Override
            public R apply(T t) {
                return null;
            }
        };
    }

    /**
     * Returns a non-functional Function instance that always returns the given return value.
     * @param returnValue What should the function return
     * @param <T> Anything
     * @param <R> Also anything
     * @return An instance of {@link Function} that will do nothing and return the given return value
     */
    public static <T, R> Function<T, R> returnFunction(final R returnValue) {
        return new Function<T, R>() {
            @Override
            public R apply(T t) {
                return returnValue;
            }
        };
    }

    /**
     * Returns a non-functional Consumer instance. It does nothing.
     * @param <T> Anything
     * @return An instance of {@link Consumer} that will do nothing
     */
    public static <T> Consumer<T> emptyConsumer() {
        return new Consumer<T>() {
            @Override
            public void accept(T t) {
                // no-op
            }
        };
    }

    /**
     * Returns a supplier that always supplies null
     * @param <T> Anything
     * @return An instance of {@link Supplier} that will always supply null
     */
    public static <T> Supplier<T> nullSupplier() {
        return new Supplier<T>() {
            @Override
            public T get() {
                return null;
            }
        };
    }

    /**
     * Returns a supplier that always supplies the given object.
     * @param object What should the supplier supply
     * @param <T> Anything
     * @return An instance of {@link Supplier} that will always supply the given object
     */
    public static <T> Supplier<T> objectSupplier(final T object) {
        return new Supplier<T>() {
            @Override
            public T get() {
                return object;
            }
        };
    }

    /**
     * Returns a predicate that will always test false.
     * @param <T> Anything
     * @return An instance of {@link Predicate} that will always test false
     */
    public static <T> Predicate<T> falsePredicate() {
        return new Predicate<T>() {
            @Override
            public boolean test(T t) {
                return false;
            }
        };
    }

    /**
     * Returns a predicate that will always test true.
     * @param <T> Anything
     * @return An instance of {@link Predicate} that will always test true
     */
    public static <T> Predicate<T> truePredicate() {
        return new Predicate<T>() {
            @Override
            public boolean test(T t) {
                return true;
            }
        };
    }

}
