package com.example.testapp.layouts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testapp.R;

public class WebActivity extends AppCompatActivity {
    WebView webView;
    EditText url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        url = findViewById(R.id.url);

        url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    Log.i(MainActivity.TAG_APP, "Search from the keyboard");
                    webView.loadUrl("http://www.google.com/search?q="+url.getText().toString());
                }
                return true;
            }
        });

    }
}