package io.github.kfaryarok.kfaryarokapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import io.github.kfaryarok.kfaryarokapp.util.ClassUtil;

/**
 * To start this activity correctly, start an intent to this activity,
 * with the long text of the update put as {@link Intent#EXTRA_TEXT} String
 * and the affected classes/if global as {@link Intent#EXTRA_SUBJECT} String[]
 */
public class UpdateDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        String longText = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        String[] affected = getIntent().getStringArrayExtra(Intent.EXTRA_SUBJECT);

        // if there's any invalid data
        if (longText == null || affected == null || affected.length == 0) {
            finish();
            Toast.makeText(this, "כישלון בהצגת העדכון.", Toast.LENGTH_LONG).show();
        }

        TextView tvClass = (TextView) findViewById(R.id.tv_updatedetails_class);
        boolean isGlobal = false;
        if (affected.length == 1) {
            if ("global".equals(affected[0])) {
                tvClass.setText(R.string.global_update);
                isGlobal = true;
            }
        }

        if (!isGlobal) {
            for (int i = 0; i < affected.length; i++) {
                String clazz = affected[i];
                tvClass.append(ClassUtil.convertEnglishClassToHebrew(clazz));
                if (i != affected.length - 1) {
                    tvClass.append(", ");
                }
            }
        }

        TextView tvLongText = (TextView) findViewById(R.id.tv_updatedetails_longtext);
        tvLongText.setText(longText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // // TODO do stuff with menuitems here
        return super.onOptionsItemSelected(item);
    }

}
