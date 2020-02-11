package com.example.outof;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    public static final String TAG = "LOG:";

    private Context context;
    private ArrayList<String> expandableListTitle;
    private HashMap<String, ArrayList<MakeListItem>> expandableListDetail;
    private final DataSetObservable dataSetObservable = new DataSetObservable();
    private DatabaseHelper myDB;

    public CustomExpandableListAdapter(Context context, ArrayList<String> expandableListTitle, HashMap<String, ArrayList<MakeListItem>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        myDB = DatabaseHelper.getInstance(context);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.expandableListTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        String listTitle = (String) getGroup(listPosition);

        if (convertView == null) {
            //check
            convertView = LayoutInflater.from(context).inflate(R.layout.list_group, null);
            groupHolder = new GroupHolder();
            groupHolder.groupTitle = convertView.findViewById(R.id.listTitle);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        groupHolder.groupTitle.setText(listTitle);
        this.notifyDataSetChanged();

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final MakeListItem expandedListItem = (MakeListItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.make_list_item, null);
            convertView.setTag(expandedListItem.getItemName());
        } else {
            convertView.getTag();
        }
        CheckBox item_checkBox = convertView.findViewById(R.id.makeList_item_checkbox);
        TextView item_textView = convertView.findViewById(R.id.makeList_item_text);

        if (expandedListItem.isSelected()) {
            item_checkBox.setChecked(true);
        } else {
            item_checkBox.setChecked(false);
        }
        notifyDataSetChanged();

        item_checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            notifyDataSetChanged();
        });

        item_textView.setText(expandedListItem.getItemName());

        return convertView;
    }

    private DataSetObservable getDataSetObservable() {
        return dataSetObservable;
    }

    @Override
    public void notifyDataSetChanged() {
        this.getDataSetObservable().notifyChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        this.getDataSetObservable().notifyInvalidated();
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.getDataSetObservable().unregisterObserver(observer);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        this.getDataSetObservable().registerObserver(observer);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        TextView groupTitle;
    }
}
