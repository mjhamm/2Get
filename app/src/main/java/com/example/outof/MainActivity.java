package com.example.outof;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, MakeListActivity.MakeListFragmentListener, ViewListActivity.ViewListFragmentListener {

    public static final String TAG = "LOG: ";

    private Context mContext;
    private MakeListActivity makeListActivity;
    private ViewListActivity viewListActivity;
    private DatabaseHelper myDB;
    private boolean rotated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.e(TAG, "Ad successfully Loaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.e(TAG, "Ad failed to Load");
            }
        });

        ViewPager mViewPager = findViewById(R.id.viewPager);
        TabLayout mTabLayout = findViewById(R.id.tabLayout);
        makeListActivity = MakeListActivity.newInstance();
        viewListActivity = ViewListActivity.newInstance();
        mContext = getApplicationContext();

        myDB = DatabaseHelper.getInstance(mContext);
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void showPopup(View view) {
        Animation cw = AnimationUtils.loadAnimation(mContext, R.anim.menu_clockwise);
        Animation acw = AnimationUtils.loadAnimation(mContext, R.anim.menu_anti_clockwise);

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.more_menu, popupMenu.getMenu());
        popupMenu.show();

        if (!rotated) {
            view.startAnimation(cw);
            rotated = true;
            cw.setFillAfter(true);
        }

        popupMenu.setOnDismissListener(dismiss -> {
            view.startAnimation(acw);
            rotated = false;
            acw.setFillAfter(true);
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
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
                clearDialog.setPositiveButton("Confirm", (dialog, which) -> {
                    makeListActivity.clear();
                    viewListActivity.clearList();
                    myDB.clearData();
                    Toast.makeText(mContext, "Your List has been cleared.", Toast.LENGTH_SHORT).show();
                });

                //Cancel Clearing List - NO
                clearDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                AlertDialog alertDialog = clearDialog.create();
                alertDialog.show();
                return true;
            case R.id.share:
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
            case R.id.about:
                Intent aboutIntent = new Intent(MainActivity.this, About.class);
                startActivity(aboutIntent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDB.close();
    }

    @Override
    public void onSelectionASent(String selection) {
        viewListActivity.addItemToList(selection);
    }

    @Override
    public void onSelectionBSent(String selection) {
        viewListActivity.removeItemFromList(selection);
    }


    @Override
    public void onSelectionARemoved(String selection) {
        makeListActivity.uncheckItem(selection);
    }
}
