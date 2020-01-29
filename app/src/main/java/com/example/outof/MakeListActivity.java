package com.example.outof;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MakeListActivity extends Fragment implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "LOG";

    private Context mContext;
    private ImageButton addCustomItem;
    private MakeListFragmentListener listener;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private ArrayList<String> expandableListTitle;
    private HashMap<String, ArrayList<MakeListItem>> expandableListDetail;
    private ConstraintLayout mAddItemParent;
    private DatabaseHelper myDB;
    private int groupExpandedInt = 0;
    private boolean groupExpanded = false;
    private int itemCheckedInt = 0;
    private boolean itemChecked = false;

    public MakeListActivity() {

    }

    public static MakeListActivity newInstance() {
        return new MakeListActivity();
    }

    public interface MakeListFragmentListener {
        void onSelectionASent(String selection);
        void onSelectionBSent(String selection);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.make_list, container,false);

        mAddItemParent = view.findViewById(R.id.addItemParent);
        addCustomItem = view.findViewById(R.id.addCustomItem_button);
        expandableListView = view.findViewById(R.id.makeList);
        expandableListDetail = getData();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(mContext, expandableListTitle, expandableListDetail);

        myDB = new DatabaseHelper(mContext);

        Cursor childData = myDB.getListContents_Children();
        if (childData.getCount() == 0) {
            addDataToDb_Group();
            addDataToDb_Children();
        } else {
            for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {

            }
        }

        /*for (int k = 0; k < expandableListAdapter.getGroupCount(); k++) {
            Cursor childData = myDB.getListContents_Children(expandableListTitle.get(k));
            if (childData.getCount() == 0) {
                Toast.makeText(mContext, "Database Empty", Toast.LENGTH_SHORT).show();
                break;
            } else {
                while(childData.moveToNext()) {
                    if (childData.getInt(2) == 0) {
                        itemChecked = false;
                    } else {
                        itemChecked = true;
                    }
                    for (int j = 0; j < expandableListAdapter.getChildrenCount(k); j++) {
                        MakeListItem makeListItem = (MakeListItem) expandableListAdapter.getChild(k,j);
                        CheckBox checkBox = view.findViewById(R.id.makeList_item_checkbox);
                        if (itemChecked) {
                            makeListItem.setSelected(true);
                            checkBox.setChecked(true);
                        }
                    }
                }
            }
            childData.close();
        }*/

        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(groupPosition -> {
            Toast.makeText(mContext, expandableListTitle.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
            groupExpandedInt = 1;
        });

        expandableListView.setOnGroupCollapseListener(groupPosition -> {
            groupExpandedInt = 0;
            Toast.makeText(mContext, expandableListTitle.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            MakeListItem makeListItem = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
            CheckBox itemCheckBox = v.findViewById(R.id.makeList_item_checkbox);

            if (makeListItem.isSelected()) {
                itemCheckedInt = 0;
                makeListItem.setSelected(false);
                itemCheckBox.setChecked(false);
                String selection = makeListItem.getItemName();
                listener.onSelectionBSent(selection);
                Log.e(TAG, selection);
            } else {
                itemCheckedInt = 1;
                makeListItem.setSelected(true);
                itemCheckBox.setChecked(true);
                String selection = makeListItem.getItemName();
                listener.onSelectionASent(selection);
                Log.e(TAG, selection);
            }
            return true;
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MakeListFragmentListener) {
            listener = (MakeListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement MakeListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void clear() {
        for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
            for (int j = 0; j < expandableListAdapter.getChildrenCount(i); j++) {

                MakeListItem makeListItem = (MakeListItem) expandableListAdapter.getChild(i,j);
                CheckBox checkBox = getView().findViewById(R.id.makeList_item_checkbox);
                if (makeListItem.isSelected()) {
                    makeListItem.setSelected(false);
                    checkBox.setChecked(false);
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addCustomItem.setOnClickListener(v -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle("Add a Custom Item");
            alertDialogBuilder.setCancelable(true);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            final View dialogView = inflater.inflate(R.layout.activity_add_custom_item, mAddItemParent);
            final EditText editText = dialogView.findViewById(R.id.customItemName_EditText);

            alertDialogBuilder.setView(dialogView);
            editText.requestFocus();

            alertDialogBuilder.setPositiveButton("Add Item", (dialog, which) -> {
                if (!editText.getText().toString().isEmpty()) {
                    String customItem = editText.getText().toString();
                    listener.onSelectionASent(customItem);
                    //Send item to ViewList and update dataset
                    Toast.makeText(mContext, "Added " + editText.getText().toString() + " to your list.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "You must enter an item to add it to your list.", Toast.LENGTH_SHORT).show();
                }
            });

            //alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myDB.close();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    private void addDataToDb_Group() {
        //Add all Groups to Database
    }

    public void addDataToDb_Children() {
        //Add all Children to Database

        //Baby & Childcare
        myDB.addChild("Baby & Childcare", "Baby Food", 0);
        myDB.addChild("Baby & Childcare", "Diapers", 0);
        myDB.addChild("Baby & Childcare", "Formula", 0);
        myDB.addChild("Baby & Childcare", "Wipes", 0);

        //Baking
        myDB.addChild("Baking", "Flour", 0);
        myDB.addChild("Baking", "Pancake Mix", 0);
        myDB.addChild("Baking", "Sugar", 0);
        myDB.addChild("Baking", "Vanilla", 0);
        myDB.addChild("Baking", "Yeast", 0);

        //Beverages
        myDB.addChild("Beverages", "Coffee", 0);
        myDB.addChild("Beverages", "Juice", 0);
        myDB.addChild("Beverages", "Soda", 0);
        myDB.addChild("Beverages", "Tea", 0);
        myDB.addChild("Beverages", "Water", 0);

        //Bread
        myDB.addChild("Bread", "Bagels", 0);
        myDB.addChild("Bread", "Bulky Rolls", 0);
        myDB.addChild("Bread", "Muffins", 0);
        myDB.addChild("Bread", "Pitas", 0);
        myDB.addChild("Bread", "Sandwich", 0);
        myDB.addChild("Bread", "Tortilla", 0);

        //Breakfast & Cereal
        myDB.addChild("Breakfast & Cereal", "Breakfast Bars", 0);
        myDB.addChild("Breakfast & Cereal", "Cold Cereal", 0);
        myDB.addChild("Breakfast & Cereal", "Granola", 0);
        myDB.addChild("Breakfast & Cereal", "Hot Cereal", 0);
        myDB.addChild("Breakfast & Cereal", "Oatmeal", 0);

        //Canned Goods
        myDB.addChild("Canned Goods", "Fruit", 0);
        myDB.addChild("Canned Goods", "Pumpkin", 0);
        myDB.addChild("Canned Goods", "Soup", 0);
        myDB.addChild("Canned Goods", "Tomato Sauce", 0);
        myDB.addChild("Canned Goods", "Tuna", 0);
        myDB.addChild("Canned Goods", "Vegetables", 0);

        //Condiments
        myDB.addChild("Condiments", "Jelly", 0);
        myDB.addChild("Condiments", "Ketchup", 0);
        myDB.addChild("Condiments", "Mayonnaise", 0);
        myDB.addChild("Condiments", "Mustard", 0);
        myDB.addChild("Condiments", "Peanut Butter", 0);
        myDB.addChild("Condiments", "Pickles", 0);
        myDB.addChild("Condiments", "Relish", 0);

        //Dairy
        myDB.addChild("Dairy", "Butter",0);
        myDB.addChild("Dairy", "Cheese",0);
        myDB.addChild("Dairy", "Milk",0);
        myDB.addChild("Dairy", "Sour Cream",0);
        myDB.addChild("Dairy", "Yogurt",0);

        //Deli
        myDB.addChild("Deli", "Cheese", 0);
        myDB.addChild("Deli", "Ham", 0);
        myDB.addChild("Deli", "Roast Beef", 0);
        myDB.addChild("Deli", "Salad", 0);
        myDB.addChild("Deli", "Turkey", 0);

        //Frozen Foods
        myDB.addChild("Frozen Foods", "Ice Cream", 0);
        myDB.addChild("Frozen Foods", "Meals", 0);
        myDB.addChild("Frozen Foods", "Pizza", 0);
        myDB.addChild("Frozen Foods", "Potatoes", 0);
        myDB.addChild("Frozen Foods", "Vegetables", 0);
        myDB.addChild("Frozen Foods", "Waffles", 0);

        //Health & Beauty
        myDB.addChild("Health & Beauty", "Bandages", 0);
        myDB.addChild("Health & Beauty", "Cold Medicine", 0);
        myDB.addChild("Health & Beauty", "Conditioner", 0);
        myDB.addChild("Health & Beauty", "Deodorant", 0);
        myDB.addChild("Health & Beauty", "Floss", 0);
        myDB.addChild("Health & Beauty", "Lotion", 0);
        myDB.addChild("Health & Beauty", "Pain Relievers", 0);
        myDB.addChild("Health & Beauty", "Razors", 0);
        myDB.addChild("Health & Beauty", "Shampoo", 0);
        myDB.addChild("Health & Beauty", "Shaving Cream", 0);
        myDB.addChild("Health & Beauty", "Soap", 0);
        myDB.addChild("Health & Beauty", "Toothpaste", 0);
        myDB.addChild("Health & Beauty", "Vitamins", 0);

        //Household
        myDB.addChild("Household", "Batteries", 0);
        myDB.addChild("Household", "Glue", 0);
        myDB.addChild("Household", "Light Bulbs", 0);
        myDB.addChild("Household", "Tape", 0);

        //Laundry, Paper & Cleaning
        myDB.addChild("Laundry, Paper & Cleaning", "Aluminum Foil", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Bleach", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Dishwashing Liquid", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Disinfectant Wipes", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Garbage Bags", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Glass Cleaner", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Hand Soap", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Household Cleaner", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Laundry Detergent", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Laundry Softener", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Paper Towels", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Plastic Bags", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Plastic Wrap", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Sponges", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Tissues", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Toilet Paper", 0);
        myDB.addChild("Laundry, Paper & Cleaning", "Trash Bags", 0);

        //Meat & Fish
        myDB.addChild("Meat & Fish", "Bacon", 0);
        myDB.addChild("Meat & Fish", "Beef", 0);
        myDB.addChild("Meat & Fish", "Fish", 0);
        myDB.addChild("Meat & Fish", "Pork", 0);
        myDB.addChild("Meat & Fish", "Poultry", 0);
        myDB.addChild("Meat & Fish", "Sausage", 0);

        //Pet Items
        myDB.addChild("Pet Items", "Cat Food", 0);
        myDB.addChild("Pet Items", "Cat Litter", 0);
        myDB.addChild("Pet Items", "Dog Food", 0);

        //Produce
        myDB.addChild("Produce", "Apples", 0);
        myDB.addChild("Produce", "Avocados", 0);
        myDB.addChild("Produce", "Bananas", 0);
        myDB.addChild("Produce", "Berries", 0);
        myDB.addChild("Produce", "Broccoli", 0);
        myDB.addChild("Produce", "Carrots", 0);
        myDB.addChild("Produce", "Cucumber", 0);
        myDB.addChild("Produce", "Garlic", 0);
        myDB.addChild("Produce", "Grapes", 0);
        myDB.addChild("Produce", "Oranges", 0);
        myDB.addChild("Produce", "Lettuce", 0);
        myDB.addChild("Produce", "Melons", 0);
        myDB.addChild("Produce", "Mushrooms", 0);
        myDB.addChild("Produce", "Onions", 0);
        myDB.addChild("Produce", "Peppers", 0);
        myDB.addChild("Produce", "Potatoes", 0);
        myDB.addChild("Produce", "Tomatoes", 0);
        myDB.addChild("Produce", "Zucchini", 0);

        //Rice & Pasta
        myDB.addChild("Rice & Pasta", "Brown Rice", 0);
        myDB.addChild("Rice & Pasta", "Lasagna", 0);
        myDB.addChild("Rice & Pasta", "Macaroni", 0);
        myDB.addChild("Rice & Pasta", "Shells", 0);
        myDB.addChild("Rice & Pasta", "Spaghetti", 0);
        myDB.addChild("Rice & Pasta", "White Rice", 0);

        //Sauces & Oil
        myDB.addChild("Sauces & Oils", "BBQ Sauce",0);
        myDB.addChild("Sauces & Oils", "Maple Syrup",0);
        myDB.addChild("Sauces & Oils", "Oil",0);
        myDB.addChild("Sauces & Oils", "Salad Dressing",0);
        myDB.addChild("Sauces & Oils", "Soy Sauce",0);
        myDB.addChild("Sauces & Oils", "Spaghetti Sauce",0);
        myDB.addChild("Sauces & Oils", "Vinegar",0);

        //Snacks
        myDB.addChild("Snacks","Candy",0);
        myDB.addChild("Snacks","Chips",0);
        myDB.addChild("Snacks","Cookies",0);
        myDB.addChild("Snacks","Crackers",0);
        myDB.addChild("Snacks","Dip/Salsa",0);
        myDB.addChild("Snacks","Nuts",0);
        myDB.addChild("Snacks","Popcorn",0);
        myDB.addChild("Snacks","Pretzels",0);
        myDB.addChild("Snacks","Raisins",0);
        myDB.addChild("Snacks","Snack Bars",0);

        //Spices
        myDB.addChild("Spices", "Basil", 0);
        myDB.addChild("Spices", "Cinnamon", 0);
        myDB.addChild("Spices", "Cumin", 0);
        myDB.addChild("Spices", "Oregano", 0);
        myDB.addChild("Spices", "Pepper", 0);
        myDB.addChild("Spices", "Salt", 0);

        //Vegetarian
        myDB.addChild("Vegetarian", "Almond Milk",0);
        myDB.addChild("Vegetarian", "Hummus",0);
        myDB.addChild("Vegetarian", "Soy Milk",0);
        myDB.addChild("Vegetarian", "Tofu",0);
    }

    public HashMap<String, ArrayList<MakeListItem>> getData() {
        LinkedHashMap<String, ArrayList<MakeListItem>> expandableListDetail = new LinkedHashMap<>();
        //Baby & Childcare
        ArrayList<MakeListItem> baby = new ArrayList<>();
        baby.add(new MakeListItem("Baby Food", itemChecked));
        baby.add(new MakeListItem("Diapers", itemChecked));
        baby.add(new MakeListItem("Formula", itemChecked));
        baby.add(new MakeListItem("Wipes", itemChecked));
        //Baking
        ArrayList<MakeListItem> baking = new ArrayList<>();
        baking.add(new MakeListItem("Flour", itemChecked));
        baking.add(new MakeListItem("Pancake Mix", itemChecked));
        baking.add(new MakeListItem("Sugar", itemChecked));
        baking.add(new MakeListItem("Vanilla", itemChecked));
        baking.add(new MakeListItem("Yeast", itemChecked));
        //Beverages
        ArrayList<MakeListItem> beverages = new ArrayList<>();
        beverages.add(new MakeListItem("Coffee", itemChecked));
        beverages.add(new MakeListItem("Juice", itemChecked));
        beverages.add(new MakeListItem("Soda", itemChecked));
        beverages.add(new MakeListItem("Tea", itemChecked));
        beverages.add(new MakeListItem("Water", itemChecked));
        //Bread
        ArrayList<MakeListItem> bread = new ArrayList<>();
        bread.add(new MakeListItem("Bagels", itemChecked));
        bread.add(new MakeListItem("Bulky Rolls", itemChecked));
        bread.add(new MakeListItem("Muffins", itemChecked));
        bread.add(new MakeListItem("Pitas", itemChecked));
        bread.add(new MakeListItem("Sandwich", itemChecked));
        bread.add(new MakeListItem("Tortilla", itemChecked));
        //Breakfast & Cereal
        ArrayList<MakeListItem> breakfast = new ArrayList<>();
        breakfast.add(new MakeListItem("Breakfast Bars", itemChecked));
        breakfast.add(new MakeListItem("Cold Cereal", itemChecked));
        breakfast.add(new MakeListItem("Granola", itemChecked));
        breakfast.add(new MakeListItem("Hot Cereal", itemChecked));
        breakfast.add(new MakeListItem("Oatmeal", itemChecked));
        //Canned Goods
        ArrayList<MakeListItem> cannedGoods = new ArrayList<>();
        cannedGoods.add(new MakeListItem("Fruit", itemChecked));
        cannedGoods.add(new MakeListItem("Pumpkin", itemChecked));
        cannedGoods.add(new MakeListItem("Soup", itemChecked));
        cannedGoods.add(new MakeListItem("Tomato Sauce", itemChecked));
        cannedGoods.add(new MakeListItem("Tuna", itemChecked));
        cannedGoods.add(new MakeListItem("Vegetables", itemChecked));
        //Condiments
        ArrayList<MakeListItem> condiments = new ArrayList<>();
        condiments.add(new MakeListItem("Jelly", itemChecked));
        condiments.add(new MakeListItem("Ketchup", itemChecked));
        condiments.add(new MakeListItem("Mayonnaise", itemChecked));
        condiments.add(new MakeListItem("Mustard", itemChecked));
        condiments.add(new MakeListItem("Peanut Butter", itemChecked));
        condiments.add(new MakeListItem("Pickles", itemChecked));
        condiments.add(new MakeListItem("Relish", itemChecked));
        //Dairy
        ArrayList<MakeListItem> dairy = new ArrayList<>();
        dairy.add(new MakeListItem("Butter", itemChecked));
        dairy.add(new MakeListItem("Cheese", itemChecked));
        dairy.add(new MakeListItem("Milk", itemChecked));
        dairy.add(new MakeListItem("Sour Cream", itemChecked));
        dairy.add(new MakeListItem("Yogurt", itemChecked));
        //Deli
        ArrayList<MakeListItem> deli = new ArrayList<>();
        deli.add(new MakeListItem("Cheese", itemChecked));
        deli.add(new MakeListItem("Ham", itemChecked));
        deli.add(new MakeListItem("Roast Beef", itemChecked));
        deli.add(new MakeListItem("Salad", itemChecked));
        deli.add(new MakeListItem("Turkey", itemChecked));
        //Frozen Foods
        ArrayList<MakeListItem> frozenFoods = new ArrayList<>();
        frozenFoods.add(new MakeListItem("Ice Cream", itemChecked));
        frozenFoods.add(new MakeListItem("Meals", itemChecked));
        frozenFoods.add(new MakeListItem("Pizza", itemChecked));
        frozenFoods.add(new MakeListItem("Potatoes", itemChecked));
        frozenFoods.add(new MakeListItem("Vegetables", itemChecked));
        frozenFoods.add(new MakeListItem("Waffles", itemChecked));
        //Health & Beauty
        ArrayList<MakeListItem> toiletries = new ArrayList<>();
        toiletries.add(new MakeListItem("Bandages", itemChecked));
        toiletries.add(new MakeListItem("Cold Medicine", itemChecked));
        toiletries.add(new MakeListItem("Conditioner", itemChecked));
        toiletries.add(new MakeListItem("Deodorant", itemChecked));
        toiletries.add(new MakeListItem("Floss", itemChecked));
        toiletries.add(new MakeListItem("Lotion", itemChecked));
        toiletries.add(new MakeListItem("Pain Relievers", itemChecked));
        toiletries.add(new MakeListItem("Razors", itemChecked));
        toiletries.add(new MakeListItem("Shampoo", itemChecked));
        toiletries.add(new MakeListItem("Shaving Cream", itemChecked));
        toiletries.add(new MakeListItem("Soap", itemChecked));
        toiletries.add(new MakeListItem("Toothpaste", itemChecked));
        toiletries.add(new MakeListItem("Vitamins", itemChecked));
        //Household
        ArrayList<MakeListItem> household = new ArrayList<>();
        household.add(new MakeListItem("Batteries", itemChecked));
        household.add(new MakeListItem("Glue", itemChecked));
        household.add(new MakeListItem("Light Bulbs", itemChecked));
        household.add(new MakeListItem("Tape", itemChecked));

        //Laundry, Paper & Cleaning
        ArrayList<MakeListItem> paperWrap = new ArrayList<>();
        paperWrap.add(new MakeListItem("Aluminum Foil", itemChecked));
        paperWrap.add(new MakeListItem("Bleach", itemChecked));
        paperWrap.add(new MakeListItem("Dishwashing Liquid", itemChecked));
        paperWrap.add(new MakeListItem("Disinfectant Wipes", itemChecked));
        paperWrap.add(new MakeListItem("Garbage Bags", itemChecked));
        paperWrap.add(new MakeListItem("Glass Cleaner", itemChecked));
        paperWrap.add(new MakeListItem("Hand Soap", itemChecked));
        paperWrap.add(new MakeListItem("Household Cleaner", itemChecked));
        paperWrap.add(new MakeListItem("Laundry Detergent", itemChecked));
        paperWrap.add(new MakeListItem("Laundry Softener", itemChecked));
        paperWrap.add(new MakeListItem("Paper Towels", itemChecked));
        paperWrap.add(new MakeListItem("Plastic Bags", itemChecked));
        paperWrap.add(new MakeListItem("Plastic Wrap", itemChecked));
        paperWrap.add(new MakeListItem("Sponges", itemChecked));
        paperWrap.add(new MakeListItem("Tissues", itemChecked));
        paperWrap.add(new MakeListItem("Toilet Paper", itemChecked));
        paperWrap.add(new MakeListItem("Trash Bags", itemChecked));
        //Meat & Fish
        ArrayList<MakeListItem> meat = new ArrayList<>();
        meat.add(new MakeListItem("Bacon", itemChecked));
        meat.add(new MakeListItem("Beef", itemChecked));
        meat.add(new MakeListItem("Fish", itemChecked));
        meat.add(new MakeListItem("Pork", itemChecked));
        meat.add(new MakeListItem("Poultry", itemChecked));
        meat.add(new MakeListItem("Sausage", itemChecked));
        //Pet Items
        ArrayList<MakeListItem> petItems = new ArrayList<>();
        petItems.add(new MakeListItem("Cat Food",itemChecked));
        petItems.add(new MakeListItem("Cat Litter",itemChecked));
        petItems.add(new MakeListItem("Dog Food",itemChecked));
        //Produce
        ArrayList<MakeListItem> produce = new ArrayList<>();
        produce.add(new MakeListItem("Apples", itemChecked));
        produce.add(new MakeListItem("Avocados", itemChecked));
        produce.add(new MakeListItem("Bananas", itemChecked));
        produce.add(new MakeListItem("Berries", itemChecked));
        produce.add(new MakeListItem("Broccoli", itemChecked));
        produce.add(new MakeListItem("Carrots", itemChecked));
        produce.add(new MakeListItem("Cucumber", itemChecked));
        produce.add(new MakeListItem("Garlic", itemChecked));
        produce.add(new MakeListItem("Grapes", itemChecked));
        produce.add(new MakeListItem("Oranges", itemChecked));
        produce.add(new MakeListItem("Lettuce", itemChecked));
        produce.add(new MakeListItem("Melons", itemChecked));
        produce.add(new MakeListItem("Mushrooms", itemChecked));
        produce.add(new MakeListItem("Onions", itemChecked));
        produce.add(new MakeListItem("Peppers", itemChecked));
        produce.add(new MakeListItem("Potatoes", itemChecked));
        produce.add(new MakeListItem("Tomatoes", itemChecked));
        produce.add(new MakeListItem("Zucchini", itemChecked));
        //Rice & Pasta
        ArrayList<MakeListItem> pasta = new ArrayList<>();
        pasta.add(new MakeListItem("Brown Rice", itemChecked));
        pasta.add(new MakeListItem("Lasagna", itemChecked));
        pasta.add(new MakeListItem("Macaroni", itemChecked));
        pasta.add(new MakeListItem("Shells", itemChecked));
        pasta.add(new MakeListItem("Spaghetti", itemChecked));
        pasta.add(new MakeListItem("White Rice", itemChecked));
        //Sauce & Oil
        ArrayList<MakeListItem> sauceOil = new ArrayList<>();
        sauceOil.add(new MakeListItem("BBQ Sauce",itemChecked));
        sauceOil.add(new MakeListItem("Maple Syrup",itemChecked));
        sauceOil.add(new MakeListItem("Oil",itemChecked));
        sauceOil.add(new MakeListItem("Salad Dressing",itemChecked));
        sauceOil.add(new MakeListItem("Soy Sauce",itemChecked));
        sauceOil.add(new MakeListItem("Spaghetti Sauce",itemChecked));
        sauceOil.add(new MakeListItem("Vinegar",itemChecked));
        //Snacks
        ArrayList<MakeListItem> snacks = new ArrayList<>();
        snacks.add(new MakeListItem("Candy",itemChecked));
        snacks.add(new MakeListItem("Chips",itemChecked));
        snacks.add(new MakeListItem("Cookies",itemChecked));
        snacks.add(new MakeListItem("Crackers",itemChecked));
        snacks.add(new MakeListItem("Dip/Salsa",itemChecked));
        snacks.add(new MakeListItem("Nuts",itemChecked));
        snacks.add(new MakeListItem("Popcorn",itemChecked));
        snacks.add(new MakeListItem("Pretzels",itemChecked));
        snacks.add(new MakeListItem("Raisins",itemChecked));
        snacks.add(new MakeListItem("Snack Bars",itemChecked));
        //Spices
        ArrayList<MakeListItem> spices = new ArrayList<>();
        spices.add(new MakeListItem("Basil",itemChecked));
        spices.add(new MakeListItem("Cinnamon",itemChecked));
        spices.add(new MakeListItem("Cumin",itemChecked));
        spices.add(new MakeListItem("Oregano",itemChecked));
        spices.add(new MakeListItem("Pepper",itemChecked));
        spices.add(new MakeListItem("Salt",itemChecked));
        //Vegetarian
        ArrayList<MakeListItem> vegetarian = new ArrayList<>();
        vegetarian.add(new MakeListItem("Almond Milk", itemChecked));
        vegetarian.add(new MakeListItem("Hummus", itemChecked));
        vegetarian.add(new MakeListItem("Soy Milk", itemChecked));
        vegetarian.add(new MakeListItem("Tofu", itemChecked));

        //Adding Lists & Groups to Expandable List
        expandableListDetail.put("Baby & Childcare", baby);
        expandableListDetail.put("Baking", baking);
        expandableListDetail.put("Beverages", beverages);
        expandableListDetail.put("Bread", bread);
        expandableListDetail.put("Breakfast & Cereal", breakfast);
        expandableListDetail.put("Canned Goods", cannedGoods);
        expandableListDetail.put("Condiments", condiments);
        expandableListDetail.put("Dairy", dairy);
        expandableListDetail.put("Deli", deli);
        expandableListDetail.put("Frozen Foods", frozenFoods);
        expandableListDetail.put("Health & Beauty", toiletries);
        expandableListDetail.put("Household", household);
        expandableListDetail.put("Laundry, Paper & Cleaning", paperWrap);
        expandableListDetail.put("Meat & Fish", meat);
        expandableListDetail.put("Pet Items", petItems);
        expandableListDetail.put("Produce", produce);
        expandableListDetail.put("Rice & Pasta",pasta);
        expandableListDetail.put("Sauces & Oils", sauceOil);
        expandableListDetail.put("Snacks", snacks);
        expandableListDetail.put("Spices", spices);
        expandableListDetail.put("Vegetarian", vegetarian);

        return expandableListDetail;
    }
}
