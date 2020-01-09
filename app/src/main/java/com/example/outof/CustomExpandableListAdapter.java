package com.example.outof;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> expandableListTitle;
    private HashMap<String, ArrayList<MakeListItem>> expandableListDetail;
    private HashMap<Integer, boolean[]> mChildCheckStates = new HashMap<>();

    public CustomExpandableListAdapter(Context context, ArrayList<String> expandableListTitle, HashMap<String, ArrayList<MakeListItem>> expandableListDetail) {
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
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_group, null);
            //LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            groupHolder = new GroupHolder();
            groupHolder.groupTitle = convertView.findViewById(R.id.listTitle);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.groupTitle.setTypeface(null, Typeface.BOLD);
        groupHolder.groupTitle.setText(listTitle);
        //listTitleTextView.setTypeface(null, Typeface.BOLD);
        //listTitleTextView.setText(listTitle);
        return convertView;
    }



    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final MakeListItem expandedListItem = (MakeListItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.make_list_item, null);
            convertView.setTag(expandedListItem.getItemName());
        } else {
            convertView.getTag();
        }
        CheckBox item_checkBox = convertView.findViewById(R.id.makeList_item_checkbox);
        TextView item_textView = convertView.findViewById(R.id.makeList_item_text);

        if (mChildCheckStates.containsKey(groupPosition)) {
            boolean[] getChecked = mChildCheckStates.get(groupPosition);
            item_checkBox.setChecked(getChecked[childPosition]);
        } else {
            boolean[] getChecked = new boolean[getChildrenCount(groupPosition)];
            mChildCheckStates.put(groupPosition, getChecked);
            item_checkBox.setChecked(false);
        }

        item_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandedListItem.isSelected()) {
                    item_checkBox.setChecked(false);
                    expandedListItem.setSelected(false);
                } else {
                    item_checkBox.setChecked(true);
                    expandedListItem.setSelected(true);
                }
            }
        });
        notifyDataSetChanged();

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        TextView groupTitle;
    }
}
