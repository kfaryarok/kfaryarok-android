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
 * Simple implementation of an immutable pair.
 * @author tbsc on 20/09/2017
 */
public class Pair<L, R> {

    private final L left;
    private final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L key() {
        return left;
    }

    public R value() {
        return right;
    }

    public L left() {
        return left;
    }

    public R right() {
        return right;
    }

    public Pair<R, L> flip() {
        return new Pair<>(right, left);
    }

    public static <L, R> Pair of(L left, R right) {
        return new Pair<>(left, right);
    }

}
