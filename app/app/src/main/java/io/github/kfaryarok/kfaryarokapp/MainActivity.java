package io.github.kfaryarok.kfaryarokapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.github.kfaryarok.kfaryarokapp.settings.SettingsActivity;
import io.github.kfaryarok.kfaryarokapp.updates.Update;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateAdapter;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateHelper;
import io.github.kfaryarok.kfaryarokapp.util.PreferenceUtil;

public class MainActivity extends AppCompatActivity implements UpdateAdapter.UpdateAdapterOnClickHandler {

    private RecyclerView mUpdatesRecyclerView;
    private UpdateAdapter mUpdateAdapter;

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

        setupUpdateRecyclerView();
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
        mUpdateAdapter = new UpdateAdapter(setupUpdates(), this);
        mUpdatesRecyclerView.setAdapter(mUpdateAdapter);
    }

    public Update[] setupUpdates() {
        Update[] updates = UpdateHelper.getUpdates(this);
        if (updates.length == 0) {
            // failed somewhere along the line of getting the updates so notify user and exit
            Toast.makeText(this, "כישלון בעיבוד נתונים.", Toast.LENGTH_LONG).show();
            finish();
            return null;
        }
        return updates;
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
    public void onClickCard(Update update) {
        // really nothing to do here as of now
    }

    @Override
    public void onClickOptions(final Update update, Button buttonView) {
        PopupMenu popupMenu = new PopupMenu(this, buttonView);
        popupMenu.getMenuInflater().inflate(R.menu.update_card, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_card_copytext:
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboard.setPrimaryClip(ClipData.newPlainText("Update Text", update.getText()));

                        if (mToast != null) {
                            mToast.cancel();
                        }
                        mToast = Toast.makeText(MainActivity.this, getString(R.string.toast_card_copiedtext), Toast.LENGTH_LONG);
                        mToast.show();
                        break;
                }
                return false;
            }

        });

        popupMenu.show();
    }

}
