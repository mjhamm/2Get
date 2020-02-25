package com.app.toget;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Iterator;

public class ViewListActivity extends Fragment implements View.OnClickListener {

    //private static final String TAG = "LOG: ";

    private TextView mTextView;
    private static ArrayList<ViewListItem> viewItems;
    private static ViewListFragmentListener listener;
    private Context mContext;
    private ViewListAdapter viewListAdapter;
    private static DatabaseHelper myDB;
    private boolean itemChecked = false;
    private boolean hasItemDetail = false;
    private ImageButton refreshButton;
    private FrameLayout progressHolder;
    private Load myLoad;

    public ViewListActivity() {

    }

    public static ViewListActivity newInstance() {
        return new ViewListActivity();
    }

    //Interface that sends information of the item to be removed to Main Activity
    public interface ViewListFragmentListener {
        void onSelectionARemoved(String selection);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        //Initialize the AsyncTask
        myLoad = new Load();
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

        //Create inflater for list item in order to check on item detail
        LayoutInflater li = LayoutInflater.from(mContext);
        View itemView = li.inflate(R.layout.view_list_item,null);

        //Opens database connection in order to loop through a cursor for the data in the listview
        //This is what determines on startup if the item should be strikethrough or has an item detail
        myDB = DatabaseHelper.getInstance(mContext);
        Cursor data = myDB.getListContents_View();
        if (data.getCount() != 0) {
            while(data.moveToNext()) {
                //Item strikethrough is true if the item in the cursor is not 0
                itemChecked = data.getInt(2) != 0;
                //Item has a detail if the item in the cursor is not empty
                hasItemDetail = !data.getString(3).isEmpty();
                //Item = String itemName, boolean itemChecked, String itemDetail, boolean hasDetail
                ViewListItem item = new ViewListItem(data.getString(1), itemChecked, data.getString(3), hasItemDetail);
                //find the detail textview and determine if it should be shown or not
                TextView mItemDetailText = itemView.findViewById(R.id.viewList_item_detail);
                //Check to see if it has a detail
                //If yes, show it and set the text to the data in the cursor
                if (hasItemDetail) {
                    //Log.e(TAG, "Item: " + item.getItemName());
                    mItemDetailText.setVisibility(View.VISIBLE);
                    mItemDetailText.setText(data.getString(3));
                }
                //add the item to the arraylist of items
                viewItems.add(item);
            }
        }
        //close the cursor
        data.close();
        //set the adapter
        viewListAdapter = new ViewListAdapter(viewItems, mContext);
        mListView.setAdapter(viewListAdapter);

        return view;
    }

    //returns a stringbuilder for when the user wants to share their list
    public StringBuilder exportList() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < viewItems.size(); i++) {
            sb.append("- ");
            sb.append(viewItems.get(i).getItemName());
            if (viewItems.get(i).hasDetail()) {
                sb.append(" ( ");
                sb.append(viewItems.get(i).getItemDetail());
                sb.append(" )");
            }
            if (viewItems.get(i).getIsStrikeThrough()) {
                sb.append(" \u2713");
            }
            if (i < viewItems.size() - 1) {
                sb.append("\n");
            }
        }
        return sb;
    }

    //checks to see if the list is empty or not
    public boolean isListEmpty() {
        return viewItems.isEmpty();
    }

    //adds an item to the list and db
    public void addItemToList(String selection) {
        viewItems.add(new ViewListItem(selection, false, "", false));
        myDB.addDataToView(selection, 0, "", 0);
        viewListAdapter.notifyDataSetChanged();
    }

    //removes an item for the list and db
    public void removeItemFromList(String selection) {
        Cursor data = myDB.getListContents_View();
        if (data.getCount() != 0) {
            while(data.moveToNext()) {
                itemChecked = data.getInt(2) != 0;
                hasItemDetail = !data.getString(3).isEmpty();
            }
        }
        ViewListItem viewListItem = new ViewListItem(selection, itemChecked,"", hasItemDetail);
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

    //Clears all items from the list
    public void clearList() {
        viewItems.clear();
        viewListAdapter.notifyDataSetChanged();
    }

    //checks to make sure that ViewListFragmentListener is implemented on current context
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ViewListFragmentListener) {
            listener = (ViewListFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ViewListFragmentListener");
        }
    }

    //set the listener to null
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //create an animation for the refresh button
        Animation cw = AnimationUtils.loadAnimation(mContext, R.anim.refresh_clockwise);

        //an onClickListener for the refresh button
        //creates an alert dialog that makes sure that the user wants to remove their purchased items
        refreshButton.setOnClickListener(refresh -> {
            refreshButton.startAnimation(cw);
            AlertDialog.Builder clearDialog = new AlertDialog.Builder(mContext);
            clearDialog.setMessage("Are you sure that you want to remove all purchased items?");
            clearDialog.setCancelable(false);

            //Clear List - YES
            clearDialog.setPositiveButton("Confirm", (dialog, which) -> {
                if (!viewItems.isEmpty()) {
                    dialog.dismiss();
                    //singleton to check if the asynctask has ran
                    //if yes then create a new instance of the task and run it
                    if (myLoad != null) {
                        myLoad = new Load();
                    }
                    myLoad.execute();
                } else {
                    Toast.makeText(mContext, "Your List is Empty.", Toast.LENGTH_SHORT).show();
                }
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


    //asynctask in order to remove all purchased items
    private class Load extends AsyncTask<Void, Void, ArrayList<ViewListItem>> {

        final ArrayList<String> selections = new ArrayList<>();

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

    //checks to see if the asynctask and if the fragment is destroyed, cancel the task
    @Override
    public void onDestroy() {
        myLoad.cancel(true);
        super.onDestroy();
    }
}
