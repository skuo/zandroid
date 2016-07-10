package org.zandroid.zandroid;

import android.app.Fragment;
import android.content.Intent;
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

public class DisplayMessageActivity extends AppCompatActivity {

    static final String STATE_SCORE = "playerScore";
    static final String STATE_LEVEL = "ployerLevel";

    private int score = 1;
    private int level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(STATE_SCORE);
            level = savedInstanceState.getInt(STATE_LEVEL);
        }

        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        String hello = getResources().getString(R.string.hello_world);
        textView.setText("[" + level + "," + score + "] "
                + hello + " ==> " + message);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.content);
        layout.addView(textView);
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
        savedInstanceState.putInt(STATE_SCORE, score+1);
        savedInstanceState.putInt(STATE_LEVEL, level+1);

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_display_message, container, false);
            return rootView;
        }
    }
}
