package com.example.outof;

import android.os.AsyncTask;

public class LoadDatabase extends AsyncTask<Void,Void,Void> {

    private MakeListActivity makeListActivity;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        makeListActivity = MakeListActivity.newInstance();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        makeListActivity.addDataToDb_Group();
        makeListActivity.addDataToDb_Children();
        return null;
    }
}
