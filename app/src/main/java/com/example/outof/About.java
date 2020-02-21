package com.example.outof;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

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

        //Launch android licenses activity
        //licenses.setOnClickListener(click -> startActivity(new Intent(this, Licenses.class)));
        licenses.setOnClickListener(click -> {
            OssLicensesMenuActivity.setActivityTitle("Open Source Libraries");
            startActivity(new Intent(this, OssLicensesMenuActivity.class));
        });

        //Start Feedback
        feedback.setOnClickListener(click -> startActivity(new Intent(this, Feedback.class)));

        //Start Help
        help.setOnClickListener(click -> startActivity(new Intent(this, Help.class)));

        //Start Privacy Policy
        privacy.setOnClickListener(click -> startActivity(new Intent(this, Privacy.class)));

        //Start Terms & Conditions
        tou.setOnClickListener(click -> startActivity(new Intent(this, Terms.class)));
    }

    //Make sure activities are destroyed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
