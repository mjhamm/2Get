package com.app.toget;

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
        ImageView help_image_8 = findViewById(R.id.help_image_8); //3

        // Using Glide to insert image into ImageView
        Glide.with(this).load(getDrawable(R.drawable.help_1)).dontTransform().into(help_image_1).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_6)).dontTransform().into(help_image_2).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_3)).dontTransform().into(help_image_8).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_2)).dontTransform().into(help_image_3).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_4)).dontTransform().into(help_image_4).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_7)).dontTransform().into(help_image_5).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_5)).dontTransform().into(help_image_6).clearOnDetach();
        Glide.with(this).load(getDrawable(R.drawable.help_8)).dontTransform().into(help_image_7).clearOnDetach();
    }
}
