package org.zandroid.zandroid;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ShareActionProvider;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class MyActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "org.zandorid.zandroid.MESSAGE";
    private static final int PICK_CONTACT_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the user interface layout for this activity
        // The layout file is defined in the project res/layout/activity_my.xml
        setContentView(R.layout.activity_my);

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

        // Set shared preference
        SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
        editor.putBoolean("notification", true);
        editor.putString("school","usc");
        editor.apply();

        // Handle incoming content
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        EditText editText = (EditText) findViewById(R.id.edit_incoming_content);
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                editText.setText("**> Handle text content");
            } else if (type.startsWith("image/")) {
                editText.setText("**> Handle single image");
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                editText.setText("**> Handle multiple images");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);

        // locate MenuItem with ShareActionProvider
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.menu_item_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "text to share");
            startActivity(Intent.createChooser(shareIntent,"Share via"));
        }

        return super.onOptionsItemSelected(item);
    }

    // -----------------------------------------------
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    // -----------------------------------------------
    /** Called when the user clicks the Map button */
    public void mapAddress(View view) throws UnsupportedEncodingException {
        EditText editText = (EditText) findViewById(R.id.edit_address);
        String address = editText.getText().toString();
        String query = URLEncoder.encode(address,"utf-8");
        //Uri location = Uri.parse("geo:0,0?q=1600+Amphiteatre+Parkway,+Mountain+View,+California");
        Uri location = Uri.parse("geo:0,0?q=" + query);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

        // verify it resolves
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe) {
            // Create an intent to show chooser
            String title = getResources().getString(R.string.map_chooser_title);
            Intent mapChooser = Intent.createChooser(mapIntent, title);
            startActivity(mapChooser);
        }
    }

    // -----------------------------------------------
    /** Called when the user clicks the Pick button */
    public void pickContact(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        // Show user only contacts w/ phone number
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = resultIntent.getData();
                // We only need to NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be calle from a separate thread to avoid blocking
                // your app's UI thread. This code does not do that.
                // Consider using CursorLoader to perform the query
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                EditText editText = (EditText) findViewById(R.id.edit_contact);
                editText.setText("contact's phone number: " + number);
            }
        }
    }

    // -----------------------------------------------
    /** Called when the user clicks the Request button */
    public void requestPermission(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                // Show rational to user
                Snackbar.make(view, R.string.permission_read_contacts_rational,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(MyActivity.this,
                                        new String[]{Manifest.permission.READ_CONTACTS},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        })
                        .show();
            } else {
                // request permission directly
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            EditText editText = (EditText) findViewById(R.id.edit_request);
            editText.setText("Read contacts permission has already been granted");

        }
    }

    /** Handle permission request response */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                EditText editText = (EditText) findViewById(R.id.edit_request);
                // if request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    editText.setText("Read contacts permission granted");
                } else {
                    editText.setText("Read contacts permission denied");
                }
                return;
            }
        }
    }

    // -----------------------------------------------
    /**
     * Switching between two fragments: article and book
     * @param view
     */
    public void selectFrag(View view) {
        Fragment fr;

        if (view == findViewById(R.id.book_fragment_button)) {
            fr = new BookFragment();
        } else {
            fr = new ArticleFragment();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_place, fr);
        ft.commit();
    }
}
