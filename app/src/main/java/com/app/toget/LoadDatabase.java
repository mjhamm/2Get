package com.app.toget;

import android.os.AsyncTask;

class LoadDatabase extends AsyncTask<Void,Void,Void> {

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
