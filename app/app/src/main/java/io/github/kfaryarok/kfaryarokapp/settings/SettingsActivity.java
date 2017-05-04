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

package io.github.kfaryarok.kfaryarokapp.settings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.github.kfaryarok.kfaryarokapp.R;

/**
 * Intent.EXTRA_TEXT is used to tell the activity if it's running as a first launch activity.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        boolean mFirstLaunchActivity = getIntent().getBooleanExtra(Intent.EXTRA_TEXT, false);

        if (mFirstLaunchActivity) {
            ActionBar ab = getSupportActionBar();

            if (ab != null) {
                // disable up button if on first launch mode
                ab.setDisplayHomeAsUpEnabled(false);
            }

            setTitle(R.string.act_firstlaunch);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (mFirstLaunchActivity) {
//            // first launch, then put the first launch menu
//            getMenuInflater().inflate(R.menu.first_launch, menu);
//            return true;
//        } else {
//            // normal settings, so show normal menu
//            return super.onCreateOptionsMenu(menu);
//        }
//    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.menu_firstlaunch_accept) {
//            finish();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
