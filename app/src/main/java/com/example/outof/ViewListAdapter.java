package com.example.outof;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ViewListAdapter extends ArrayAdapter<ViewListItem> {

    private Context context;
    public ArrayList<ViewListItem> listItems;
    public DataSetObservable dataSetObservable = new DataSetObservable();

    public ViewListAdapter(ArrayList<ViewListItem> listItems, Context context) {
        super(context, R.layout.view_list_item, listItems);
        this.listItems = listItems;
        this.context = context;
    }

    /*public ViewListAdapter(Context context, ArrayList<ViewListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }*/

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public ViewListItem getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView item_textView;
        boolean item_isStrikeThrough;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ViewListItem viewListItem = listItems.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.view_list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.item_textView = convertView.findViewById(R.id.viewList_item_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ViewListItem item = getItem(position);

        if (viewListItem.getIsStrikeThrough()) {
            viewHolder.item_textView.setPaintFlags(viewHolder.item_textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.item_textView.setPaintFlags(viewHolder.item_textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        viewHolder.item_textView.setOnClickListener(v -> {
            if (viewListItem.getIsStrikeThrough()) {
                viewListItem.setStrikeThrough(false);
                viewHolder.item_textView.setPaintFlags(viewHolder.item_textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            } else {
                viewListItem.setStrikeThrough(true);
                viewHolder.item_textView.setPaintFlags(viewHolder.item_textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

        viewHolder.item_textView.setText(listItems.get(position).getItemName());
        viewHolder.item_textView.setTag(position);
        viewHolder.item_isStrikeThrough = listItems.get(position).getIsStrikeThrough();
        notifyDataSetChanged();

        return convertView;
    }

    @Override
    public void remove(@Nullable ViewListItem object) {
        super.remove(object);
    }

    @Override
    public int getPosition(@Nullable ViewListItem item) {
        return super.getPosition(item);
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
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}
