package home.recipe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import home.recipe.rest.JSONParser;


public class MainActivity extends Activity {
    private JSONObject jsonobject;
    private JSONArray jsonarray;
    private ListView listview;
    private ListViewAdapter adapter;
    private ProgressDialog mProgressDialog;
    private ArrayList<HashMap<String, String>> arraylist;
    private boolean loadingMore = false;

    private EditText search;

    //URL to get JSON Array
    private static String url = "http://food2fork.com/api/search?key=4416dc74c59eb93a2a5c2f0b581e44cd&q=";

    //JSON Node Names
     static final String RECIPES_TAG = "recipes";
     static final String TITLE_TAG = "title";
     static final String PUBLISHER_URL_TAG = "publisher_url";
     static final String IMAGE_URL_TAG = "image_url";
     static final String F2F_URL_TAG = "f2f_url";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.main);
        addKeyListener();


    }
    public void addKeyListener() {

        // get edittext component
        search = (EditText) findViewById(R.id.search);

        // add a keylistener to keep track user input
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                // if keydown and "enter" is pressed
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    StringBuilder builder = new StringBuilder().append(url).append(search.getText());
                    url = builder.toString();
                    Log.v("LOG URL",url);
                    new DownloadJSON().execute();
                    return true;

                }

                return false;
            }
        });
    }

    // DownloadJSON AsyncTask
    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("getting the recipes");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create an array
            arraylist = new ArrayList<HashMap<String, String>>();
            // Retrieve JSON Objects from the given URL address
            jsonobject = JSONParser.getJSONFromUrl(url);

            try {
                // Locate the array name in JSON
                jsonarray = jsonobject.getJSONArray(RECIPES_TAG);

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);
                    // Retrive JSON Objects
                    map.put(F2F_URL_TAG,jsonobject.getString(F2F_URL_TAG));
                    map.put(TITLE_TAG, jsonobject.getString(TITLE_TAG));
                    map.put(PUBLISHER_URL_TAG, jsonobject.getString(PUBLISHER_URL_TAG));
                    map.put(IMAGE_URL_TAG, jsonobject.getString(IMAGE_URL_TAG));
                    // Set the JSON Objects into the array
                    arraylist.add(map);
                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.listview);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(MainActivity.this, arraylist);
            // Set the adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }
}