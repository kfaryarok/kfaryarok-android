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

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;

/**
 * Allows me to centralize update-getting to one place, and changing it throughout the app
 * without digging around.
 *
 * @author tbsc on 16/03/2017
 */
public class UpdateHelper {

    /**
     * Fetches updates, parses them and filters them.
     * @return List of relavant updates, or an empty array if failed parsing
     */
    public static Update[] getUpdates(Context ctx) {
        try {
            return UpdateParser.filterUpdates(UpdateParser.parseUpdates(new UpdateFetcher().execute(ctx).get()), PreferenceUtil.getClassPreference(ctx));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Update[0];
        }
    }

    /**
     * Creates a string of the classes in the array, with the user's current class first and
     * other classes afterwards, comma-separated.
     *
     * I'll try to explain how it works, because it's not simple at first glance.
     * It assumes that the array contains the user's class, because it is supposed to have it, but
     * it won't cause it to error.
     * 1. Add the user's class first, always.
     * 2. If there is more than one class, add a comma for the next classes
     * 3. Loop through the classes
     * 4. Continue to next class if it's the user's class (because it's always there)
     * 5. Append the class
     * Now, deciding whether to put a comma here is tricky.
     * 6. Append comma, unless:
     * - we're at last class
     * - we're at second last class AND last class is the user's class (to prevent putting unnecessary commas)
     *
     * @param classes The array of classes to format
     * @param userClass To know which class to put first. If the classes array doesn't have it, then it will not be reordered.
     * @return Class array in string form, comma-separated.
     */
    public static String formatClassString(String[] classes, String userClass) {
        if (classes == null || classes.length == 0 || userClass == null || userClass.length() == 0) {
            // if anything is invalid just return null
            return null;
        }
        // string builder for creating the class string
        StringBuilder classBuilder = new StringBuilder();

        // user's class is always there, and always first
        classBuilder.append(userClass);

        // convert to array list so to remove the user's class from the list
        ArrayList<String> classList = new ArrayList<>();
        Collections.addAll(classList, classes);
        classList.remove(userClass);

        // if no more classes, just return current string
        if (classList.isEmpty()) {
            return classBuilder.toString();
        }

        // there are more classes, put a comma
        classBuilder.append(", ");

        for (int i = 0; i < classList.size(); i++) {
            // put the class
            String clazz = classList.get(i);
            classBuilder.append(clazz);

            // if not the last class, put a comma
            if (i < classList.size() - 1) {
                classBuilder.append(", ");
            }
        }

        return classBuilder.toString();
    }

    /**
     * Takes the given update, and from the affected classes/"globality" and the text of the update,
     * format a string from it, showing it's information.
     * @param update The update to format
     * @param ctx Context, used to get preference values and strings
     * @return Formatted string detailing the update
     */
    public static String formatUpdate(Update update, Context ctx) {
        return formatUpdate(update, ctx.getString(R.string.global_update), PreferenceUtil.getClassPreference(ctx));
    }

    /**
     * Separated the actual work from the main method to make it easier for me to create a unit
     * test for this.
     * @param update The update object
     * @param globalUpdateString What text to show if it's global
     * @param userClass The user's class
     * @return Formatted string detailing the update
     */
    public static String formatUpdate(Update update, String globalUpdateString, String userClass) {
        if (update == null) {
            // your daily null check!
            return null;
        }

        String affects = "";

        if (update.getAffected().isGlobal()) {
            // global update; set affects string to global_update
            affects = globalUpdateString;
        } else if (update.getAffected() instanceof ClassesAffected) {
            // normal update; get formatted class string
            ClassesAffected affected = (ClassesAffected) update.getAffected();
            affects = formatClassString(affected.getClassesAffected(), userClass);
        }

        // return formatted string like in this example (in english): I7, K5: blah blah
        return affects + ": " + update.getText();
    }

}
