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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.btn_about_advancedmode_easteregg)
    private Button btnEasterEgg;

    private int easterEggClickCounter;
    private static final int clickCounterMax = 7;
    private boolean developerModeActive;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        ViewCompat.setLayoutDirection(getWindow().getDecorView(), ViewCompat.LAYOUT_DIRECTION_LTR);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // cache current devmode state
        developerModeActive = prefs.getBoolean(getString(R.string.pref_advanced_mode_bool), false);

        if (developerModeActive) {
            // show button if devmode is enabled already, as a way to disable it
            btnEasterEgg.setBackgroundResource(android.R.drawable.btn_default);
        } else {
            btnEasterEgg.setText("");
        }

        btnEasterEgg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!developerModeActive) {
                    // only if devmode isn't enabled
                    if (easterEggClickCounter == clickCounterMax) {
                        // update prefs
                        prefs.edit().putBoolean(getString(R.string.pref_advanced_mode_bool), developerModeActive = true).apply();

                        // alert user
                        if (toast != null) {
                            toast.cancel();
                        }
                        toast = Toast.makeText(getApplicationContext(), getString(R.string.toast_devmode_enabled), Toast.LENGTH_LONG);
                        toast.show();

                        btnEasterEgg.setBackgroundResource(android.R.drawable.btn_default);
                        btnEasterEgg.setText(getString(R.string.btn_devmode_easteregg));
                    } else {
                        // increase count
                        easterEggClickCounter++;

                        if (easterEggClickCounter >= 4) {
                            if (toast != null) {
                                toast.cancel();
                            }
                            // the extra space is because android removes spaces from strings
                            toast = Toast.makeText(getApplicationContext(),
                                    (clickCounterMax - easterEggClickCounter + 1) + " " +
                                            getString(R.string.toast_devmode_clicks_remaining),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                } else {
                    prefs.edit().putBoolean(getString(R.string.pref_advanced_mode_bool), developerModeActive = false).apply();
                    btnEasterEgg.setBackgroundResource(android.R.color.transparent);
                    btnEasterEgg.setText("");
                    easterEggClickCounter = 0;

                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(getApplicationContext(), getString(R.string.toast_devmode_disabled), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

}
