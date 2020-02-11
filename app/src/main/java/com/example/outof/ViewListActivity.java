package com.example.outof;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

        myDB = DatabaseHelper.getInstance(mContext);
        Cursor data = myDB.getListContents_View();
        if (data.getCount() == 0) {
            Log.e(TAG, "Database Empty for view Items");
        } else {
            while(data.moveToNext()) {
                itemChecked = data.getInt(2) != 0;
                viewItems.add(new ViewListItem(data.getString(1), itemChecked));
            }
        }
        data.close();
        viewListAdapter = new ViewListAdapter(viewItems, mContext);
        mListView.setAdapter(viewListAdapter);

        return view;
    }

    public void exportToBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(mListView.getWidth(), mListView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mListView.draw(canvas);
        try {
            File cachePath = new File(mContext.getCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addItemToList(String selection) {
        /*itemCheckedInt = 0;
        Cursor data = myDB.checkItem(selection);
        if (data.getCount() != 0) {
            itemChecked = data.getInt(2) != 0;
        } else {
            itemChecked = false;
            myDB.addDataToView(selection, 0);
        }*/
        Cursor data = myDB.getListContents_View();
        if (data.getCount() != 0) {
            while(data.moveToNext()) {
                if (data.getInt(2) == 0) {
                    itemChecked = false;
                } else {
                    itemChecked = true;
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

        /*Cursor data = myDB.checkItem(selection);
        if (data.getCount() != 0) {
            itemChecked = data.getInt(2) != 0;
        } else {
            itemChecked = false;
        }*/

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
        viewListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
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
        if (v.getId() == R.id.viewList_item_text) {
            if (mTextView.getPaintFlags() == Paint.STRIKE_THRU_TEXT_FLAG) {
                mTextView.setPaintFlags(mTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                myDB.updateChild(mTextView.getText().toString(), 0);
            } else {
                mTextView.setPaintFlags(mTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                myDB.updateChild(mTextView.getText().toString(), 1);
            }
                /*if (mTextView.getPaintFlags() == Paint.STRIKE_THRU_TEXT_FLAG) {
                    mTextView.setPaintFlags(mTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                } else {
                    mTextView.setPaintFlags(mTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }*/
        }
    }
}
