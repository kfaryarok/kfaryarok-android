package io.github.kfaryarok.kfaryarokapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    private int mEasterEggClickCounter;
    private static final int mClickCounterMax = 7;
    private boolean mDeveloperModeActive;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // cache current devmode state
        mDeveloperModeActive = prefs.getBoolean(getString(R.string.pref_advanced_mode_bool), false);

        final Button btnEasterEgg = (Button) findViewById(R.id.btn_about_advancedmode_easteregg);

        if (mDeveloperModeActive) {
            // show button if devmode is enabled already, as a way to disable it
            btnEasterEgg.setBackgroundResource(android.R.drawable.btn_default);
        } else {
            btnEasterEgg.setText("");
        }

        btnEasterEgg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!mDeveloperModeActive) {
                    // only if devmode isn't enabled
                    if (mEasterEggClickCounter == mClickCounterMax) {
                        // update prefs
                        prefs.edit().putBoolean(getString(R.string.pref_advanced_mode_bool), mDeveloperModeActive = true).apply();

                        // alert user
                        if (mToast != null) {
                            mToast.cancel();
                        }
                        mToast = Toast.makeText(getApplicationContext(), getString(R.string.toast_devmode_enabled), Toast.LENGTH_LONG);
                        mToast.show();

                        btnEasterEgg.setBackgroundResource(android.R.drawable.btn_default);
                        btnEasterEgg.setText(getString(R.string.btn_devmode_easteregg));
                    } else {
                        // increase count
                        mEasterEggClickCounter++;

                        if (mEasterEggClickCounter >= 4) {
                            if (mToast != null) {
                                mToast.cancel();
                            }
                            // the extra space is because android removes spaces from strings
                            mToast = Toast.makeText(getApplicationContext(),
                                    (mClickCounterMax - mEasterEggClickCounter + 1) + " " +
                                            getString(R.string.toast_devmode_clicks_remaining),
                                    Toast.LENGTH_LONG);
                            mToast.show();
                        }
                    }
                } else {
                    prefs.edit().putBoolean(getString(R.string.pref_advanced_mode_bool), mDeveloperModeActive = false).apply();
                    btnEasterEgg.setBackgroundResource(android.R.color.transparent);
                    btnEasterEgg.setText("");
                    mEasterEggClickCounter = 0;

                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(getApplicationContext(), getString(R.string.toast_devmode_disabled), Toast.LENGTH_LONG);
                    mToast.show();
                }
            }
        });
    }

}
