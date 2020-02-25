package com.app.toget;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Loads the Splashscreen activity on startup and then loads mainactivity
        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        finish();
    }
}
