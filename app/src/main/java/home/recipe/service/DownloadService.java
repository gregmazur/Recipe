package home.recipe.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import home.recipe.MainActivity;
import home.recipe.entity.Recipe;


public class DownloadService extends IntentService {


    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;


    private static final String TAG = "DownloadService";
    private static final String RECIPES_TAG = "recipes";
    static final String TITLE_TAG = "title";
    static final String PUBLISHER_TAG = "publisher";
    static final String IMAGE_URL_TAG = "image_url";
    static final String F2F_URL_TAG = "f2f_url";


    public DownloadService() {
        super(DownloadService.class.getName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");

        Bundle bundle = new Bundle();


        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {
                ArrayList<Recipe> results = downloadData(url);


                /* Sending result back to activity */
                if (null != results && results.size() > 0) {
                    bundle.putParcelableArrayList("result", results);
                    receiver.send(STATUS_FINISHED, bundle);
                }
            } catch (Exception e) {


                /* Sending error message back to activity */
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        }
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }


    private ArrayList<Recipe> downloadData(String requestUrl) throws IOException, DownloadException {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        /* forming th java.net.URL object */
        URL url = new URL(requestUrl);
        urlConnection = (HttpURLConnection) url.openConnection();
        /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");
        /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");
        /* for Get request */
        urlConnection.setRequestMethod("GET");
        int statusCode = urlConnection.getResponseCode();
        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            String response = convertInputStreamToString(inputStream);
            ArrayList<Recipe> results = parseResult(response);
            return results;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }


    private String convertInputStreamToString(InputStream inputStream) throws IOException {


        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";


        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }


            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }


        return result;
    }


    private ArrayList<Recipe> parseResult(String result) {


        ArrayList<Recipe> recipesList = new ArrayList<>();
        try {
            JSONObject jsonobject = new JSONObject(result);
            JSONArray jsonarray = jsonobject.getJSONArray(RECIPES_TAG);
            for (int i = 0; i < jsonarray.length(); i++) {
                Recipe recipe = new Recipe();
                jsonobject = jsonarray.getJSONObject(i);
                recipe.setF2f_url(jsonobject.getString(F2F_URL_TAG));
                recipe.setTitle(jsonobject.getString(TITLE_TAG));
                recipe.setPublisherName(jsonobject.getString(PUBLISHER_TAG));
                recipe.setImage_url(jsonobject.getString(IMAGE_URL_TAG));
                recipesList.add(recipe);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return recipesList;
    }

    public class DownloadException extends Exception {


        public DownloadException(String message) {
            super(message);
        }


        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}