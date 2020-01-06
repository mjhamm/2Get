package com.example.outof;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MakeListAdapter extends ArrayAdapter<MakeListItem> {

    public ArrayList<MakeListItem> makeListItems;
    private Context context;

    public MakeListAdapter(ArrayList<MakeListItem> makeListItems, Context context) {
        super(context, R.layout.make_list_item, makeListItems);
        this.makeListItems = makeListItems;
        this.context = context;
    }

    private static class MakeItemViewHolder {
        public TextView itemName;
        public CheckBox checkBox;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        MakeItemViewHolder holder = new MakeItemViewHolder();

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.make_list_item, null);

            holder.itemName = view.findViewById(R.id.makeList_item_text);
            holder.checkBox = view.findViewById(R.id.makeList_item_checkbox);
            view.setTag(holder);
        } else {
            holder = (MakeItemViewHolder) view.getTag();
        }

        MakeListItem item = makeListItems.get(position);
        holder.itemName.setText(item.getItemName());
        holder.checkBox.setChecked(item.isSelected());
        holder.checkBox.setTag(item);

        return view;
    }

    @Override
    public int getCount() {
        return makeListItems.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Nullable
    @Override
    public MakeListItem getItem(int position) {
        return makeListItems.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
