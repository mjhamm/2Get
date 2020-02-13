package com.example.outof;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, MakeListActivity.MakeListFragmentListener {

    private Context mContext;
    private MakeListActivity makeListActivity;
    private ViewListActivity viewListActivity;
    private DatabaseHelper myDB;
    private PublisherAdView mPublisherAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager mViewPager = findViewById(R.id.viewPager);
        TabLayout mTabLayout = findViewById(R.id.tabLayout);
        mPublisherAdView = findViewById(R.id.adView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);
        makeListActivity = MakeListActivity.newInstance();
        viewListActivity = ViewListActivity.newInstance();
        mContext = getApplicationContext();

        myDB = DatabaseHelper.getInstance(mContext);
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void showPopup(View view) {
        //hideSoftKeyboard(view);
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.more_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    /*public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }*/

    private void setupViewPager(ViewPager viewPager) {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(makeListActivity, "Make List");
        adapter.addFragment(viewListActivity, "View List");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            //Clear List
            case R.id.clear:
                AlertDialog.Builder clearDialog = new AlertDialog.Builder(MainActivity.this);
                clearDialog.setMessage("Are you sure that you want to clear everything on your list?");
                clearDialog.setCancelable(false);

                //Clear List - YES
                clearDialog.setPositiveButton("Yes", (dialog, which) -> {
                    makeListActivity.clear();
                    viewListActivity.clearList();
                    myDB.clearData();
                    Toast.makeText(mContext, "Your List has been cleared.", Toast.LENGTH_SHORT).show();
                });

                //Cancel Clearing List - NO
                clearDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());

                AlertDialog alertDialog = clearDialog.create();
                alertDialog.show();
                return true;
            case R.id.share:
                //Toast.makeText(mContext, "Share List", Toast.LENGTH_SHORT).show();

                /*viewListActivity.exportToBitmap();
                File imagePath = new File(mContext.getCacheDir(),"images");
                File newFile = new File(imagePath, "list.png");
                Uri contentUri = FileProvider.getUriForFile(mContext, "com.example.outof.fileprovider", newFile);

                if (contentUri != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Here's the Shopping List!");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    startActivity(Intent.createChooser(shareIntent, "Share..."));
                }*/
                if (!viewListActivity.exportList().toString().isEmpty()) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Here's the Shopping List!");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "2Get Shopping List!\n\n" + viewListActivity.exportList().toString());
                    startActivity(Intent.createChooser(shareIntent, "Share..."));
                } else {
                    Toast.makeText(mContext, "You have nothing added to your List.", Toast.LENGTH_SHORT).show();
                }

                return true;
            /*case R.id.print:
                Toast.makeText(mContext, "Print List", Toast.LENGTH_SHORT).show();
                return true;*/
            case R.id.about:
                Toast.makeText(mContext, "About", Toast.LENGTH_SHORT).show();
                Intent aboutIntent = new Intent(MainActivity.this, About.class);
                startActivity(aboutIntent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onDestroy() {
        myDB.close();
        super.onDestroy();
    }

    @Override
    public void onSelectionASent(String selection) {
        viewListActivity.addItemToList(selection);
    }

    @Override
    public void onSelectionBSent(String selection) {
        viewListActivity.removeItemFromList(selection);
    }


}
