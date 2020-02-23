package com.app.toget;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

class ViewListAdapter extends ArrayAdapter<ViewListItem> {

    private final Context context;
    private final ArrayList<ViewListItem> listItems;
    private final DataSetObservable dataSetObservable = new DataSetObservable();
    private final DatabaseHelper myDB;

    public ViewListAdapter(ArrayList<ViewListItem> listItems, Context context) {
        super(context, R.layout.view_list_item, listItems);
        this.listItems = listItems;
        this.context = context;
        myDB = DatabaseHelper.getInstance(context);
    }

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
        TextView item_detail_textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ViewListItem viewListItem = listItems.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.view_list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.item_textView = convertView.findViewById(R.id.viewList_item_text);
            viewHolder.item_detail_textView = convertView.findViewById(R.id.viewList_item_detail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Check for if item is strikethrough or not
        if (viewListItem.getIsStrikeThrough()) {
            viewHolder.item_textView.setPaintFlags(viewHolder.item_textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.item_detail_textView.setPaintFlags(viewHolder.item_detail_textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.item_textView.setPaintFlags(viewHolder.item_textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            viewHolder.item_detail_textView.setPaintFlags(viewHolder.item_detail_textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        if (viewListItem.hasDetail()) {
            viewHolder.item_detail_textView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.item_detail_textView.setVisibility(View.GONE);
        }

        //Item name onClick Listener
        viewHolder.item_textView.setOnClickListener(v -> {
            if (viewListItem.getIsStrikeThrough()) {
                viewListItem.setStrikeThrough(false);
                myDB.updateView(viewListItem.getItemName(), false, viewListItem.getItemDetail(), viewListItem.hasDetail());
                viewHolder.item_textView.setPaintFlags(viewHolder.item_textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                viewHolder.item_detail_textView.setPaintFlags(viewHolder.item_detail_textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            } else {
                viewListItem.setStrikeThrough(true);
                myDB.updateView(viewListItem.getItemName(), true, viewListItem.getItemDetail(), viewListItem.hasDetail());
                viewHolder.item_textView.setPaintFlags(viewHolder.item_textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.item_detail_textView.setPaintFlags(viewHolder.item_detail_textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

        viewHolder.item_textView.setOnLongClickListener(v -> {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptView = layoutInflater.inflate(R.layout.item_detail_prompt, null);

            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setView(promptView);

            final EditText itemDetail = promptView.findViewById(R.id.item_detail_editText_prompt);

            dialog.setCancelable(true)
                    .setPositiveButton("Confirm", (dialog1, which) -> {
                        if (!itemDetail.getText().toString().isEmpty()) {
                            viewListItem.setHasDetail(true);
                            viewHolder.item_detail_textView.setVisibility(View.VISIBLE);
                            viewListItem.setItemDetail(itemDetail.getText().toString());
                            viewHolder.item_detail_textView.setText(itemDetail.getText().toString());
                            myDB.updateView(viewListItem.getItemName(), viewListItem.getIsStrikeThrough(), itemDetail.getText().toString(), viewListItem.hasDetail());
                        } else {
                            viewListItem.setHasDetail(false);
                            viewHolder.item_detail_textView.setVisibility(View.GONE);
                            myDB.updateView(viewListItem.getItemName(), viewListItem.getIsStrikeThrough(), itemDetail.getText().toString(), viewListItem.hasDetail());
                        }
                    })
                    .setNegativeButton("Cancel", (dialog2, which) -> dialog2.cancel());
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
            return false;
        });
        //Detail onClick Listener
        viewHolder.item_detail_textView.setOnClickListener(v -> {
            if (viewListItem.getIsStrikeThrough()) {
                viewListItem.setStrikeThrough(false);
                myDB.updateView(viewListItem.getItemName(), false, viewListItem.getItemDetail(), viewListItem.hasDetail());
                viewHolder.item_textView.setPaintFlags(viewHolder.item_textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                viewHolder.item_detail_textView.setPaintFlags(viewHolder.item_detail_textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            } else {
                viewListItem.setStrikeThrough(true);
                myDB.updateView(viewListItem.getItemName(), true, viewListItem.getItemDetail(), viewListItem.hasDetail());
                viewHolder.item_textView.setPaintFlags(viewHolder.item_textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.item_detail_textView.setPaintFlags(viewHolder.item_detail_textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

        viewHolder.item_textView.setText(listItems.get(position).getItemName());
        viewHolder.item_detail_textView.setText(viewListItem.getItemDetail());

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
