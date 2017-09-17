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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import io.github.kfaryarok.kfaryarokapp.MainActivity;
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
     * @throws IOException Failed retrieving updates from server
     * @throws JSONException Invalid JSON retrieved
     */
    public static Update[] getUpdatesFromServer(Context ctx) throws IOException, JSONException {
        try {
            String json = new UpdateFetcher().execute(ctx).get();
            if ("".equals(json)) {
                throw new IOException();
            } else {
                // I know all of these try blocks are ugly but that's how it's gotta be done
                try {
                    setUpdatesCache(ctx, json);
                } catch (IOException e) {
                    Log.w("UpdateHelper", "Failed caching updates: " + e.getMessage());
                }
                return UpdateParser.filterUpdates(UpdateParser.parseUpdates(json), PreferenceUtil.getClassPreference(ctx));
            }
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UpdateHelper", "Update fetcher thread interrupted (" + e.getMessage() + ")");
            return null;
        }
    }

    /**
     * Parses cached updates and filters them.
     * @param ctx Used to access cache directory
     * @return Parsed, filtered updates from cache
     * @throws FileNotFoundException Nothing is cached
     * @throws JSONException Cache is invalid
     */
    public static Update[] getUpdatesFromCache(Context ctx) throws FileNotFoundException, JSONException {
        return UpdateParser.filterUpdates(UpdateParser.parseUpdates(UpdateHelper.getUpdatesCache(ctx)), PreferenceUtil.getClassPreference(ctx));
    }

    /**
     * Based on when was cache last updated, decided whether cache or server data should be used.
     * It then parses and filters it.
     *
     * If the cached data is older than an hour, it fetches new data, and if it's older than 3 hours,
     * it shows a warning to the user that it's outdated.
     *
     * @param ctx Used to get cache, get strings, and update UI if enabled
     * @param notifyUi Should this method update {@link MainActivity}'s UI
     *                 If this is true, ctx ***MUST*** be the instance of {@link MainActivity}!
     * @return Parsed filtered updates from either cache/server
     */
    public static Update[] getUpdates(Context ctx, boolean notifyUi) {
        // TODO: Look into Android's sync adapters

        // This method is called in MainActivity and in AlertHelper.
        // In order to show toasts, access to MainActivity's showToast method is needed.
        // AlertHelper doesn't show any toasts, so access to MainActivity isn't needed.
        // I don't want to have duplicate methods, so when notifyUi is true,
        // ctx is assumed to be an instance of MainActivity.
        // If notifyUi is true but ctx isn't an instance of MainActivity, an NPE will be thrown.

        MainActivity main = null;
        if (notifyUi && ctx instanceof MainActivity) {
            main = (MainActivity) ctx;
        }

        // if last sync was less than an hour ago, use cache instead of syncing again
        long oneHourInMillis = 3600000;
        if (UpdateHelper.isCached(ctx) && System.currentTimeMillis() - UpdateHelper.getWhenLastCached(ctx).getTime() < oneHourInMillis) {
            try {
                // first trying to get updates from cache
                return getUpdatesFromCache(ctx);
            } catch (FileNotFoundException cacheException) {
                // failed getting data from cache
                try {
                    // trying to get updates from server
                    Log.i("UpdateHelper", "No cache saved, syncing from server");
                    return UpdateHelper.getUpdatesFromServer(ctx);
                } catch (IOException | JSONException serverException) {
                    // failed getting data from server too, error out
                    if (notifyUi) {
                        main.showNoCacheAndNoInternetError(serverException, cacheException);
                    }
                    return null;
                }
            } catch (JSONException e) {
                // whatever's in cache is invalid, delete it and retry
                deleteCache(ctx);
                return getUpdates(ctx, notifyUi);
            }
        } else {
            Update[] updates;

            // last sync was more than an hour ago, try syncing from server first
            try {
                updates = UpdateHelper.getUpdatesFromServer(ctx);
            } catch (IOException | JSONException serverException) {
                // loading from server failed
                try {
                    // try showing cached data
                    updates = getUpdatesFromCache(ctx);

                    if (notifyUi) {
                        main.showToast(ctx.getString(R.string.toast_load_nointernet_usingcache));

                        // if cached data is older than 3 hours tell user it might be outdated
                        long threeHoursInMillis = 10800000;
                        if (UpdateHelper.isCached(ctx) && System.currentTimeMillis() - UpdateHelper.getWhenLastCached(ctx).getTime() > threeHoursInMillis) {
                            main.mOutdatedWarningTextView.setVisibility(View.VISIBLE);
                            main.mOutdatedWarningTextView.setText(R.string.tv_main_warning_outdated);
                        }
                    }

                    return updates;
                } catch (FileNotFoundException cacheException) {
                    // loading from cache failed too, just error out and tell user
                    if (notifyUi) {
                        main.showNoCacheAndNoInternetError(serverException, cacheException);
                    }
                    return null;
                } catch (JSONException e) {
                    // whatever's in cache is invalid, delete it and retry
                    deleteCache(ctx);
                    return getUpdates(ctx, notifyUi);
                }
            }

            // some fail-safes to help user in case he entered an invalid custom update server
            if (updates == null) {
                if (!PreferenceUtil.getUpdateServerPreference(ctx).equals(UpdateFetcher.DEFAULT_UPDATE_URL)) {
                    // it failed and it doesn't use the default update url, so switch to default and retry
                    PreferenceUtil.getSharedPreferences(ctx).edit()
                            .putString(ctx.getString(R.string.pref_updateserver_string), ctx.getString(R.string.pref_updateserver_string_def))
                            .apply();
                    if (notifyUi) {
                        main.showToast(ctx.getString(R.string.toast_load_defaultserver_revert));
                    }
                    // re-run this method, but now with default server
                    return getUpdates(ctx, notifyUi);
                }
                // failed somewhere along the line of getting the updates so notify user
                if (notifyUi) {
                    main.showToast(ctx.getString(R.string.toast_load_failure));
                }
            }

            return updates;
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

        // convert to array list so to remove the user's class from the list
        ArrayList<String> classList = new ArrayList<>();
        Collections.addAll(classList, classes);

        if (classList.contains(userClass)) {
            // don't assume about user class, first check if it's even there
            classBuilder.append(userClass);
            classList.remove(userClass);
        }

        if (!classList.isEmpty()) {
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

    /**
     * Retrieves the last downloaded JSON file from the app's cache directory.
     * @param ctx Used to access the cache directory
     * @return Cached updates
     * @throws FileNotFoundException If file wasn't found, meaning no cache is available
     */
    public static String getUpdatesCache(Context ctx) throws FileNotFoundException {
        File appDir = ctx.getCacheDir();
        File lastSynced = new File(appDir, "update.json");
        return new Scanner(lastSynced).useDelimiter("\\Z").next();
    }

    /**
     * Caches the given JSON string data to a file in the app's cache directory.
     * @param ctx Used to access the cache directory
     * @param data What to cache
     * @throws IOException if file creation/writing fails
     */
    public static void setUpdatesCache(Context ctx, String data) throws IOException {
        File appDir = ctx.getCacheDir();
        File lastSynced = new File(appDir, "update.json");
        lastSynced.createNewFile();
        FileWriter writer = new FileWriter(lastSynced);
        writer.write(data);
        writer.close();
    }

    /**
     * Checks when were the updates cached last time.
     * @param ctx Used to get cache directory
     * @return {@link java.util.Date} object of when it happened
     */
    public static Date getWhenLastCached(Context ctx) {
        File appDir = ctx.getCacheDir();
        File lastSynced = new File(appDir, "update.json");
        return new Date(lastSynced.lastModified());
    }

    /**
     * Sees when cache was last updated and formats it, in "HH:mm:ss dd/MM/yyyy" format.
     * If cache was never updated, it returns "אף פעם".
     * @param ctx Used to see when cache was last updated as a {@link Date}
     * @return formatted string of the date returned by {@link #getWhenLastCached(Context)}
     */
    public static String getWhenLastCachedFormatted(Context ctx) {
        Date lastUpdated = getWhenLastCached(ctx);
        if (lastUpdated.equals(new Date(0))) {
            return "אף פעם";
        } else {
            return (String) DateFormat.format("kk:mm:ss dd/MM/yyyy", lastUpdated);
        }
    }

    /**
     * Checks if there's a cached file.
     * @param ctx Used to access the cache directory
     * @return Does update.json exist in the cache directory
     */
    public static boolean isCached(Context ctx) {
        File appDir = ctx.getCacheDir();
        File lastSynced = new File(appDir, "update.json");
        return lastSynced.exists();
    }

    /**
     * If exists, deletes update.json in the cache directory
     * @param ctx Used to access the cache directory
     */
    public static void deleteCache(Context ctx) {
        File appDir = ctx.getCacheDir();
        File lastSynced = new File(appDir, "update.json");
        if (lastSynced.exists()) {
            lastSynced.delete();
        }
    }

}
