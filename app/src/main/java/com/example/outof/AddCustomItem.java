package com.example.outof;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddCustomItem extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom_item);
        setTheme(R.style.customItem);
    }
}
