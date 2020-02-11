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
        Button transparency = findViewById(R.id.trans_button);

        transparency.setOnClickListener(click -> {
            startActivity(new Intent(this, TransparencyStatement.class));
        });

        licenses.setOnClickListener(click -> {
            OssLicensesMenuActivity.setActivityTitle("Licenses");
            startActivity(new Intent(this, OssLicensesMenuActivity.class));
        });
    }

    public void close_about(View view) {
        finish();
    }
}
