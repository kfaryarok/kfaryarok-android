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

package io.github.kfaryarok.kfaryarokapp.updates;

/**
 * Interface for checking is a certain class if affected.
 *
 * @author tbsc on 03/03/2017
 */
public interface Affected {

    /**
     * Check if this affects the given class.
     * @param clazz The class to check
     * @return if this affects that class
     */
    boolean affects(String clazz);

    /**
     * Only if the update is global should this be returning true.
     * @return if update is global
     */
    boolean isGlobal();

}
