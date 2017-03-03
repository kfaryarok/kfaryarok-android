package io.github.kfaryarok.kfaryarokapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import io.github.kfaryarok.kfaryarokapp.updates.Update;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateAdapter;
import io.github.kfaryarok.kfaryarokapp.updates.UpdateImpl;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mUpdatesRecyclerView;
    private UpdateAdapter mUpdateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO fetch updates, filter unwanted ones and save into array
        Update[] updates = new Update[] {
                new UpdateImpl("שלום"),
                new UpdateImpl("test", "hello?"),
                new UpdateImpl(new String[] {
                        "ח4", "י7"
                }, "private!")
        };

        mUpdatesRecyclerView = (RecyclerView) findViewById(R.id.rv_updates);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mUpdatesRecyclerView.setLayoutManager(layoutManager);
        mUpdatesRecyclerView.setHasFixedSize(true);

        mUpdateAdapter = new UpdateAdapter(updates.length, updates);
        mUpdatesRecyclerView.setAdapter(mUpdateAdapter);
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
                // TODO settings menu
                break;
            case R.id.menu_advanced:
                item.setChecked(!item.isChecked());
                // TODO save checked data and show advanced options
                break;
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
