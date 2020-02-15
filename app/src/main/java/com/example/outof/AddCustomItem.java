package com.example.outof;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class AddCustomItem extends AppCompatActivity{

    public AddCustomItem() {

    }

    public static AddCustomItem newInstance() {
        return new AddCustomItem();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom_item);
        setTheme(R.style.customItem);

        EditText itemName = findViewById(R.id.customItemName_EditText);
    }
}
