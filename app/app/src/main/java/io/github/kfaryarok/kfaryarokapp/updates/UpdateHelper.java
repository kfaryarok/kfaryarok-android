package io.github.kfaryarok.kfaryarokapp.updates;

import android.content.Context;

import org.json.JSONException;

import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;
import io.github.kfaryarok.kfaryarokapp.util.TestUtil;

/**
 * Allows me to centralize update-getting to one place, and changing it throughout the app
 * without digging around.
 *
 * @author tbsc on 16/03/2017
 */
public class UpdateHelper {

    /**
     * TODO currently uses test data!
     * Fetches updates, parses them and filters them.
     * @return List of relavant updates, or an empty array if failed parsing
     */
    public static Update[] getUpdates(Context ctx) {
        try {
            return UpdateParser.filterUpdates(UpdateParser.parseUpdates(TestUtil.getTestJsonString() /* UpdateFetcher.fetchUpdates() */), PreferenceUtil.getClassPreference(ctx));
        } catch (JSONException e) {
            e.printStackTrace();
            return new Update[0];
        }
    }

    /**
     * Creates a string of the classes in the array, with the user's current class first and
     * other classes afterwards, comma-separated.
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

        // we know the update MUST affect the user, so put his class first
        classBuilder.append(userClass);

        // if there're more classes than the users, put a comma
        if (classes.length > 1) {
            classBuilder.append(", ");
        }

        for (int i = 0; i < classes.length; i++) {
            String clazz = classes[i];

            // if update's class is not the user's class (which is already appended)
            if (!clazz.equalsIgnoreCase(userClass)) {
                // put it too
                classBuilder.append(clazz);

                // if the last class isn't the user's class and we haven't reached the last class, put a comma
                if (!classes[classes.length - 1].equalsIgnoreCase(userClass) && i != classes.length - 1) {
                    classBuilder.append(", ");
                }
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
        if (update == null) {
            // your daily null check!
            return null;
        }

        String affects = "";

        if (update.getAffected().isGlobal()) {
            // global update; set affects string to global_update
            affects = ctx.getString(R.string.global_update);
        } else if (update.getAffected() instanceof ClassesAffected) {
            // normal update; get formatted class string
            ClassesAffected affected = (ClassesAffected) update.getAffected();
            affects = formatClassString(affected.getClassesAffected(), PreferenceUtil.getClassPreference(ctx));
        }

        // return formatted string like in this example (in english): I7, K5: blah blah
        return affects + ": " + update.getText();
    }

}
