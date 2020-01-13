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
import android.widget.BaseAdapter;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MakeListActivity extends Fragment implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "LOG";

    private ArrayList<MakeListItem> makeItems;
    private Context mContext;
    private ImageButton addCustomItem;

    private MakeListFragmentListener listener;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private ArrayList<String> expandableListTitle;
    private HashMap<String, ArrayList<MakeListItem>> expandableListDetail;
    private ConstraintLayout mAddItemParent;

    public MakeListActivity() {

    }

    public static MakeListActivity newInstance() {
        return new MakeListActivity();
    }

    public interface MakeListFragmentListener {
        void onSelectionASent(String selection);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.make_list, container,false);
        makeItems = new ArrayList<>();

        mAddItemParent = view.findViewById(R.id.addItemParent);
        addCustomItem = view.findViewById(R.id.addCustomItem_button);
        expandableListView = view.findViewById(R.id.makeList);
        expandableListDetail = ExpandableListData.getData();
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(mContext, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(groupPosition -> Toast.makeText(mContext, expandableListTitle.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show());

        expandableListView.setOnGroupCollapseListener(groupPosition -> Toast.makeText(mContext, expandableListTitle.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show());

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            MakeListItem makeListItem = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
            CheckBox itemCheckBox = v.findViewById(R.id.makeList_item_checkbox);

            if (makeListItem.isSelected()) {
                makeListItem.setSelected(false);
                itemCheckBox.setChecked(false);
                String selection = makeListItem.getItemName();
                listener.onSelectionASent(selection);
                //Toast.makeText(mContext, makeListItem.getItemName() + " is NOT Selected.", Toast.LENGTH_SHORT).show();
            } else {
                makeListItem.setSelected(true);
                itemCheckBox.setChecked(true);
                String selection = makeListItem.getItemName();
                listener.onSelectionASent(selection);
                //Toast.makeText(mContext, makeListItem.getItemName() + " is Selected.", Toast.LENGTH_SHORT).show();
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
            alertDialogBuilder.setCancelable(false);

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

            alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
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
}
