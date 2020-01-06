package com.example.outof;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class MakeListActivity extends Fragment implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "LOG";

    private ListView mListView;
    private ArrayList<MakeListItem> makeItems;
    private Context mContext;
    private ImageButton addCustomItem;
    private MakeListAdapter makeListAdapter;

    public MakeListActivity() {

    }

    public static MakeListActivity newInstance() {
        return new MakeListActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.make_list, container,false);
        makeItems = new ArrayList<>();
        displayMakeList();
        mListView = view.findViewById(R.id.makeList);
        addCustomItem = view.findViewById(R.id.addCustomItem_button);
        makeListAdapter = new MakeListAdapter(makeItems, mContext);
        mListView.setAdapter(makeListAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView.setOnItemClickListener((parent, view1, position, id) -> {
            CheckBox checkBox = view1.findViewById(R.id.makeList_item_checkbox);
            checkBox.performClick();

            int pos = mListView.getPositionForView(view1);
            if (pos != ListView.INVALID_POSITION) {
                MakeListItem makeItem = makeItems.get(pos);
                if (makeItem.isSelected()) {
                    makeItem.setSelected(false);
                } else {
                    makeItem.setSelected(true);
                }

                //Toast.makeText(mContext, "Clicked on Item: " + makeItem.getItemName() + ". State: is " + makeItem.isSelected(), Toast.LENGTH_SHORT).show();
            }
        });

        addCustomItem.setOnClickListener(v -> {
            Intent startCustomItemIntent = new Intent(mContext, AddCustomItem.class);
            startActivity(startCustomItemIntent);
        });
    }

    public void clear() {
        if (makeItems != null) {
            for (MakeListItem item : makeItems) {
                item.setSelected(false);
            }
        } else {
            Log.e(TAG, "Array is Empty");
        }
        makeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    public void displayMakeList() {
        //Baking
        makeItems.add(new MakeListItem("Sugar", false));
        makeItems.add(new MakeListItem("Flour", false));
        makeItems.add(new MakeListItem("Vanilla", false));
        makeItems.add(new MakeListItem("Yeast", false));
        makeItems.add(new MakeListItem("Pancake Mix", false));
        //Beverages
        makeItems.add(new MakeListItem("Water", false));
        makeItems.add(new MakeListItem("Juice", false));
        makeItems.add(new MakeListItem("Soda", false));
        makeItems.add(new MakeListItem("Coffee", false));
        makeItems.add(new MakeListItem("Tea", false));
        //Bread
        makeItems.add(new MakeListItem("Bagels", false));
        makeItems.add(new MakeListItem("Sandwich", false));
        makeItems.add(new MakeListItem("Tortilla", false));
        makeItems.add(new MakeListItem("Muffins", false));
        makeItems.add(new MakeListItem("Pitas", false));
        makeItems.add(new MakeListItem("Bulky Rolls", false));
        //Canned Goods
        makeItems.add(new MakeListItem("Fruit", false));
        makeItems.add(new MakeListItem("Vegetables", false));
        makeItems.add(new MakeListItem("Soup", false));
        makeItems.add(new MakeListItem("Tuna", false));
        makeItems.add(new MakeListItem("Tomato Sauce", false));
        makeItems.add(new MakeListItem("Pumpkin", false));
        //Condiments
        makeItems.add(new MakeListItem("Ketchup", false));
        makeItems.add(new MakeListItem("Mustard", false));
        makeItems.add(new MakeListItem("Relish", false));
        makeItems.add(new MakeListItem("Barbecue", false));
        makeItems.add(new MakeListItem("Pickles", false));
        makeItems.add(new MakeListItem("Mayonnaise", false));
        makeItems.add(new MakeListItem("Peanut Butter", false));
        makeItems.add(new MakeListItem("Jelly", false));
        //Dairy
        makeItems.add(new MakeListItem("Milk", false));
        makeItems.add(new MakeListItem("Cheese", false));
        makeItems.add(new MakeListItem("Butter", false));
        makeItems.add(new MakeListItem("Yogurt", false));
        //Deli
        makeItems.add(new MakeListItem("Cheese", false));
        makeItems.add(new MakeListItem("Turkey", false));
        makeItems.add(new MakeListItem("Ham", false));
        makeItems.add(new MakeListItem("Roast Beef", false));
        makeItems.add(new MakeListItem("Salad", false));
        //Frozen Foods
        makeItems.add(new MakeListItem("Pizza", false));
        makeItems.add(new MakeListItem("Vegetables", false));
        makeItems.add(new MakeListItem("Ice Cream", false));
        makeItems.add(new MakeListItem("Waffles", false));
        makeItems.add(new MakeListItem("Meals", false));
        makeItems.add(new MakeListItem("Potatoes", false));
        //Meat & Fish
        makeItems.add(new MakeListItem("Bacon", false));
        makeItems.add(new MakeListItem("Beef", false));
        makeItems.add(new MakeListItem("Fish", false));
        makeItems.add(new MakeListItem("Pork", false));
        makeItems.add(new MakeListItem("Poultry", false));
        makeItems.add(new MakeListItem("Sausage", false));
        //Pasta
        makeItems.add(new MakeListItem("Lasagna", false));
        makeItems.add(new MakeListItem("Macaroni", false));
        makeItems.add(new MakeListItem("Shells", false));
        makeItems.add(new MakeListItem("Spaghetti", false));
        //Produce
        makeItems.add(new MakeListItem("Apples", false));
        makeItems.add(new MakeListItem("Bananas", false));
        makeItems.add(new MakeListItem("Berries", false));
        makeItems.add(new MakeListItem("Grapes", false));
        makeItems.add(new MakeListItem("Oranges", false));
        makeItems.add(new MakeListItem("Melons", false));
        makeItems.add(new MakeListItem("Avocados", false));
        makeItems.add(new MakeListItem("Broccoli", false));
        makeItems.add(new MakeListItem("Carrots", false));
        makeItems.add(new MakeListItem("Cucumber", false));
        makeItems.add(new MakeListItem("Garlic", false));
        makeItems.add(new MakeListItem("Lettuce", false));
        makeItems.add(new MakeListItem("Peppers", false));
        makeItems.add(new MakeListItem("Potatoes", false));
        makeItems.add(new MakeListItem("Tomatoes", false));
        makeItems.add(new MakeListItem("Onions", false));
        makeItems.add(new MakeListItem("Mushrooms", false));
        makeItems.add(new MakeListItem("Zucchini", false));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
