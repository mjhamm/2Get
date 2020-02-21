package com.app.toget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class Privacy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_statement);

        WebView webView = findViewById(R.id.privacy_webView);
        webView.loadUrl("file:///android_asset/privacy_policy.html");
    }
}
