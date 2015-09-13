package home.recipe;

/**
 * Created by greg on 12.09.15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;


public class SingleItemActivity extends Activity {

    private WebView mWebView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleitemview);
        Intent intent = getIntent();
        String url = intent.getStringExtra(MainActivity.F2F_URL_TAG);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
    }



}
