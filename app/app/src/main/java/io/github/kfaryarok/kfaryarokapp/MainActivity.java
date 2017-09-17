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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import io.github.kfaryarok.kfaryarokapp.settings.SettingsActivity;
import io.github.kfaryarok.kfaryarokapp.updates.Update;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateAdapter;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateHelper;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;

public class MainActivity extends AppCompatActivity implements UpdateAdapter.UpdateAdapterOnClickHandler {

    private RecyclerView mUpdatesRecyclerView;
    private UpdateAdapter mUpdateAdapter;
    private TextView mInfoTextView;
    public TextView mOutdatedWarningTextView;

    private SharedPreferences prefs;

    private Toast mToast;

    public static boolean mResumeFromFirstLaunch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        checkFirstLaunch();
        setupLayoutDirection();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupMisc();
        setupUpdateRecyclerView();
        updateInfoTextView();
    }

    public void checkFirstLaunch() {
        if (!PreferenceUtil.getLaunchedBeforePreference(this)) {
            // first launch!
            // open settings activity configured for first launch
            Intent firstLaunchActivity = new Intent(this, SettingsActivity.class).putExtra(Intent.EXTRA_TEXT, true);
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
        mUpdateAdapter = new UpdateAdapter(UpdateHelper.getUpdates(this, true), this);
        mUpdatesRecyclerView.setAdapter(mUpdateAdapter);
    }

    public void setupMisc() {
        mOutdatedWarningTextView = (TextView) findViewById(R.id.tv_main_outdated_warning);
    }

    public void updateInfoTextView() {
        mInfoTextView = (TextView) findViewById(R.id.tv_main_info);
        mInfoTextView.setText(String.format("עודכן לאחרונה: %s", UpdateHelper.getWhenLastCachedFormatted(this)));
    }

    /**
     * Meant to be used with {@link UpdateHelper#getUpdates(Context, boolean)} failures.
     * @return *ALWAYS* returns null!
     */
    public Update[] showNoCacheAndNoInternetError(Exception serverException, Exception cacheException) {
        showToast(getString(R.string.toast_load_nocache_nointernet));
        Log.e("MainActivity",
                "Failed getting updates from server and cache (cache exception: "
                        + cacheException.getMessage()
                        + ", server exception: " + serverException.getMessage() + ")");
        return null;
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
