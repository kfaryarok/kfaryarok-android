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

package io.github.kfaryarok.kfaryarokapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.concurrent.ExecutionException;

import io.github.kfaryarok.kfaryarokapp.settings.SettingsActivity;
import io.github.kfaryarok.kfaryarokapp.updates.Update;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateAdapter;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateHelper;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateTask;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;
import io.github.kfaryarok.kfaryarokapp.util.functional.Consumer;

public class MainActivity extends AppCompatActivity implements UpdateAdapter.UpdateAdapterOnClickHandler {

    private RecyclerView mUpdatesRecyclerView;
    private UpdateAdapter mUpdateAdapter;
    private ViewSwitcher mViewSwitcher;
    private TextView mInfoTextView;
    public TextView mOutdatedWarningTextView;
    private ProgressBar mLoadProgressBar;

    private SharedPreferences prefs;

    private Toast mToast;

    public static boolean mResumeFromFirstLaunch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showLoadingScreen();

        checkFirstLaunch();
        setupLayoutDirection();

        setupMisc();
        setupUpdateRecyclerView();
        updateInfoTextView();

        hideLoadingScreen();
    }

    public void showLoadingScreen() {
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.vs_main_loading_data);
        mLoadProgressBar = (ProgressBar) findViewById(R.id.pb_main_loading);
        mViewSwitcher.showNext();
    }

    public void hideLoadingScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewSwitcher.showPrevious();
            }
        }, 150); // artificial delay
    }

    public void checkFirstLaunch() {
        if (!PreferenceUtil.getLaunchedBeforePreference(this)) {
            // first launch!
            // open settings activity configured for first launch
            Intent firstLaunchActivity = new Intent(this, SettingsActivity.class).putExtra(Intent.EXTRA_TEXT, true);
            hideLoadingScreen();
            startActivity(firstLaunchActivity);
        }
    }

    public void setupLayoutDirection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }

    public void setupUpdateRecyclerView() {
        mUpdatesRecyclerView = (RecyclerView) findViewById(R.id.rv_updates);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mUpdatesRecyclerView.setLayoutManager(layoutManager);
        mUpdatesRecyclerView.setHasFixedSize(true);

        setupUpdateAdapter();
    }

    public void setupUpdateAdapter() {
        try {
            mUpdateAdapter = new UpdateAdapter(getUpdatesAsync(), this);
            mUpdatesRecyclerView.setAdapter(mUpdateAdapter);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.e("MainActivity", "UpdateTask interrupted: " + e.getMessage());
        }
    }

    public Update[] getUpdatesAsync() throws ExecutionException, InterruptedException {
        return new UpdateTask(new Consumer<String>() {
            @Override
            public void accept(String s) {
                showToast(s);
            }
        }).execute(this).get(); // new AsyncTask<Context, String, Update[]>() {
//
//            private Toast mToast;
//
//            @Override
//            protected Update[] doInBackground(Context... params) {
//                return getUpdates(params[0]);
//            }
//
//            public Update[] getUpdates(Context ctx) {
//                // if last sync was less than an hour ago, use cache instead of syncing again
//                if (!UpdateHelper.isCacheOlderThan1Hour(ctx)) {
//                    try {
//                        // first trying to get updates from cache
//                        return UpdateHelper.getUpdatesFromCache(ctx);
//                    } catch (FileNotFoundException cacheException) {
//                        // failed getting data from cache
//                        try {
//                            // trying to get updates from server
//                            Log.i("UpdateHelper", "No cache saved, syncing from server");
//                            return getUpdatesFromServer(ctx);
//                        } catch (IOException | JSONException serverException) {
//                            // failed getting data from server too, error out
//                            return showNoCacheAndNoInternetError(serverException, cacheException);
//                        }
//                    } catch (JSONException e) {
//                        // whatever's in cache is invalid, delete it and retry
//                        UpdateHelper.deleteCache(ctx);
//                        return getUpdates(ctx);
//                    }
//                } else {
//                    Update[] updates;
//
//                    // last sync was more than an hour ago, try syncing from server first
//                    try {
//                        updates = getUpdatesFromServer(ctx);
//                    } catch (IOException | JSONException serverException) {
//                        // loading from server failed
//                        try {
//                            // try showing cached data
//                            updates = UpdateHelper.getUpdatesFromCache(ctx);
//
//                            publishProgress(ctx.getString(R.string.toast_load_nointernet_usingcache));
//
//                            return updates;
//                        } catch (FileNotFoundException cacheException) {
//                            // loading from cache failed too, just error out and tell user
//                            return showNoCacheAndNoInternetError(serverException, cacheException);
//                        } catch (JSONException e) {
//                            // whatever's in cache is invalid, delete it and retry
//                            UpdateHelper.deleteCache(ctx);
//                            return getUpdates(ctx);
//                        }
//                    }
//
//                    // some fail-safes to help user in case he entered an invalid custom update server
//                    if (updates == null) {
//                        if (!PreferenceUtil.getUpdateServerPreference(ctx).equals(UpdateFetcher.DEFAULT_UPDATE_URL)) {
//                            // it failed and it doesn't use the default update url, so switch to default and retry
//                            PreferenceUtil.getSharedPreferences(ctx).edit()
//                                    .putString(ctx.getString(R.string.pref_updateserver_string), ctx.getString(R.string.pref_updateserver_string_def))
//                                    .apply();
//                            publishProgress(ctx.getString(R.string.toast_load_defaultserver_revert));
//                            // re-run this method, but now with default server
//                            return getUpdates(ctx);
//                        }
//                        // failed somewhere along the line of getting the updates so notify user
//                        publishProgress(ctx.getString(R.string.toast_load_failure));
//                    }
//
//                    return updates;
//                }
//            }
//
//            public Update[] getUpdatesFromServer(Context ctx) throws IOException, JSONException {
//                String json = UpdateFetcher.fetchUpdates(ctx);
//                if ("".equals(json)) {
//                    throw new IOException();
//                } else {
//                    // I know all of these try blocks are ugly but that's how it's gotta be done
//                    try {
//                        UpdateHelper.setUpdatesCache(ctx, json);
//                    } catch (IOException e) {
//                        Log.w("UpdateHelper", "Failed caching updates: " + e.getMessage());
//                    }
//                    return UpdateParser.filterUpdates(UpdateParser.parseUpdates(json), PreferenceUtil.getClassPreference(ctx));
//                }
//            }
//
//            /**
//             * @return *ALWAYS* returns null!
//             */
//            public Update[] showNoCacheAndNoInternetError(Exception serverException, Exception cacheException) {
//                publishProgress(getString(R.string.toast_load_nocache_nointernet));
//                Log.e("MainActivity",
//                        "Failed getting updates from server and cache (cache exception: "
//                                + cacheException.getMessage()
//                                + ", server exception: " + serverException.getMessage() + ")");
//                return null;
//            }
//
//            @Override
//            protected void onProgressUpdate(String... values) {
//                if (mToast != null) {
//                    mToast.cancel();
//                }
//                mToast = Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_LONG);
//                mToast.show();
//            }
//
//            @Override
//            protected void onPostExecute(Update[] updates) {
//
//            }
//
//        }.execute(this).get();
    }

    public void setupMisc() {
        mOutdatedWarningTextView = (TextView) findViewById(R.id.tv_main_outdated_warning);
    }

    public void updateInfoTextView() {
        mInfoTextView = (TextView) findViewById(R.id.tv_main_info);
        mInfoTextView.setText(String.format("עודכן לאחרונה: %s", UpdateHelper.getWhenLastCachedFormatted(this)));

        // if cached data is older than 3 hours tell user it might be outdated
        if (UpdateHelper.isCacheOlderThan3Hours(MainActivity.this)) {
            MainActivity.this.mOutdatedWarningTextView.setVisibility(View.VISIBLE);
            MainActivity.this.mOutdatedWarningTextView.setText(R.string.tv_main_warning_outdated);
        }
    }

    public Toast showToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        mToast.show();
        return mToast;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mResumeFromFirstLaunch) {
            mResumeFromFirstLaunch = false;
            recreate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class).putExtra(Intent.EXTRA_TEXT, false));
                break;
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickCard(View v, Update update) {
        // if card is clicked and the line count is bigger than 3 (meaning it can be expanded/"dexpanded")
        TextView tvText = (TextView) v.findViewById(R.id.tv_updatecard_text);
        View viewExpand = v.findViewById(R.id.view_updatecard_expand);
        if (tvText.getLineCount() > 3) {
            // if current max lines is 3, expand to 100 lines, and else "dexpand" back to 3
            // it uses TextViewCompat instead of the given method for API 15 compatibility
            if (TextViewCompat.getMaxLines(tvText) == 3) {
                tvText.setMaxLines(100);
                viewExpand.setBackgroundResource(R.drawable.ic_arrow_drop_up_grey_600_24dp);
            } else {
                tvText.setMaxLines(3);
                viewExpand.setBackgroundResource(R.drawable.ic_arrow_drop_down_grey_600_24dp);
            }
        }
    }

    @Override
    public void onClickOptions(View v, final Update update, Button buttonView) {
        PopupMenu popupMenu = new PopupMenu(this, buttonView);
        popupMenu.getMenuInflater().inflate(R.menu.update_card, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_card_copytext:
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboard.setPrimaryClip(ClipData.newPlainText("Update Text", update.getText()));

                        showToast(getString(R.string.toast_card_copiedtext));
                        break;
                }
                return false;
            }

        });

        popupMenu.show();
    }

}
