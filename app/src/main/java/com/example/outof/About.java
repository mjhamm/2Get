package com.example.outof;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button licenses = findViewById(R.id.licenses_button);
        //Button transparency = findViewById(R.id.trans_button);
        Button feedback = findViewById(R.id.feedback_button);
        Button help = findViewById(R.id.help_button);
        Button privacy = findViewById(R.id.privacy_button);
        Button tou = findViewById(R.id.tou_button);

        /*transparency.setOnClickListener(click -> {
            startActivity(new Intent(this, TransparencyStatement.class));
        });*/

        licenses.setOnClickListener(click -> {
            OssLicensesMenuActivity.setActivityTitle("Licenses");
            startActivity(new Intent(this, OssLicensesMenuActivity.class));
        });

        feedback.setOnClickListener(click -> startActivity(new Intent(this, Feedback.class)));

        help.setOnClickListener(click -> startActivity(new Intent(this, Help.class)));

        privacy.setOnClickListener(click -> startActivity(new Intent(this, PrivacyStatement.class)));

        tou.setOnClickListener(click -> startActivity(new Intent(this, TermsAndConditions.class)));
    }

    public void close_about(View view) {
        finish();
    }
}
