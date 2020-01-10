package com.example.outof;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AddCustomItem extends AppCompatActivity{

    private EditText itemName;
    private Spinner itemCategory;

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

        itemName = findViewById(R.id.customItemName_EditText);
        //itemCategory = findViewById(R.id.customItemCategory_spinner);

        /*String[] arraySpinner = new String[] {
                "Baking", "Beverages", "Breads", "Canned Goods", "Condiments", "Dairy", "Deli", "Frozen Foods",
                "Meat & Fish", "Pasta", "Produce"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategory.setAdapter(adapter);*/

        /*DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * .95), (int)(height * .45));
        getWindow().setGravity(Gravity.CENTER);*/
    }
}
