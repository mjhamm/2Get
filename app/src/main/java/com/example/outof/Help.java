package com.example.outof;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Finding all ImageViews in help.xml
        ImageView help_image_1 = findViewById(R.id.help_image_1); //1
        ImageView help_image_2 = findViewById(R.id.help_image_2); //6
        ImageView help_image_3 = findViewById(R.id.help_image_3); //2
        ImageView help_image_4 = findViewById(R.id.help_image_4); //4
        ImageView help_image_5 = findViewById(R.id.help_image_5); //7
        ImageView help_image_6 = findViewById(R.id.help_image_6); //5
        ImageView help_image_7 = findViewById(R.id.help_image_7); //8

        // Using Glide to insert image into ImageView
        Glide.with(this).load(getDrawable(R.drawable.help_1)).into(help_image_1).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_6)).into(help_image_2).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_2)).into(help_image_3).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_4)).into(help_image_4).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_7)).into(help_image_5).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_5)).into(help_image_6).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_8)).into(help_image_7).clearOnDetach();
    }
}
