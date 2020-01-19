package com.example.outof;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ViewListActivity extends Fragment implements View.OnClickListener {

    public static final String TAG = "LOG";

    private ListView mListView;
    private TextView mTextView;
    private ArrayList<ViewListItem> viewItems;
    private Context mContext;
    private ViewListAdapter viewListAdapter;
    private ArrayList<String> listItems;
    private DatabaseHelper myDB;
    private int itemCheckedInt = 0;
    private boolean itemChecked = false;

    public ViewListActivity() {

    }

    public static ViewListActivity newInstance() {
        return new ViewListActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_list, container,false);
        viewItems = new ArrayList<>();

        mListView = view.findViewById(R.id.viewList);
        mTextView = view.findViewById(R.id.viewList_item_text);

        myDB = new DatabaseHelper(mContext);
        Cursor data = myDB.getListContents_View();
        if (data.getCount() == 0) {
            Toast.makeText(mContext, "Database Empty", Toast.LENGTH_SHORT).show();
        } else {
            while(data.moveToNext()) {
                if (data.getInt(2) == 0) {
                    itemChecked = false;
                } else {
                    itemChecked = true;
                }
                viewItems.add(new ViewListItem(data.getString(1), itemChecked));
            }
        }
        data.close();
        viewListAdapter = new ViewListAdapter(viewItems, mContext);
        mListView.setAdapter(viewListAdapter);

        return view;
    }

    public void addItemToList(String selection) {
        itemCheckedInt = 0;
        Cursor data = myDB.getListContents_View();
        if (data.getCount() != 0) {
            while(data.moveToNext()) {
                if (data.getInt(2) == 0) {
                    itemCheckedInt = 0;
                } else {
                    itemCheckedInt = 1;
                }
            }
        }
        ViewListItem viewListItem = new ViewListItem(selection, itemChecked);
        if (!myDB.dupCheckViewTable(selection)) {
            myDB.addDataToView(viewListItem.getItemName(), itemCheckedInt);
        }
        data.close();
        viewItems.add(viewListItem);
        viewListAdapter.notifyDataSetChanged();
    }

    public void removeItemFromList(String selection) {
        ViewListItem viewListItem = new ViewListItem(selection, false);
        for (int i = 0; i < viewItems.size(); i++) {
            if (viewListItem.getItemName().equalsIgnoreCase(viewItems.get(i).getItemName())) {
                viewItems.remove(i);
                myDB.removeDataFromView(viewListItem.getItemName());
                viewListAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public void clearList() {
        viewItems.clear();
        myDB.clearData();
        viewListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewList_item_text:
                if (mTextView.getPaintFlags() == Paint.STRIKE_THRU_TEXT_FLAG) {
                    mTextView.setPaintFlags(mTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                } else {
                    mTextView.setPaintFlags(mTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myDB.close();
    }
}
