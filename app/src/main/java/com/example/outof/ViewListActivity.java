package com.example.outof;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class ViewListActivity extends Fragment implements View.OnClickListener {

    public static final String TAG = "LOG";

    private TextView mTextView;
    private ArrayList<ViewListItem> viewItems;
    private ViewListFragmentListener listener;
    private Context mContext;
    private ViewListAdapter viewListAdapter;
    private DatabaseHelper myDB;
    private boolean itemChecked = false;
    private ImageButton refreshButton;
    private FrameLayout progressHolder;

    public ViewListActivity() {

    }

    public static ViewListActivity newInstance() {
        return new ViewListActivity();
    }

    public interface ViewListFragmentListener {
        void onSelectionARemoved(String selection);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_list, container,false);
        viewItems = new ArrayList<>();

        ListView mListView = view.findViewById(R.id.viewList);
        mTextView = view.findViewById(R.id.viewList_item_text);
        refreshButton = view.findViewById(R.id.refresh_button);
        progressHolder = view.findViewById(R.id.progressHolder);

        myDB = DatabaseHelper.getInstance(mContext);
        Cursor data = myDB.getListContents_View();
        if (data.getCount() == 0) {
            Log.e(TAG, "Database Empty for view Items");
        } else {
            while(data.moveToNext()) {
                itemChecked = data.getInt(2) != 0;
                ViewListItem item = new ViewListItem(data.getString(1), itemChecked);
                viewItems.add(item);
            }
        }
        data.close();
        viewListAdapter = new ViewListAdapter(viewItems, mContext);
        mListView.setAdapter(viewListAdapter);

        return view;
    }

    public StringBuilder exportList() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < viewItems.size(); i++) {
            sb.append(viewItems.get(i).getItemName());
            if (i < viewItems.size() - 1) {
                sb.append("\n");
            }
        }

        return sb;
    }

    public void addItemToList(String selection) {
        viewItems.add(new ViewListItem(selection, false));
        /*if (!myDB.dupCheckViewTable(selection)) {
            myDB.addDataToView(selection, 0);
        }*/
        myDB.addDataToView(selection, 0);
        viewListAdapter.notifyDataSetChanged();
    }

    public void removeItemFromList(String selection) {
        Cursor data = myDB.getListContents_View();
        if (data.getCount() != 0) {
            while(data.moveToNext()) {
                itemChecked = data.getInt(2) != 0;
            }
        }
        ViewListItem viewListItem = new ViewListItem(selection, itemChecked);
        for (int i = 0; i < viewItems.size(); i++) {
            if (viewListItem.getItemName().equalsIgnoreCase(viewItems.get(i).getItemName())) {
                viewItems.remove(i);
                myDB.removeDataFromView(viewListItem.getItemName());
                break;
            }
        }
        data.close();
        viewListAdapter.notifyDataSetChanged();
    }

    public void clearList() {
        viewItems.clear();
        viewListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ViewListFragmentListener) {
            listener = (ViewListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ViewListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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

        Animation cw = AnimationUtils.loadAnimation(mContext, R.anim.refresh_clockwise);

        refreshButton.setOnClickListener(refresh -> {
            AlertDialog.Builder clearDialog = new AlertDialog.Builder(mContext);
            clearDialog.setMessage("Are you sure that you want to remove all purchased items?");
            clearDialog.setCancelable(false);

            refreshButton.startAnimation(cw);

            //Clear List - YES
            clearDialog.setPositiveButton("Confirm", (dialog, which) -> {
                dialog.dismiss();
                new Load().execute();
            });

            //Cancel Clearing List - NO
            clearDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            AlertDialog alertDialog = clearDialog.create();
            alertDialog.show();

        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.viewList_item_text) {
            if (mTextView.getPaintFlags() == Paint.STRIKE_THRU_TEXT_FLAG) {
                mTextView.setPaintFlags(mTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            } else {
                mTextView.setPaintFlags(mTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
    }

    private class Load extends AsyncTask<Void, Void, ArrayList<ViewListItem>> {

        ArrayList<String> selections = new ArrayList<>();
        /*ViewListFragmentListener mListener;
        ViewListAdapter mAdapter;
        DatabaseHelper db;

        private Load(ArrayList<ViewListItem> items, ViewListFragmentListener listener, ViewListAdapter adapter, DatabaseHelper myDb) {
            mListener = listener;
            mViewItems = items;
            mAdapter = adapter;
            db = myDb;
        }*/

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<ViewListItem> doInBackground(Void... voids) {
            Iterator itr = viewItems.iterator();
            while(itr.hasNext()) {
                ViewListItem item = (ViewListItem) itr.next();
                if (item.getIsStrikeThrough()) {
                    itr.remove();
                    String selection = item.getItemName();
                    selections.add(selection);
                    myDB.removeDataFromView(item.getItemName());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ViewListItem> viewListItems) {
            super.onPostExecute(viewListItems);
            for (String item : selections) {
                listener.onSelectionARemoved(item);
            }
            progressHolder.setVisibility(View.GONE);
            viewListAdapter.notifyDataSetChanged();
        }
    }
}
