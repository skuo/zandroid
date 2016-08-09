package org.zandroid.zandroid;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "HttpExample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own network action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        EditText editText = (EditText) findViewById(R.id.edit_networkUrl);
        editText.setText(message);
    }

    // called when the user clicks the Load button
    public void load(View view) {
        // Get the URL the the text field
        EditText editText = (EditText) findViewById(R.id.edit_networkUrl);
        String url = editText.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(url);
        } else {
            Toast.makeText(getApplicationContext(),"No network connection avaialble.",Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * Use AnsyncTask to create a task away from the main UI thread.  This task takes a
     * URL string and uses it to create an HttpUrlConnection.  Once the connection
     * has been established, the AsyncTask downloads the contents of the webpage as
     * an InputStream.  Finally, the InputStream is converted into a string, which is
     * displayed in the UI by the AsyncTask's onPostExecute method.
     */
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params come from the execute() call: params[0] is the url
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page.  URL may be invalid";
            }
        }

        // onPoastExecute displays the results of the AsyncTask
        @Override
        protected void onPostExecute(String result) {
            TextView textView = (TextView) findViewById(R.id.networkTextView);
            textView.setText(result);
        }

        /*
         * Given a URL, establishes an HttpUrlConnectino and retrieves
         * the webpage content as InputStream, which it returns as a string.
         */
        private String downloadUrl(String urlStr) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved webpage content
            int len = 500;

            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); // milliseconds
                conn.setConnectTimeout(15000); // milliseconds
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // starts the query
                conn.connect();
                int responseCode = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response is: " + responseCode);
                is = conn.getInputStream();

                // convert the InputStream into a string
                String contentAsString = readIt(is, len);
                return contentAsString;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    /**
     * Read an InputStream and converts it to a String
     */
    public String readIt(InputStream is, int len) throws IOException, UnsupportedEncodingException{
        Reader reader = new InputStreamReader(is, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
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
