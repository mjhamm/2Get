package com.example.outof;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class MakeListActivity extends Fragment implements CompoundButton.OnCheckedChangeListener {

    //public static final String TAG = "LOG";

    private Context mContext;
    private ImageButton addCustomItem;
    private MakeListFragmentListener listener;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private ArrayList<String> expandableListTitle;
    private HashMap<String, ArrayList<MakeListItem>> expandableListDetail;
    private ConstraintLayout mAddItemParent;
    private static DatabaseHelper myDB;
    private Cursor childData;
    private View itemView;

    public MakeListActivity() {

    }

    public static MakeListActivity newInstance() {
        return new MakeListActivity();
    }

    public interface MakeListFragmentListener {
        void onSelectionASent(String selection);
        void onSelectionBSent(String selection);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.make_list, container,false);

        final LayoutInflater layoutView  = getLayoutInflater();
        itemView = layoutView.inflate(R.layout.make_list_item, null);

        mAddItemParent = view.findViewById(R.id.addItemParent);
        addCustomItem = view.findViewById(R.id.addCustomItem_button);
        expandableListView = view.findViewById(R.id.makeList);
        expandableListDetail = getData();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandListAdapter(mContext, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        myDB = DatabaseHelper.getInstance(mContext);

        Cursor groupData = myDB.getListContents_Group();
        childData = myDB.getListContents_Children();
        int groupCount = 0;


        //EXPANDING OF GROUPS
        if (groupData.getCount() == 0) {
            LoadDatabase load = new LoadDatabase();
            load.execute();
        } else {
            while(groupData.moveToNext()) { /* Beginning of Moving through group */
                if (groupCount == expandableListAdapter.getGroupCount()) {
                    break;
                } else {
                    if (groupData.getInt(2) != 0) {
                        expandableListView.expandGroup(groupData.getPosition());
                    }
                }
                groupCount++;
            }/* End of Moving through group */
            groupData.close();
        }

        //CHECK CHILDREN
        checkChildren();

        expandableListView.setOnGroupExpandListener(groupPosition -> myDB.updateGroup(expandableListTitle.get(groupPosition), 1));

        expandableListView.setOnGroupCollapseListener(groupPosition -> myDB.updateGroup(expandableListTitle.get(groupPosition), 0));

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            MakeListItem makeListItem = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
            CheckBox itemCheckBox = v.findViewById(R.id.makeList_item_checkbox);

            if (makeListItem.isSelected()) {
                makeListItem.setSelected(false);
                itemCheckBox.setChecked(false);
                String selection = makeListItem.getItemName();
                listener.onSelectionBSent(selection);
                myDB.updateChild(selection, 0);
            } else {
                makeListItem.setSelected(true);
                itemCheckBox.setChecked(true);
                String selection = makeListItem.getItemName();
                listener.onSelectionASent(selection);
                myDB.updateChild(selection, 1);
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
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.make_list_item, null);
        for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
            expandableListView.collapseGroup(i);
            for (int j = 0; j < expandableListAdapter.getChildrenCount(i); j++) {

                MakeListItem makeListItem = (MakeListItem) expandableListAdapter.getChild(i,j);
                CheckBox checkBox = itemView.findViewById(R.id.makeList_item_checkbox);
                if (makeListItem.isSelected()) {
                    makeListItem.setSelected(false);
                    checkBox.setChecked(false);
                }
            }
        }
        myDB.clearGroups();
        myDB.clearChildren();
    }

    public void uncheckItem(String selection) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.make_list_item, null);
        for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {
            expandableListView.collapseGroup(i);
            for (int j = 0; j < expandableListAdapter.getChildrenCount(i); j++) {
                MakeListItem makeListItem = (MakeListItem) expandableListAdapter.getChild(i,j);
                CheckBox checkBox = itemView.findViewById(R.id.makeList_item_checkbox);
                if (makeListItem.getItemName().equalsIgnoreCase(selection)) {
                    makeListItem.setSelected(false);
                    checkBox.setChecked(false);
                    myDB.updateChild(selection, 0);
                }
            }
        }
    }

    private void checkChildren() {
        int childCount = 0;
        int count = 0;
        while(childData.moveToNext()) {
            if (childCount == expandableListAdapter.getChildrenCount(count)) {
                childCount = 0;
                count++;
            }
            MakeListItem makeListItem = (MakeListItem) expandableListAdapter.getChild(count, childCount);
            CheckBox itemCheckBox = itemView.findViewById(R.id.makeList_item_checkbox);
            if (childData.getInt(3) == 1) {
                makeListItem.setSelected(true);
                itemCheckBox.setChecked(true);
            } else {
                makeListItem.setSelected(false);
                itemCheckBox.setChecked(false);
            }
            childCount++;
        }
        childData.close();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Animation cw = AnimationUtils.loadAnimation(mContext, R.anim.add_clockwise);
        Animation acw = AnimationUtils.loadAnimation(mContext, R.anim.add_anti_clockwise);

        addCustomItem.setOnClickListener(v -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle("Add a Custom Item");
            alertDialogBuilder.setCancelable(true);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            final View dialogView = inflater.inflate(R.layout.activity_add_custom_item, mAddItemParent);
            final EditText editText = dialogView.findViewById(R.id.customItemName_EditText);

            alertDialogBuilder.setView(dialogView);
            editText.requestFocus();

            addCustomItem.startAnimation(cw);
            cw.setFillAfter(true);

            alertDialogBuilder.setPositiveButton("Add Item", (dialog, which) -> {
                if (!editText.getText().toString().isEmpty()) {
                    String customItem = editText.getText().toString();
                    listener.onSelectionASent(customItem);
                    //Send item to ViewList and update dataset
                    Toast.makeText(mContext, "Added " + editText.getText().toString() + " to your list.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "You must enter an item to add it to your list.", Toast.LENGTH_SHORT).show();
                }
                addCustomItem.startAnimation(acw);
                acw.setFillAfter(true);
            });

            alertDialogBuilder.setOnDismissListener(dismiss -> {
                addCustomItem.startAnimation(acw);
                acw.setFillAfter(true);
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    public void addDataToDb_Group() {
        //Add all Groups to Database
        myDB.addGroup("Baby & Childcare",0);
        myDB.addGroup("Baking",0);
        myDB.addGroup("Beverages",0);
        myDB.addGroup("Bread",0);
        myDB.addGroup("Breakfast & Cereal",0);
        myDB.addGroup("Canned Goods",0);
        myDB.addGroup("Condiments",0);
        myDB.addGroup("Dairy",0);
        myDB.addGroup("Deli",0);
        myDB.addGroup("Frozen Foods",0);
        myDB.addGroup("Health & Beauty",0);
        myDB.addGroup("Household",0);
        myDB.addGroup("Laundry, Paper & Cleaning",0);
        myDB.addGroup("Meat & Seafood",0);
        myDB.addGroup("Pet Items",0);
        myDB.addGroup("Pre-Baked Goods",0);
        myDB.addGroup("Produce",0);
        myDB.addGroup("Rice & Pasta",0);
        myDB.addGroup("Sauces & Oils",0);
        myDB.addGroup("Snacks",0);
        myDB.addGroup("Spices",0);
    }

    public void addDataToDb_Children() {
        //Add all Children to Database

        //Baby & Childcare
        myDB.addChild("Baby & Childcare", "Baby Food", 0);
        myDB.addChild("Baby & Childcare", "Diapers", 0);
        myDB.addChild("Baby & Childcare", "Formula", 0);
        myDB.addChild("Baby & Childcare", "Wipes", 0);

        //Baking
        myDB.addChild("Baking", "Baking Powder", 0);
        myDB.addChild("Baking", "Baking Soda", 0);
        myDB.addChild("Baking", "Brown Sugar", 0);
        myDB.addChild("Baking", "Flour", 0);
        myDB.addChild("Baking", "Pancake Mix", 0);
        myDB.addChild("Baking", "Sugar", 0);
        myDB.addChild("Baking", "Syrup", 0);
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
        myDB.addChild("Bread", "English Muffins", 0);
        myDB.addChild("Bread", "Hamburger/Hot dog Rolls", 0);
        myDB.addChild("Bread", "Pitas", 0);
        myDB.addChild("Bread", "Rolls", 0);
        myDB.addChild("Bread", "Sandwich", 0);
        myDB.addChild("Bread", "Tortilla", 0);

        //Breakfast & Cereal
        myDB.addChild("Breakfast & Cereal", "Cereal", 0);
        myDB.addChild("Breakfast & Cereal", "Granola", 0);
        myDB.addChild("Breakfast & Cereal", "Granola Bars", 0);
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
        myDB.addChild("Dairy", "Cream",0);
        myDB.addChild("Dairy", "Milk",0);
        myDB.addChild("Dairy", "Sour Cream",0);
        myDB.addChild("Dairy", "Yogurt",0);

        //Deli
        myDB.addChild("Deli", "Deli Cheese", 0);
        myDB.addChild("Deli", "Deli Meats", 0);
        myDB.addChild("Deli", "Deli Salads", 0);

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
        myDB.addChild("Meat & Seafood", "Bacon", 0);
        myDB.addChild("Meat & Seafood", "Beef", 0);
        myDB.addChild("Meat & Seafood", "Burgers", 0);
        myDB.addChild("Meat & Seafood", "Fish", 0);
        myDB.addChild("Meat & Seafood", "Hot dogs", 0);
        myDB.addChild("Meat & Seafood", "Pork", 0);
        myDB.addChild("Meat & Seafood", "Poultry", 0);
        myDB.addChild("Meat & Seafood", "Sausage", 0);
        myDB.addChild("Meat & Seafood", "Shrimp", 0);

        //Pet Items
        myDB.addChild("Pet Items", "Cat Food", 0);
        myDB.addChild("Pet Items", "Cat Litter", 0);
        myDB.addChild("Pet Items", "Dog Food", 0);

        //Pre-Baked Goods
        myDB.addChild("Pre-Baked Goods", "Brownies", 0);
        myDB.addChild("Pre-Baked Goods", "Cake", 0);
        myDB.addChild("Pre-Baked Goods", "Cookies", 0);
        myDB.addChild("Pre-Baked Goods", "Muffins", 0);
        myDB.addChild("Pre-Baked Goods", "Pie", 0);

        //Produce
        myDB.addChild("Produce", "Apples", 0);
        myDB.addChild("Produce", "Avocados", 0);
        myDB.addChild("Produce", "Bananas", 0);
        myDB.addChild("Produce", "Berries", 0);
        myDB.addChild("Produce", "Broccoli", 0);
        myDB.addChild("Produce", "Carrots", 0);
        myDB.addChild("Produce", "Celery", 0);
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
        myDB.addChild("Rice & Pasta", "Pasta", 0);
        myDB.addChild("Rice & Pasta", "White Rice", 0);

        //Sauces & Oil
        myDB.addChild("Sauces & Oils", "BBQ Sauce",0);
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
        myDB.addChild("Spices", "Bay Leaf", 0);
        myDB.addChild("Spices", "Cayenne Pepper", 0);
        myDB.addChild("Spices", "Chili Powder", 0);
        myDB.addChild("Spices", "Cinnamon", 0);
        myDB.addChild("Spices", "Crushed Red Pepper", 0);
        myDB.addChild("Spices", "Cumin", 0);
        myDB.addChild("Spices", "Curry", 0);
        myDB.addChild("Spices", "Garlic Powder/Salt", 0);
        myDB.addChild("Spices", "Ginger", 0);
        myDB.addChild("Spices", "Onion Powder/Salt", 0);
        myDB.addChild("Spices", "Oregano", 0);
        myDB.addChild("Spices", "Nutmeg", 0);
        myDB.addChild("Spices", "Paprika", 0);
        myDB.addChild("Spices", "Pepper", 0);
        myDB.addChild("Spices", "Rosemary", 0);
        myDB.addChild("Spices", "Salt", 0);
        myDB.addChild("Spices", "Turmeric", 0);
    }

    //Creating HashMaps and ArrayLists for information to go into Expandable List View
    private HashMap<String, ArrayList<MakeListItem>> getData() {
        LinkedHashMap<String, ArrayList<MakeListItem>> expandableListDetail = new LinkedHashMap<>();
        //Baby & Childcare
        ArrayList<MakeListItem> baby = new ArrayList<>();
        baby.add(new MakeListItem("Baby Food", false));
        baby.add(new MakeListItem("Diapers", false));
        baby.add(new MakeListItem("Formula", false));
        baby.add(new MakeListItem("Wipes", false));
        //Baking
        ArrayList<MakeListItem> baking = new ArrayList<>();
        baking.add(new MakeListItem("Baking Powder", false));
        baking.add(new MakeListItem("Baking Soda", false));
        baking.add(new MakeListItem("Brown Sugar", false));
        baking.add(new MakeListItem("Flour", false));
        baking.add(new MakeListItem("Pancake Mix", false));
        baking.add(new MakeListItem("Sugar", false));
        baking.add(new MakeListItem("Syrup", false));
        baking.add(new MakeListItem("Vanilla", false));
        baking.add(new MakeListItem("Yeast", false));
        //Beverages
        ArrayList<MakeListItem> beverages = new ArrayList<>();
        beverages.add(new MakeListItem("Coffee", false));
        beverages.add(new MakeListItem("Juice", false));
        beverages.add(new MakeListItem("Soda", false));
        beverages.add(new MakeListItem("Tea", false));
        beverages.add(new MakeListItem("Water", false));
        //Bread
        ArrayList<MakeListItem> bread = new ArrayList<>();
        bread.add(new MakeListItem("Bagels", false));
        bread.add(new MakeListItem("English Muffins", false));
        bread.add(new MakeListItem("Hamburger/Hot dog Rolls", false));
        bread.add(new MakeListItem("Pitas", false));
        bread.add(new MakeListItem("Rolls", false));
        bread.add(new MakeListItem("Sandwich", false));
        bread.add(new MakeListItem("Tortilla", false));
        //Breakfast & Cereal
        ArrayList<MakeListItem> breakfast = new ArrayList<>();
        breakfast.add(new MakeListItem("Cereal", false));
        breakfast.add(new MakeListItem("Granola", false));
        breakfast.add(new MakeListItem("Granola Bars", false));
        breakfast.add(new MakeListItem("Oatmeal", false));
        //Canned Goods
        ArrayList<MakeListItem> cannedGoods = new ArrayList<>();
        cannedGoods.add(new MakeListItem("Fruit", false));
        cannedGoods.add(new MakeListItem("Pumpkin", false));
        cannedGoods.add(new MakeListItem("Soup", false));
        cannedGoods.add(new MakeListItem("Tomato Sauce", false));
        cannedGoods.add(new MakeListItem("Tuna", false));
        cannedGoods.add(new MakeListItem("Vegetables", false));
        //Condiments
        ArrayList<MakeListItem> condiments = new ArrayList<>();
        condiments.add(new MakeListItem("Jelly", false));
        condiments.add(new MakeListItem("Ketchup", false));
        condiments.add(new MakeListItem("Mayonnaise", false));
        condiments.add(new MakeListItem("Mustard", false));
        condiments.add(new MakeListItem("Peanut Butter", false));
        condiments.add(new MakeListItem("Pickles", false));
        condiments.add(new MakeListItem("Relish", false));
        //Dairy
        ArrayList<MakeListItem> dairy = new ArrayList<>();
        dairy.add(new MakeListItem("Butter", false));
        dairy.add(new MakeListItem("Cheese", false));
        dairy.add(new MakeListItem("Cream", false));
        dairy.add(new MakeListItem("Milk", false));
        dairy.add(new MakeListItem("Sour Cream", false));
        dairy.add(new MakeListItem("Yogurt", false));
        //Deli
        ArrayList<MakeListItem> deli = new ArrayList<>();
        deli.add(new MakeListItem("Deli Cheese", false));
        deli.add(new MakeListItem("Deli Meats", false));
        deli.add(new MakeListItem("Deli Salads", false));
        //Frozen Foods
        ArrayList<MakeListItem> frozenFoods = new ArrayList<>();
        frozenFoods.add(new MakeListItem("Ice Cream", false));
        frozenFoods.add(new MakeListItem("Frozen Meals", false));
        frozenFoods.add(new MakeListItem("Frozen Pizza", false));
        frozenFoods.add(new MakeListItem("Frozen Potatoes", false));
        frozenFoods.add(new MakeListItem("Frozen Vegetables", false));
        frozenFoods.add(new MakeListItem("Frozen Waffles", false));
        //Health & Beauty
        ArrayList<MakeListItem> toiletries = new ArrayList<>();
        toiletries.add(new MakeListItem("Bandages", false));
        toiletries.add(new MakeListItem("Cold Medicine", false));
        toiletries.add(new MakeListItem("Conditioner", false));
        toiletries.add(new MakeListItem("Deodorant", false));
        toiletries.add(new MakeListItem("Floss", false));
        toiletries.add(new MakeListItem("Lotion", false));
        toiletries.add(new MakeListItem("Pain Relievers", false));
        toiletries.add(new MakeListItem("Razors", false));
        toiletries.add(new MakeListItem("Shampoo", false));
        toiletries.add(new MakeListItem("Shaving Cream", false));
        toiletries.add(new MakeListItem("Soap", false));
        toiletries.add(new MakeListItem("Toothpaste", false));
        toiletries.add(new MakeListItem("Vitamins", false));
        //Household
        ArrayList<MakeListItem> household = new ArrayList<>();
        household.add(new MakeListItem("Batteries", false));
        household.add(new MakeListItem("Glue", false));
        household.add(new MakeListItem("Light Bulbs", false));
        household.add(new MakeListItem("Tape", false));

        //Laundry, Paper & Cleaning
        ArrayList<MakeListItem> paperWrap = new ArrayList<>();
        paperWrap.add(new MakeListItem("Aluminum Foil", false));
        paperWrap.add(new MakeListItem("Bleach", false));
        paperWrap.add(new MakeListItem("Dishwashing Liquid", false));
        paperWrap.add(new MakeListItem("Disinfectant Wipes", false));
        paperWrap.add(new MakeListItem("Garbage Bags", false));
        paperWrap.add(new MakeListItem("Glass Cleaner", false));
        paperWrap.add(new MakeListItem("Hand Soap", false));
        paperWrap.add(new MakeListItem("Household Cleaner", false));
        paperWrap.add(new MakeListItem("Laundry Detergent", false));
        paperWrap.add(new MakeListItem("Laundry Softener", false));
        paperWrap.add(new MakeListItem("Paper Towels", false));
        paperWrap.add(new MakeListItem("Plastic Bags", false));
        paperWrap.add(new MakeListItem("Plastic Wrap", false));
        paperWrap.add(new MakeListItem("Sponges", false));
        paperWrap.add(new MakeListItem("Tissues", false));
        paperWrap.add(new MakeListItem("Toilet Paper", false));
        paperWrap.add(new MakeListItem("Trash Bags", false));
        //Meat & Fish
        ArrayList<MakeListItem> meat = new ArrayList<>();
        meat.add(new MakeListItem("Bacon", false));
        meat.add(new MakeListItem("Beef", false));
        meat.add(new MakeListItem("Burgers", false));
        meat.add(new MakeListItem("Fish", false));
        meat.add(new MakeListItem("Hot dogs", false));
        meat.add(new MakeListItem("Pork", false));
        meat.add(new MakeListItem("Poultry", false));
        meat.add(new MakeListItem("Sausage", false));
        meat.add(new MakeListItem("Shrimp", false));
        //Pet Items
        ArrayList<MakeListItem> petItems = new ArrayList<>();
        petItems.add(new MakeListItem("Cat Food", false));
        petItems.add(new MakeListItem("Cat Litter", false));
        petItems.add(new MakeListItem("Dog Food", false));
        //Pre-Baked Goods
        ArrayList<MakeListItem> preBaked = new ArrayList<>();
        preBaked.add(new MakeListItem("Brownies",false));
        preBaked.add(new MakeListItem("Cake",false));
        preBaked.add(new MakeListItem("Cookies",false));
        preBaked.add(new MakeListItem("Muffins",false));
        preBaked.add(new MakeListItem("Pie",false));
        //Produce
        ArrayList<MakeListItem> produce = new ArrayList<>();
        produce.add(new MakeListItem("Apples", false));
        produce.add(new MakeListItem("Avocados", false));
        produce.add(new MakeListItem("Bananas", false));
        produce.add(new MakeListItem("Berries", false));
        produce.add(new MakeListItem("Broccoli", false));
        produce.add(new MakeListItem("Carrots", false));
        produce.add(new MakeListItem("Celery", false));
        produce.add(new MakeListItem("Cucumber", false));
        produce.add(new MakeListItem("Garlic", false));
        produce.add(new MakeListItem("Grapes", false));
        produce.add(new MakeListItem("Oranges", false));
        produce.add(new MakeListItem("Lettuce", false));
        produce.add(new MakeListItem("Melons", false));
        produce.add(new MakeListItem("Mushrooms", false));
        produce.add(new MakeListItem("Onions", false));
        produce.add(new MakeListItem("Peppers", false));
        produce.add(new MakeListItem("Potatoes", false));
        produce.add(new MakeListItem("Tomatoes", false));
        produce.add(new MakeListItem("Zucchini", false));
        //Rice & Pasta
        ArrayList<MakeListItem> pasta = new ArrayList<>();
        pasta.add(new MakeListItem("Brown Rice", false));
        pasta.add(new MakeListItem("Pasta", false));
        pasta.add(new MakeListItem("White Rice", false));
        //Sauce & Oil
        ArrayList<MakeListItem> sauceOil = new ArrayList<>();
        sauceOil.add(new MakeListItem("BBQ Sauce", false));
        sauceOil.add(new MakeListItem("Oil", false));
        sauceOil.add(new MakeListItem("Salad Dressing", false));
        sauceOil.add(new MakeListItem("Soy Sauce", false));
        sauceOil.add(new MakeListItem("Spaghetti Sauce", false));
        sauceOil.add(new MakeListItem("Vinegar", false));
        //Snacks
        ArrayList<MakeListItem> snacks = new ArrayList<>();
        snacks.add(new MakeListItem("Candy", false));
        snacks.add(new MakeListItem("Chips", false));
        snacks.add(new MakeListItem("Cookies", false));
        snacks.add(new MakeListItem("Crackers", false));
        snacks.add(new MakeListItem("Dip/Salsa", false));
        snacks.add(new MakeListItem("Nuts", false));
        snacks.add(new MakeListItem("Popcorn", false));
        snacks.add(new MakeListItem("Pretzels", false));
        snacks.add(new MakeListItem("Raisins", false));
        snacks.add(new MakeListItem("Snack Bars", false));
        //Spices
        ArrayList<MakeListItem> spices = new ArrayList<>();
        spices.add(new MakeListItem("Basil", false));
        spices.add(new MakeListItem("Bay Leaf", false));
        spices.add(new MakeListItem("Cayenne Pepper", false));
        spices.add(new MakeListItem("Chili Powder", false));
        spices.add(new MakeListItem("Cinnamon", false));
        spices.add(new MakeListItem("Crushed Red Pepper", false));
        spices.add(new MakeListItem("Cumin", false));
        spices.add(new MakeListItem("Curry", false));
        spices.add(new MakeListItem("Garlic Powder/Salt", false));
        spices.add(new MakeListItem("Ginger", false));
        spices.add(new MakeListItem("Onion Powder/Salt", false));
        spices.add(new MakeListItem("Oregano", false));
        spices.add(new MakeListItem("Nutmeg", false));
        spices.add(new MakeListItem("Paprika", false));
        spices.add(new MakeListItem("Pepper", false));
        spices.add(new MakeListItem("Rosemary", false));
        spices.add(new MakeListItem("Salt", false));
        spices.add(new MakeListItem("Turmeric", false));

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
        expandableListDetail.put("Meat & Seafood", meat);
        expandableListDetail.put("Pet Items", petItems);
        expandableListDetail.put("Pre-Baked Goods", preBaked);
        expandableListDetail.put("Produce", produce);
        expandableListDetail.put("Rice & Pasta",pasta);
        expandableListDetail.put("Sauces & Oils", sauceOil);
        expandableListDetail.put("Snacks", snacks);
        expandableListDetail.put("Spices", spices);

        return expandableListDetail;
    }
}
