package com.example.outof;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MakeListActivity extends Fragment implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "LOG";

    private ExpandableListView mListView;
    private ArrayList<MakeListItem> makeItems;
    private Context mContext;
    private ImageButton addCustomItem;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private ArrayList<String> expandableListTitle;
    private HashMap<String, ArrayList<MakeListItem>> expandableListDetail;

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

        addCustomItem = view.findViewById(R.id.addCustomItem_button);
        expandableListView = view.findViewById(R.id.makeList);
        expandableListDetail = ExpandableListData.getData();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(mContext, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(groupPosition -> Toast.makeText(mContext, expandableListTitle.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show());

        expandableListView.setOnGroupCollapseListener(groupPosition -> Toast.makeText(mContext, expandableListTitle.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show());

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                MakeListItem makeListItem = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);

                //Check
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);

                CheckBox checkBox = v.findViewById(R.id.makeList_item_checkbox);
                if (makeListItem.isSelected()) {
                    checkBox.setChecked(false);
                    makeListItem.setSelected(false);
                } else {
                    checkBox.setChecked(true);
                    makeListItem.setSelected(true);
                }
                Toast.makeText(mContext, expandableListTitle.get(groupPosition) + "-> " + expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).getItemName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Expandable List View
        expandableListView.setOnItemClickListener((parent, view1, position, id) -> {
            CheckBox checkBox = view1.findViewById(R.id.makeList_item_checkbox);
            checkBox.performClick();

            int pos = expandableListView.getPositionForView(view1);
            if (pos != ExpandableListView.INVALID_POSITION) {
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
            /*final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.activity_add_custom_item);
            dialog.setCancelable(false);

            final Spinner spinner = dialog.findViewById(R.id.customItemCategory_spinner);
            final EditText editText = dialog.findViewById(R.id.customItemName_EditText);
            Button cancelButton = dialog.findViewById(R.id.cancelCustomItem_button);
            Button addButton = dialog.findViewById(R.id.addCustomItem_button);

            dialog.show();*/

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle("Add a Custom Item");

            LayoutInflater inflater = LayoutInflater.from(mContext);
            final View dialogView = inflater.inflate(R.layout.activity_add_custom_item, null);

            final Spinner spinner = dialogView.findViewById(R.id.customItemCategory_spinner);
            final EditText editText = dialogView.findViewById(R.id.customItemName_EditText);
            //Button cancelButton = dialogView.findViewById(R.id.cancelCustomItem_button);
            //Button addButton = dialogView.findViewById(R.id.addCustomItem_button);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.category, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view12, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            alertDialogBuilder.setView(dialogView);

            alertDialogBuilder.setPositiveButton("Add Item to List", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        CheckBox checkBox = getView() != null ? getView().findViewById(R.id.makeList_item_checkbox) : null;
        ExpandableListView expandableListView = getView() != null ? getView().findViewById(R.id.makeList) : null;
        if (expandableListView != null) {
            int groupsCount = expandableListView.getExpandableListAdapter().getGroupCount();
            boolean[] groupExpandedArray = new boolean[groupsCount];
            for (int i = 0; i < groupsCount; i += 1) {
                groupExpandedArray[i] = expandableListView.isGroupExpanded(i);
            }
            outState.putBooleanArray("groupExpandedArray", groupExpandedArray);
            outState.putInt("firstVisiblePosition", expandableListView.getFirstVisiblePosition());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            boolean[] groupExpandedArray = savedInstanceState.getBooleanArray("groupExpandedArray");
            int firstVisiblePosition = savedInstanceState.getInt("firstVisiblePosition", -1);
            ExpandableListView expandableListView = getView() instanceof ViewGroup ? getView().findViewById(R.id.makeList) : null;
            if (expandableListView != null && groupExpandedArray != null) {
                for (int i = 0; i <groupExpandedArray.length; i++) {
                    if (groupExpandedArray[i]) {
                        expandableListView.expandGroup(i);
                    }
                }
                if (firstVisiblePosition >= 0) {
                    expandableListView.setSelection(firstVisiblePosition);
                }
            }
        }
    }

    public void clear() {
        /*if (makeItems != null) {
            for (MakeListItem item : makeItems) {
                item.setSelected(false);
            }
        } else {
            Log.e(TAG, "Array is Empty");
        }*/
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

    /*public void displayMakeList() {
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
    }*/

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
