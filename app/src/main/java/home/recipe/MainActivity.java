package home.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import home.recipe.entity.Recipe;
import home.recipe.service.DownloadResultReceiver;
import home.recipe.service.DownloadService;


public class MainActivity extends Activity implements DownloadResultReceiver.Receiver {
    private ListView listView = null;
    private ListViewAdapter arrayAdapter = null;
    private DownloadResultReceiver mReceiver;
    private final String defaultURL = "http://food2fork.com/api/search?key=4416dc74c59eb93a2a5c2f0b581e44cd&q=";
    private final String page = "&page=";
    private String searchURL = "";
    private EditText search;
    private Intent intent;
    private Button previous;
    private Button next;
    private int pageNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);
        listView = (ListView) findViewById(R.id.listview);
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        intent = new Intent(Intent.ACTION_SYNC, null, this, DownloadService.class);
        search = (EditText) findViewById(R.id.search);
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    pageNumber = 1;
                    StringBuilder builder = new StringBuilder().append(defaultURL).append(search.getText());
                    searchURL = builder.toString();
                    Log.v("LOG URL", searchURL);
                    intent.putExtra("url", searchURL);
                    intent.putExtra("receiver", mReceiver);
                    startService(intent);
                    return true;
                }
                return false;
            }
        });
        previous = (Button) findViewById(R.id.previous_btn);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageNumber>1){
                    pageNumber--;
                    StringBuilder builder = new StringBuilder().append(searchURL).append(page).append(pageNumber);
                    Log.v("LOG URL", builder.toString());
                    intent.putExtra("url", builder.toString());
                    intent.putExtra("receiver", mReceiver);
                    startService(intent);
                }

            }
        });
        next = (Button) findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    pageNumber++;
                    StringBuilder builder = new StringBuilder().append(searchURL).append(page).append(pageNumber);
                    Log.v("LOG URL", builder.toString());
                    intent.putExtra("url", builder.toString());
                    intent.putExtra("receiver", mReceiver);
                    startService(intent);
            }
        });


    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case DownloadService.STATUS_RUNNING:
                setProgressBarIndeterminateVisibility(true);
                break;
            case DownloadService.STATUS_FINISHED:
                setProgressBarIndeterminateVisibility(false);
                ArrayList<Recipe> results = resultData.getParcelableArrayList("result");
                arrayAdapter = new ListViewAdapter(MainActivity.this, results);
                listView.setAdapter(arrayAdapter);
                break;
            case DownloadService.STATUS_ERROR:
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
