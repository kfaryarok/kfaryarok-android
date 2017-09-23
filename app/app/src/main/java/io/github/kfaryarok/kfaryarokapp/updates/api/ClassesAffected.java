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

package io.github.kfaryarok.kfaryarokapp.updates.api;

/**
 * Simple wrapper for affecting specific classes.
 * The classes need to be in English, and only when displayed to the user should
 * they be converted back to Hebrew.
 *
 * @author tbsc on 03/03/2017
 */
public class ClassesAffected implements Affected {

    private String[] classesAffected;

    public ClassesAffected(String... classes) {
        this.classesAffected = classes;
    }

    /**
     * If ever needed, gets the affected classes array.
     * Not in {@link Affected} because not every class has one.
     * @return affected classes array.
     */
    public String[] getClassesAffected() {
        return classesAffected;
    }

    /**
     * Checks to see if the class is affected, by seeing if it exists in the array of affected classes.
     * @param clazz The class to check
     * @return is this class affected
     */
    @Override
    public boolean affects(String clazz) {
        // daily null check
        if (clazz == null || clazz.length() == 0) {
            return false;
        }

        // loop through array
        for (String clazzAffected : classesAffected) {
            if (clazzAffected.equals(clazz)) {
                // exists
                return true;
            }
        }

        // doesn't
        return false;
    }

    /**
     * Isn't {@link GlobalAffected} or a subclass of it, so not global.
     * @return false
     */
    @Override
    public boolean isGlobal() {
        return false;
    }

}
