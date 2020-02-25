package com.app.toget;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Licenses Button
        Button licenses = findViewById(R.id.licenses_button);
        //Feedback Button
        Button feedback = findViewById(R.id.feedback_button);
        //Help Button
        Button help = findViewById(R.id.help_button);
        //Privacy Policy Button
        Button privacy = findViewById(R.id.privacy_button);
        //Terms & Conditions Button
        Button tou = findViewById(R.id.tou_button);
        //End User License Agreement Button
        Button eula = findViewById(R.id.eula_button);

        //Launch android licenses activity
        licenses.setOnClickListener(click -> startActivity(new Intent(this, Licenses.class)));

        //Start Feedback
        feedback.setOnClickListener(click -> startActivity(new Intent(this, Feedback.class)));

        //Start Help
        help.setOnClickListener(click -> startActivity(new Intent(this, Help.class)));

        //Start Privacy Policy
        privacy.setOnClickListener(click -> startActivity(new Intent(this, Privacy.class)));

        //Start Terms & Conditions
        tou.setOnClickListener(click -> startActivity(new Intent(this, Terms.class)));

        //Start EULA
        eula.setOnClickListener(click -> startActivity(new Intent(this, EULA.class)));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
