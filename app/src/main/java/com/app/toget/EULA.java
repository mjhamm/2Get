package com.app.toget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class EULA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eula);

        WebView webView = findViewById(R.id.eula_webView);
        webView.loadUrl("file:///android_asset/eula.html");
    }
}
