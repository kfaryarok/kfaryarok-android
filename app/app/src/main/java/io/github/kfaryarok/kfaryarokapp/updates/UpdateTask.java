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
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;

import io.github.kfaryarok.kfaryarokapp.R;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;
import io.github.kfaryarok.kfaryarokapp.util.functional.Consumer;
import io.github.kfaryarok.kfaryarokapp.util.functional.FunctionalUtil;

/**
 * @author tbsc on 20/09/2017
 */
public class UpdateTask extends AsyncTask<Context, String, Update[]> {

    private Consumer<String> mShowToast;

    /**
     * A constructor that lets you specify how should toasts be shown.
     * @param showToast consumemr that will show a toast of the given string
     */
    public UpdateTask(Consumer<String> showToast) {
        this.mShowToast = showToast;
    }

    /**
     * A constructor that will not show any toasts (empty consumer)
     */
    public UpdateTask() {
        this.mShowToast = FunctionalUtil.emptyConsumer();
    }

    @Override
    protected Update[] doInBackground(Context... params) {
        return getUpdates(params[0], mShowToast);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        mShowToast.accept(values[0]);
    }

    /**
     * Based on when was cache last updated, decides whether cache or server data should be used.
     * It then parses and filters it.
     *
     * If the cached data is older than an hour, it fetches new data, and if it's older than 3 hours,
     * it shows a warning to the user that it's outdated.
     *
     * @param ctx Used to get cache, get strings, and update UI if enabled
     * @param showToast Let callers of this method define how/if showing toasts should be done
     * @return Parsed filtered updates from either cache/server
     */
    public Update[] getUpdates(Context ctx, Consumer<String> showToast) {
        // TODO: Look into Android's sync adapters

        // if last sync was less than an hour ago, use cache instead of syncing again
        if (!UpdateHelper.isCacheOlderThan1Hour(ctx)) {
            try {
                // first trying to get updates from cache
                return UpdateHelper.getUpdatesFromCache(ctx);
            } catch (FileNotFoundException cacheException) {
                // failed getting data from cache
                try {
                    // trying to get updates from server
                    Log.i("UpdateHelper", "No cache saved, syncing from server");
                    return UpdateHelper.getUpdatesFromServer(ctx);
                } catch (IOException | JSONException serverException) {
                    // failed getting data from server too, error out
                    showNoCacheAndNoInternetError(ctx, serverException, cacheException);
                    return null;
                }
            } catch (JSONException e) {
                // whatever's in cache is invalid, delete it and retry
                UpdateHelper.deleteCache(ctx);
                return getUpdates(ctx, showToast);
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
                    updates = UpdateHelper.getUpdatesFromCache(ctx);
                    showToast.accept(ctx.getString(R.string.toast_load_nointernet_usingcache));
                    return updates;
                } catch (FileNotFoundException cacheException) {
                    // loading from cache failed too, just error out and tell user
                    return showNoCacheAndNoInternetError(ctx, serverException, cacheException);
                } catch (JSONException e) {
                    // whatever's in cache is invalid, delete it and retry
                    UpdateHelper.deleteCache(ctx);
                    return getUpdates(ctx, showToast);
                }
            }

            // some fail-safes to help user in case he entered an invalid custom update server
            // TODO: This can't ever be reached, make sure it can
            if (updates == null) {
                if (!PreferenceUtil.getUpdateServerPreference(ctx).equals(UpdateFetcher.DEFAULT_UPDATE_URL)) {
                    // it failed and it doesn't use the default update url, so switch to default and retry
                    PreferenceUtil.getSharedPreferences(ctx).edit()
                            .putString(ctx.getString(R.string.pref_updateserver_string), ctx.getString(R.string.pref_updateserver_string_def))
                            .apply();
                    showToast.accept(ctx.getString(R.string.toast_load_defaultserver_revert));
                    // re-run this method, but now with default server
                    return getUpdates(ctx, showToast);
                }
                // failed somewhere along the line of getting the updates so notify user
                showToast.accept(ctx.getString(R.string.toast_load_failure));
            }

            return updates;
        }
    }

    /**
     * @return *ALWAYS* returns null!
     */
    public Update[] showNoCacheAndNoInternetError(Context ctx, Exception serverException, Exception cacheException) {
        publishProgress(ctx.getString(R.string.toast_load_nocache_nointernet));
        Log.e("MainActivity",
                "Failed getting updates from server and cache (cache exception: "
                        + cacheException.getMessage()
                        + ", server exception: " + serverException.getMessage() + ")");
        return null;
    }

}
