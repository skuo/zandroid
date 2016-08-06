package org.zandroid.zandroid;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MediaPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action for Media Player Activity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
    }

    @Override
    public void onResume() {
        super.onResume(); // Always call the superclass
    }

    @Override
    public void onPause() {
        super.onPause(); // Always call the superclass

        // Do something useful here such as auto-save user inputs
    }

    @Override
    public void onStop() {
        super.onStop(); // Always call the superclass method first

        // Perform larger CPU intensive shut-down operations, such as persisting to db
    }

    @Override
    public void onDestroy() {
        super.onDestroy(); // always call the superclass

        // stop method tracing that the activity started during onCreate()
        // onandroid.os.Debug.stopMethodTracing();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game statue

        super.onSaveInstanceState(savedInstanceState); // always called the superclass
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        // Not necessary if they are included in onCreate()
        //score = savedInstanceState.getInt(STATE_SCORE);
        //level = savedInstanceState.getInt(STATE_LEVEL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar
        // automatically handles clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
