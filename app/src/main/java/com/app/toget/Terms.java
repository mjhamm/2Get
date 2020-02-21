package com.app.toget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class Terms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        WebView webView = findViewById(R.id.tou_webView);
        webView.loadUrl("file:///android_asset/terms_and_conditions.html");
    }
}
