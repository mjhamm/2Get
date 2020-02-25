package com.app.toget;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

class ExpandListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    // Group Titles
    private final ArrayList<String> expandableListTitle;

    // Children Information with link to group
    private final HashMap<String, ArrayList<MakeListItem>> expandableListDetail;
    private final DataSetObservable dataSetObservable = new DataSetObservable();

    public ExpandListAdapter(Context context, ArrayList<String> expandableListTitle, HashMap<String, ArrayList<MakeListItem>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
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
        //Create Group holder
        GroupHolder groupHolder;

        //get group title from group position in list
        String listTitle = (String) getGroup(listPosition);

        //Check if convertview is null
        //Inflate view
        //Initialize Group Holder
        //find grouptitle textView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_group, null);
            groupHolder = new GroupHolder();
            groupHolder.groupTitle = convertView.findViewById(R.id.listTitle);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        //Set the group title
        groupHolder.groupTitle.setText(listTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //Get child item using group and position
        final MakeListItem expandedListItem = (MakeListItem) getChild(groupPosition, childPosition);

        //Create Child Holder
        ChildHolder childHolder;

        //Check if convertview is null
        //Inflate view
        //Initialize Child Holder
        //find child textView and CheckBox
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.make_list_item, null);
            childHolder = new ChildHolder();
            childHolder.childTitle = convertView.findViewById(R.id.makeList_item_text);
            childHolder.childCheckBox = convertView.findViewById(R.id.makeList_item_checkbox);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        // Make sure child item is checked or not based off of isSelected Method in Make List Item
        if (expandedListItem.isSelected()) {
            childHolder.childCheckBox.setChecked(true);
        } else {
            childHolder.childCheckBox.setChecked(false);
        }

        //Set the Child title
        childHolder.childTitle.setText(expandedListItem.getItemName());

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

    // Group Holder Class
    static class GroupHolder {
        TextView groupTitle;
    }

    // Child Holder Class
    static class ChildHolder {
        TextView childTitle;
        CheckBox childCheckBox;
    }
}
