package com.example.outof;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ViewListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ViewListItem> listItems;
    private final DataSetObservable dataSetObservable = new DataSetObservable();

    public ViewListAdapter(Context context, ArrayList<ViewListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView item_textView;
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
        this.notifyDataSetChanged();

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
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}
